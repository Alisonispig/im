package org.example.commond.handler;

import org.example.commond.AbstractCmdHandler;
import org.example.config.ImConfig;
import org.example.enums.CommandEnum;
import org.example.packets.handler.system.HeartbeatBody;
import org.example.packets.handler.system.RespBody;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsResponse;

public class HeartbeatReqHandler extends AbstractCmdHandler {
    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_HEARTBEAT_REQ;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext imChannelContext) {
        return WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_HEARTBEAT_REQ, new HeartbeatBody((byte) -128)), ImConfig.CHARSET);
    }

}
