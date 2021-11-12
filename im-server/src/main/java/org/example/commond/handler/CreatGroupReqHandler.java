package org.example.commond.handler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.example.commond.AbstractCmdHandler;
import org.example.commond.CommandManager;
import org.example.config.Im;
import org.example.enums.CommandEnum;
import org.example.enums.DefaultEnum;
import org.example.enums.JoinGroupEnum;
import org.example.packets.bean.Group;
import org.example.packets.bean.User;
import org.example.packets.handler.CreateGroupReqBody;
import org.example.packets.handler.JoinGroupNotifyBody;
import org.example.protocol.http.service.UploadService;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

import java.util.ArrayList;

@Slf4j
public class CreatGroupReqHandler extends AbstractCmdHandler {

    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_CREATE_GROUP_REQ;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext channelContext) {

        log.info("创建群组");
        WsRequest httpPacket = (WsRequest) packet;
        System.out.println(httpPacket.getWsBodyText());
        CreateGroupReqBody request = JSONObject.parseObject(httpPacket.getWsBodyText(), CreateGroupReqBody.class);

        String roomName = request.getRoomName();
        // 当前用户
        User user = Im.getUser(channelContext, false);

        String url = UploadService.uploadDefault(DefaultEnum.ACCOUNT_GROUP);
        // 创建群聊
        Group build = Group.builder().roomId(IdUtil.getSnowflake().nextIdStr()).isFriend(request.getIsFriend()).index(System.currentTimeMillis()).roomName(roomName).users(new ArrayList<>())
                .build();
        request.getUsers().add(user);
        if (!request.getIsFriend()) {
            build.setAvatar(Im.fileUrl + request.getAvatar());
        }
        if (!request.getIsFriend() && StrUtil.isBlank(request.getAvatar())) {
            build.setAvatar(url);
        }
        for (User addUser : request.getUsers()) {
            User userInfo = userService.getUserInfo(addUser.getId());
            build.getUsers().add(userInfo);
            BeanUtil.copyProperties(userInfo, addUser);
        }

        // 如果是好友会话 1. 将群组的好友ID和备注字段放进各自的信息中
        if (request.getIsFriend()) {
            friendInfoService.createFriendTwoWay(build.getRoomId(), user.getId(), request.getUsers().get(0).getId());
        }

        groupService.saveOrUpdateById(build);
//        userGroupService.addGroupUser(build.getRoomId(), user.getId());

        JoinGroupNotifyBody joinGroupNotifyBody = JoinGroupNotifyBody.builder().group(build).users(request.getUsers())
                .code(JoinGroupEnum.STATE_CREATE.getValue()).build();

        // 发送加入群聊消息
        WsRequest wsRequest = WsRequest.fromText(JSON.toJSONString(joinGroupNotifyBody, SerializerFeature.DisableCircularReferenceDetect), Im.CHARSET);
        AbstractCmdHandler command = CommandManager.getCommand(CommandEnum.COMMAND_JOIN_GROUP_REQ);
        command.handler(wsRequest, channelContext);

//        Chat.resetGroup(build, user.getId());
//
//        // 发送群组创建成功消息
//        WsResponse response = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_CREATE_GROUP_RESP, build), Im.CHARSET);
//        Im.send(channelContext, response);

//        Im.addGroup(channelContext, joinGroupNotifyBody.getGroup());
//        Im.bindGroup(channelContext, joinGroupNotifyBody.getGroup());
//        // TODO 添加会话
//        messageHelper.addChat(user.getId(), build.getRoomId());
        return null;
    }
}
