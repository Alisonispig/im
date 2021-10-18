package org.example.commond.handler;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.commond.AbstractCmdHandler;
import org.example.enums.CommandEnum;
import org.example.packets.handler.MessageReactionReqBody;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

@Slf4j
public class MessageReactionReqHandler extends AbstractCmdHandler {

    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_SEND_MESSAGE_REACTION_REQ;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext channelContext) {

        WsRequest request = (WsRequest) packet;

        MessageReactionReqBody messageReactionReqBody = JSON.parseObject(request.getWsBodyText(), MessageReactionReqBody.class);

        log.info("{}", messageReactionReqBody);


        return null;
    }
}
