package org.example.commond.handler;

import com.alibaba.fastjson.JSONObject;
import org.example.commond.AbstractCmdHandler;
import org.example.config.Im;
import org.example.enums.CommandEnum;
import org.example.packets.handler.MessageReadReqBody;
import org.example.packets.handler.RespBody;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

public class MessageReadReqHandler extends AbstractCmdHandler {
    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_MESSAGE_READ_REQ;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext channelContext) {

        WsRequest request = (WsRequest) packet;

        MessageReadReqBody messageReadReqBody = JSONObject.parseObject(request.getWsBodyText(), MessageReadReqBody.class);
        Im.get().messageHelper.clearUnReadMessage(channelContext,messageReadReqBody.getRoomId());

        WsResponse response = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_MESSAGE_READ_RESP, messageReadReqBody), Im.CHARSET);

        Im.send(channelContext,response);

        return null;
    }
}
