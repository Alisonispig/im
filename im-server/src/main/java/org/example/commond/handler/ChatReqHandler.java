package org.example.commond.handler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.example.commond.AbstractCmdHandler;
import org.example.config.Im;
import org.example.enums.CommandEnum;
import org.example.packets.handler.ChatRespBody;
import org.example.packets.handler.ChatReqBody;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

@Slf4j
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

        ChatRespBody response = BeanUtil.copyProperties(request, ChatRespBody.class);
        response.set_id(IdUtil.getSnowflake().nextId());


        Im.sendToGroup(response, channelContext);

        return null;
    }
}
