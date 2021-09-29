package org.example.commond.handler;

import com.alibaba.fastjson.JSONObject;
import org.example.commond.AbstractCmdHandler;
import org.example.enums.CommandEnum;
import org.example.packets.ChatReqBody;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

public class ChatReqHandler extends AbstractCmdHandler {

    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_CHAT_REQ;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext channelContext) {
        WsRequest httpPacket = (WsRequest) packet;
        System.out.println(httpPacket.getWsBodyText());
        ChatReqBody chatReqBody = JSONObject.parseObject(httpPacket.getWsBodyText(), ChatReqBody.class);
        return null;
    }
}
