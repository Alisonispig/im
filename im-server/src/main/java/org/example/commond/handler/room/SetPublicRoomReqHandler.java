package org.example.commond.handler.room;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.example.commond.AbstractCmdHandler;
import org.example.commond.CommandManager;
import org.example.config.Im;
import org.example.enums.CommandEnum;
import org.example.enums.GroupOutTypeEnum;
import org.example.packets.bean.Group;
import org.example.packets.bean.User;
import org.example.packets.handler.message.ChatReqBody;
import org.example.packets.handler.room.GroupAdminReqBody;
import org.example.packets.handler.room.SetPublicRoomReqBody;
import org.example.packets.handler.system.RespBody;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

public class SetPublicRoomReqHandler extends AbstractCmdHandler {

    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_SET_PUBLIC_ROOM_REQ;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext channelContext) {
        WsRequest request = (WsRequest) packet;

        SetPublicRoomReqBody body = JSON.parseObject(request.getBody(), SetPublicRoomReqBody.class);

        Group groupInfo = groupService.getGroupInfo(body.getRoomId());
        groupInfo.setPublicRoom(body.getPublicRoom());
        groupService.updateById(groupInfo);


        WsResponse wsResponse = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_SET_PUBLIC_ROOM_RESP), Im.CHARSET);
        Im.send(channelContext, wsResponse);
        return null;
    }

}
