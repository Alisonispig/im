package org.example.commond.handler;

import org.example.commond.AbstractCmdHandler;
import org.example.config.ImChannelContext;
import org.example.enums.CommandEnum;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsResponse;

public class HeartbeatReqHandler extends AbstractCmdHandler {
    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_HANDSHAKE_REQ;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext imChannelContext) {
        return null;
    }

}
