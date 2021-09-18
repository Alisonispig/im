package org.example.commond.handler;

import org.example.commond.AbstractCmdHandler;
import org.example.config.Im;
import org.example.enums.CommandEnum;
import org.example.packets.ImPacket;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.http.common.HttpRequest;
import org.tio.websocket.common.WsResponse;

public class JoinGroupReqHandler extends AbstractCmdHandler {

    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_JOIN_GROUP_REQ;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext channelContext) {

        HttpRequest request = (HttpRequest) packet;
        Im.bindGroup(channelContext, request.);
        return null;
    }
}
