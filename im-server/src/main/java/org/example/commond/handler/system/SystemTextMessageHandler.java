package org.example.commond.handler.system;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.example.commond.AbstractCmdHandler;
import org.example.config.Chat;
import org.example.config.Im;
import org.example.enums.CommandEnum;
import org.example.enums.DefaultEnum;
import org.example.packets.Status;
import org.example.packets.bean.Group;
import org.example.packets.bean.Message;
import org.example.packets.bean.User;
import org.example.packets.bean.UserGroup;
import org.example.packets.handler.ChatRespBody;
import org.example.packets.handler.RespBody;
import org.example.packets.handler.room.JoinGroupNotifyBody;
import org.example.packets.handler.system.SystemMessageReqBody;
import org.example.packets.handler.system.SystemTextMessage;
import org.example.protocol.http.service.UploadService;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

import java.util.List;

public class SystemTextMessageHandler extends AbstractCmdHandler {

    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_SYSTEM_MESSAGE_REQ;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext channelContext) {
        WsRequest request = (WsRequest) packet;

        SystemTextMessage body = JSONObject.parseObject(request.getBody(), SystemTextMessage.class);

        User system = initSystemAccount(body.getSenderId());

        Message message = body.build(system.getId());

        for (String receiver : body.getReceivers()) {
            Group group = init(receiver);
            message.setId(IdUtil.getSnowflake().nextIdStr());
            message.setRoomId(group.getRoomId());
            messageService.putGroupMessage(message);

            groupService.updateLastMessage(message);

            ChatRespBody response = BeanUtil.copyProperties(message, ChatRespBody.class);
            // 发送给群组用户
            Chat.sendToGroup(response);
        }

        return null;
    }

    private User initSystemAccount(String account) {

        String url = UploadService.uploadDefault(DefaultEnum.LOGO);

        User system = userService.getByAccount(account);
        if (system == null) {
            User build = User.builder().account("SYSTEM").id(IdUtil.getSnowflake().nextIdStr())
                    .status(Status.online())
                    .avatar(url)
                    .username("信使").isSystem(true).build();
            userService.saveOrUpdate(build);
            return build;
        }
        return system;
    }

    private Group init(String receiver) {
        Group group;
        UserGroup userGroup = userGroupService.getSystemUserGroup(receiver);
        if (userGroup == null) {
            String url = UploadService.uploadDefault(DefaultEnum.LOGO);
            group = Group.builder().roomId(IdUtil.getSnowflake().nextIdStr())
                    .index(System.currentTimeMillis())
                    .avatar(url)
                    .isFriend(false)
                    .isSystem(true)
                    .isDeleted(false).roomName("系统消息").build();
            userGroupService.addGroupUser(group.getRoomId(), receiver, true);
            groupService.saveOrUpdateById(group);


            JoinGroupNotifyBody joinGroupNotifyBody = new JoinGroupNotifyBody();
            joinGroupNotifyBody.setGroup(group);
            joinGroupNotifyBody.setUsers(CollUtil.newArrayList(userService.getUserInfo(receiver)));

            List<ChannelContext> channelContexts = Im.getChannelByUserId(receiver);
            if (CollUtil.isNotEmpty(channelContexts)) {
                for (ChannelContext context : channelContexts) {
                    Im.addGroup(context, group);
                    Im.bindGroup(context, group);

                    // 发送群组会话
                    WsResponse response = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_SYSTEM_MESSAGE_RESP, joinGroupNotifyBody), Im.CHARSET);
                    Im.bSend(context, response);
                }
            }
        } else {
            group = groupService.getGroupInfo(userGroup.getRoomId());
            group.setIndex(System.currentTimeMillis());
            groupService.updateById(group);
        }

        return group;
    }

}
