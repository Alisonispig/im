package org.example.commond.handler.message;

import org.example.commond.AbstractCmdHandler;
import org.example.enums.CommandEnum;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsResponse;

public class MessageHistoryHandler extends AbstractCmdHandler {
    
    @Override
    public CommandEnum command() {
        
        return null;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext channelContext) {
        return null;
    }
}
