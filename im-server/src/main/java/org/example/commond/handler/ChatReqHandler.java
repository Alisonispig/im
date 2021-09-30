package org.example.commond.handler;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import org.example.commond.AbstractCmdHandler;
import org.example.config.Im;
import org.example.enums.CommandEnum;
import org.example.packets.ChatRepBody;
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
        ChatReqBody request = JSONObject.parseObject(httpPacket.getWsBodyText(), ChatReqBody.class);

        ChatRepBody response = new ChatRepBody();
        response.set_id(IdUtil.getSnowflake().nextId());
        response.setSenderId(request.getSenderId());
        response.setRoomId(request.getRoomId());

        Im.sendToGroup(response, channelContext);

        return null;
    }
}
