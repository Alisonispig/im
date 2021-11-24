package org.example.commond.handler.room;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.example.commond.AbstractCmdHandler;
import org.example.commond.CommandManager;
import org.example.config.Chat;
import org.example.config.Im;
import org.example.enums.CommandEnum;
import org.example.enums.OutGroupTypeEnum;
import org.example.enums.RoomRoleEnum;
import org.example.packets.bean.User;
import org.example.packets.bean.UserGroup;
import org.example.packets.handler.ChatReqBody;
import org.example.packets.handler.RespBody;
import org.example.packets.handler.room.GroupUserReqBody;
import org.example.packets.handler.room.HandoverGroupRespBody;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

public class HandoverGroupHandler extends AbstractCmdHandler {
    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_HANDOVER_GROUP_REQ;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext channelContext) {

        WsRequest request = (WsRequest) packet;

        GroupUserReqBody body = JSON.parseObject(request.getBody(), GroupUserReqBody.class);

        User user = Im.getUser(channelContext);
        UserGroup userGroup = userGroupService.getUserGroup(body.getRoomId(), user.getId());
        if (userGroup == null) {
            return null;
        }
        if (!RoomRoleEnum.ADMIN.equals(userGroup.getRole())) {

            WsResponse response = WsResponse.fromText(RespBody.fail(CommandEnum.COMMAND_HANDOVER_GROUP_RESP, "你不是群主"), Im.CHARSET);
            Im.send(channelContext, response);
            return null;
        }
        UserGroup wait = userGroupService.getUserGroup(body.getRoomId(), body.getUserId());
        wait.setRole(RoomRoleEnum.ADMIN);
        userGroupService.update(wait);

        userGroup.setRole(RoomRoleEnum.GENERAL);
        userGroupService.update(userGroup);

        HandoverGroupRespBody handoverGroupRespBody = new HandoverGroupRespBody(body.getRoomId(), userGroup.getUserId(), wait.getUserId());
        WsResponse response = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_HANDOVER_GROUP_RESP, handoverGroupRespBody), Im.CHARSET);
        Im.sendToGroup(body.getRoomId(), response);


        // 移交群主消息
        AbstractCmdHandler command = CommandManager.getCommand(CommandEnum.COMMAND_CHAT_REQ);
        // 发送退出群聊消息
        String content = "群主已由" + "\"" + user.getUsername() + "\" 变更为" + "\"" + userService.getUserInfo(body.getUserId()).getUsername() + "\" ";
        ChatReqBody chatReqBody = ChatReqBody.buildSystem(body.getRoomId(), user.getId(), content);

        WsRequest wsRequest = WsRequest.fromText(JSON.toJSONString(chatReqBody, SerializerFeature.DisableCircularReferenceDetect), Im.CHARSET);

        command.handler(wsRequest, channelContext);

        return null;
    }
}
