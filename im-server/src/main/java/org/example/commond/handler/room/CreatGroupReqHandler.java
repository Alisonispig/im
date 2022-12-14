package org.example.commond.handler.room;

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
import org.example.enums.RoomRoleEnum;
import org.example.packets.bean.FriendInfo;
import org.example.packets.bean.Group;
import org.example.packets.bean.User;
import org.example.packets.handler.room.CreateGroupReqBody;
import org.example.packets.handler.room.JoinGroupNotifyBody;
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
        log.info(httpPacket.getWsBodyText());
        CreateGroupReqBody request = JSONObject.parseObject(httpPacket.getBody(), CreateGroupReqBody.class);

        String roomName = request.getRoomName();
        // 当前用户, 并设置用户为管理员
        User user = Im.getUser(channelContext, false);
        user.setRole(RoomRoleEnum.ADMIN);

        // 如果是好友的情况下校验是不是好友
        if(request.getIsFriend()) {
            FriendInfo roomInfo = friendInfoService.getRoomInfo(user.getId(), request.getUsers().get(0).getId());
            if(roomInfo != null){
                return null;
            }
        }

        String url = UploadService.uploadDefault(DefaultEnum.ACCOUNT_GROUP);
        // 创建群聊
        Group build = Group.builder().roomId(IdUtil.getSnowflake().nextIdStr())
                .isFriend(request.getIsFriend()).index(System.currentTimeMillis()).roomName(roomName)
                .publicRoom(request.getPublicRoom())
                .users(new ArrayList<>())
                .build();
        request.getUsers().add(user);
        if (!request.getIsFriend()) {
            build.setAvatar(request.getAvatar());
        }
        if (!request.getIsFriend() && StrUtil.isBlank(request.getAvatar())) {
            build.setAvatar(url);
        }
        for (User addUser : request.getUsers()) {
            User userInfo = userService.getUserInfo(addUser.getId());
            build.getUsers().add(userInfo);
            BeanUtil.copyProperties(userInfo, addUser,"role");
        }

        // 如果是好友会话 1. 将群组的好友ID和备注字段放进各自的信息中
        if (request.getIsFriend()) {
            friendInfoService.createFriendTwoWay(build.getRoomId(), user.getId(), request.getUsers().get(0).getId());
        }

        groupService.saveOrUpdateById(build);
        JoinGroupNotifyBody joinGroupNotifyBody = JoinGroupNotifyBody.builder().group(build).users(request.getUsers())
                .code(JoinGroupEnum.STATE_CREATE.getValue()).build();

        // 发送加入群聊消息
        WsRequest wsRequest = WsRequest.fromText(JSON.toJSONString(joinGroupNotifyBody, SerializerFeature.DisableCircularReferenceDetect), Im.CHARSET);
        AbstractCmdHandler command = CommandManager.getCommand(CommandEnum.COMMAND_JOIN_GROUP_REQ);
        command.handler(wsRequest, channelContext);

        return null;
    }
}
