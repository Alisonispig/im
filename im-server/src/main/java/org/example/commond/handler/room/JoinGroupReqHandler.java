package org.example.commond.handler.room;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.example.commond.AbstractCmdHandler;
import org.example.commond.CommandManager;
import org.example.config.Chat;
import org.example.config.Im;
import org.example.enums.CommandEnum;
import org.example.enums.RoomRoleEnum;
import org.example.packets.LastMessage;
import org.example.packets.bean.Group;
import org.example.packets.bean.User;
import org.example.packets.handler.message.ChatReqBody;
import org.example.packets.handler.room.JoinGroupNotifyBody;
import org.example.packets.handler.system.RespBody;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class JoinGroupReqHandler extends AbstractCmdHandler {

    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_JOIN_GROUP_REQ;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext channelContext) {

        WsRequest request = (WsRequest) packet;
        String str = StrUtil.str(request.getBody(), Im.CHARSET);

        // 转换为加入群组消息
        JoinGroupNotifyBody joinGroupNotifyBody = JSON.parseObject(str, JoinGroupNotifyBody.class);
        // 异常判断: 如果加入的用户未空, 则返回
        if (CollUtil.isEmpty(joinGroupNotifyBody.getUsers())) {
            return null;
        }

        // 获取完整的群组信息并返回
        Group group = groupService.getGroupInfo(joinGroupNotifyBody.getGroup().getRoomId());
        group.setNotice(true);
        joinGroupNotifyBody.setGroup(group);

        // 处理要加入的完整信息， 有可能来自创建群组， 群组创建时的创建者用户是完整的，不需要进行拷贝
        for (User user : joinGroupNotifyBody.getUsers()) {
            if (null == user.getRole()) {
                User userInfo = userService.getUserInfo(user.getId());
                userInfo.setRole(RoomRoleEnum.GENERAL);
                BeanUtil.copyProperties(userInfo, user);
            }
        }

        log.info("加入群组消息：" + JSON.toJSONString(joinGroupNotifyBody, SerializerFeature.DisableCircularReferenceDetect));

        // 绑定到群聊
        for (User addUser : joinGroupNotifyBody.getUsers()) {
            // 用户是否在线,在线直接绑定一个群组
            List<ChannelContext> channelByUserId = Im.getChannelByUserId(addUser.getId());
            if (CollUtil.isNotEmpty(channelByUserId)) {
                for (ChannelContext context : channelByUserId) {
                    Im.addGroup(context, group);
                    Im.bindGroup(context, group);
                }
            }
            // 持久化到数据库
            userGroupService.addGroupUser(group.getRoomId(), addUser.getId(), addUser.getRole());
        }

        // 发送申请加入群组响应
        WsResponse wsResponse = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_JOIN_GROUP_RESP, joinGroupNotifyBody), Im.CHARSET);
        Im.send(channelContext, wsResponse);

        List<String> joinUsers = joinGroupNotifyBody.getUsers().stream().map(User::getId).collect(Collectors.toList());

        List<User> groupUsers = userGroupService.getGroupUsers(joinGroupNotifyBody.getGroup().getRoomId(),joinUsers);

        joinGroupNotifyBody.setOtherUsers(groupUsers);

        // 发送加入群组消息
        Chat.sendToGroup(joinGroupNotifyBody);

        if(!group.getIsFriend()){
            // 发送加入群聊消息
            User nowUser = Im.getUser(channelContext);
            AbstractCmdHandler command = CommandManager.getCommand(CommandEnum.COMMAND_CHAT_REQ);

            String collect = joinGroupNotifyBody.getUsers().stream().map(User::getUsername).collect(Collectors.joining("、"));
            String msg = "";
            if(joinGroupNotifyBody.getUsers().size() == 1 && nowUser.getId().equals(joinGroupNotifyBody.getUsers().get(0).getId())) {
                msg = "\"" + collect + "\" 通过群组搜索加入群聊";
            } else {
                msg = "\"" + nowUser.getUsername()+  "\" 邀请 \"" + collect + "\" 加入群聊";
            }

            ChatReqBody chatReqBody = ChatReqBody.buildSystem(joinGroupNotifyBody.getGroup().getRoomId(), nowUser.getId(), msg);
            WsRequest wsRequest = WsRequest.fromText(JSON.toJSONString(chatReqBody, SerializerFeature.DisableCircularReferenceDetect), Im.CHARSET);
            command.handler(wsRequest, channelContext);

        }

        return null;
    }
}
