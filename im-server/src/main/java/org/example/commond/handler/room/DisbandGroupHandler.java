package org.example.commond.handler.room;

import com.alibaba.fastjson.JSON;
import org.example.commond.AbstractCmdHandler;
import org.example.config.Im;
import org.example.enums.CommandEnum;
import org.example.packets.handler.RespBody;
import org.example.packets.handler.room.GroupUserReqBody;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

public class DisbandGroupHandler extends AbstractCmdHandler {

    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_DISBAND_GROUP_REQ;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext channelContext) {

        WsRequest request = (WsRequest) packet;

        GroupUserReqBody body = JSON.parseObject(request.getBody(), GroupUserReqBody.class);

        WsResponse response = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_DISBAND_GROUP_RESP, body), Im.CHARSET);

        Im.bSendToGroup(body.getRoomId(), response);

        groupService.delete(body.getRoomId());

        userGroupService.delete(body.getRoomId());

        Im.removeGroup(body.getRoomId());

        // 移除群组

        return null;
    }
}
