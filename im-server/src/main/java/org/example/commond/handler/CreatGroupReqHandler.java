package org.example.commond.handler;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.example.commond.AbstractCmdHandler;
import org.example.commond.CommandManager;
import org.example.config.Im;
import org.example.enums.CommandEnum;
import org.example.enums.JoinGroupEnum;
import org.example.packets.FriendInfo;
import org.example.packets.Group;
import org.example.packets.User;
import org.example.packets.handler.CreateGroupReqBody;
import org.example.packets.handler.JoinGroupNotifyBody;
import org.example.packets.handler.RespBody;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

import java.util.List;

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
        // 创建群聊
        Group build = Group.builder().roomId(IdUtil.getSnowflake().nextIdStr()).index(System.currentTimeMillis()).roomName(roomName)
                .avatar("https://pic2.zhimg.com/v2-7e7cf5bcd064fbae2f0f4a23118eddb5_r.jpg?source=1940ef5c")
                .addUser(user).build();
        for (User addUser : request.getUsers()) {
            User userInfo = messageHelper.getUserInfo(addUser.get_id());
            build.getUsers().add(userInfo);
        }

        // 如果是好友会话 1. 将群组的好友ID和备注字段放进各自的信息中
        if (request.getIsFriend()) {
            messageHelper.putFriendInfo(user.get_id(), build.getRoomId(), new FriendInfo(request.getUsers().get(0).get_id(), ""));
            messageHelper.putFriendInfo(request.getUsers().get(0).get_id(), build.getRoomId(), new FriendInfo(user.get_id(), ""));
        }

        // 更新群组信息
        messageHelper.setGroupInfo(build);
        messageHelper.addGroupUser(user.get_id(),build.getRoomId());

        JoinGroupNotifyBody joinGroupNotifyBody = JoinGroupNotifyBody.builder().group(build).users(request.getUsers()).code(JoinGroupEnum.STATE_CREATE.getValue()).build();

        // 发送加入群聊消息
        WsRequest wsRequest = WsRequest.fromText(JSON.toJSONString(joinGroupNotifyBody, SerializerFeature.DisableCircularReferenceDetect), Im.CHARSET);
        AbstractCmdHandler command = CommandManager.getCommand(CommandEnum.COMMAND_JOIN_GROUP_REQ);
        command.handler(wsRequest, channelContext);

        Im.resetGroup(build, user.get_id(), null);

        // 发送群组创建成功消息
        WsResponse response = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_CREATE_GROUP_RESP, build), Im.CHARSET);
        Im.send(channelContext, response);

        Im.addGroup(channelContext, joinGroupNotifyBody.getGroup());
        Im.bindGroup(channelContext, joinGroupNotifyBody.getGroup());
        // 添加会话
        messageHelper.addChat(user.get_id(), build.getRoomId());
        return null;
    }
}
