package org.example.commond.handler.room;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.example.commond.AbstractCmdHandler;
import org.example.commond.CommandManager;
import org.example.config.Im;
import org.example.enums.CommandEnum;
import org.example.packets.bean.Group;
import org.example.packets.bean.User;
import org.example.packets.handler.message.ChatReqBody;
import org.example.packets.handler.system.RespBody;
import org.example.packets.handler.room.GroupProfileReqBody;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

import java.util.stream.Collectors;

public class EditGroupProfileReqHandler extends AbstractCmdHandler {

    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_EDIT_GROUP_PROFILE_REQ;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext channelContext) {

        WsRequest request = (WsRequest) packet;

        GroupProfileReqBody body = JSON.parseObject(request.getBody(), GroupProfileReqBody.class);
        boolean changeName = false;
        boolean changeAvatar = false;
        Group groupInfo = groupService.getGroupInfo(body.getRoomId());
        if (!groupInfo.getRoomName().equals(body.getRoomName())) {
            changeName = true;
        }
        groupInfo.setRoomName(body.getRoomName());
        if (StrUtil.isNotBlank(body.getAvatar())) {
            groupInfo.setAvatar(body.getAvatar());
            changeAvatar = true;
        } else {
            body.setAvatar(groupInfo.getAvatar());
        }

        groupService.updateById(groupInfo);

        WsResponse response = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_EDIT_GROUP_PROFILE_RESP, body), Im.CHARSET);
        Im.sendToGroup(body.getRoomId(), response);

        // 发送修改信息
        User nowUser = Im.getUser(channelContext);
        AbstractCmdHandler command = CommandManager.getCommand(CommandEnum.COMMAND_CHAT_REQ);
        if (changeName) {
            ChatReqBody chatReqBody = ChatReqBody.buildSystem(body.getRoomId(), nowUser.getId(), nowUser.getUsername() + "修改了群名称");
            WsRequest wsRequest = WsRequest.fromText(JSON.toJSONString(chatReqBody, SerializerFeature.DisableCircularReferenceDetect), Im.CHARSET);
            command.handler(wsRequest, channelContext);
        }
        if (changeAvatar) {
            ChatReqBody chatReqBody = ChatReqBody.buildSystem(body.getRoomId(), nowUser.getId(), nowUser.getUsername() + "修改了群头像");
            WsRequest wsRequest = WsRequest.fromText(JSON.toJSONString(chatReqBody, SerializerFeature.DisableCircularReferenceDetect), Im.CHARSET);
            command.handler(wsRequest, channelContext);
        }

        return null;
    }
}
