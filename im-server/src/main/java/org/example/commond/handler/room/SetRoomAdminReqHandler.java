package org.example.commond.handler.room;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.example.commond.AbstractCmdHandler;
import org.example.commond.CommandManager;
import org.example.config.Chat;
import org.example.config.Im;
import org.example.enums.CommandEnum;
import org.example.enums.GroupAdminTypeEnum;
import org.example.enums.RoomRoleEnum;
import org.example.packets.bean.User;
import org.example.packets.bean.UserGroup;
import org.example.packets.handler.message.ChatReqBody;
import org.example.packets.handler.user.UserStatusBody;
import org.example.packets.handler.room.GroupAdminReqBody;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

public class SetRoomAdminReqHandler extends AbstractCmdHandler {

    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_SET_ROOM_ADMIN_REQ;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext channelContext) {

        WsRequest request = (WsRequest) packet;

        GroupAdminReqBody body = JSON.parseObject(request.getBody(), GroupAdminReqBody.class);

        User user = Im.getUser(channelContext);

        UserGroup userGroup = userGroupService.getUserGroup(body.getRoomId(), body.getUserId());
        if (userGroup == null) {
            return null;
        }

        userGroup.setRole(body.getType().equals(GroupAdminTypeEnum.SET) ? RoomRoleEnum.SUB_ADMIN : RoomRoleEnum.GENERAL);
        userGroupService.update(userGroup);

        User userInfo = userService.getUserInfo(body.getUserId());
        userInfo.setRole(userGroup.getRole());

        // 用户状态变化消息
        UserStatusBody userStatusBody = new UserStatusBody();
        userStatusBody.setGroup(groupService.getGroupInfo(body.getRoomId()));
        userStatusBody.setUser(userInfo);

        Chat.sendToGroup(userStatusBody, channelContext, true);

        // 移交群主消息
        AbstractCmdHandler command = CommandManager.getCommand(CommandEnum.COMMAND_CHAT_REQ);
        String content;
        if (body.getType().equals(GroupAdminTypeEnum.SET)) {
            // 发送退出群聊消息
            content = user.getUsername() + "已设置\"" + userService.getUserInfo(body.getUserId()).getUsername() + "\"为管理员";
        } else {
            content = user.getUsername() + "已解除\"" + userService.getUserInfo(body.getUserId()).getUsername() + "\"管理员身份";
        }

        ChatReqBody chatReqBody = ChatReqBody.buildSystem(body.getRoomId(), user.getId(), content);

        WsRequest wsRequest = WsRequest.fromText(JSON.toJSONString(chatReqBody, SerializerFeature.DisableCircularReferenceDetect), Im.CHARSET);

        command.handler(wsRequest, channelContext);
        return null;
    }

}
