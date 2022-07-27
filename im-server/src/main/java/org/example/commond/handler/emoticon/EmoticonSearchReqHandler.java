package org.example.commond.handler.emoticon;

import com.alibaba.fastjson.JSON;
import org.example.commond.AbstractCmdHandler;
import org.example.config.Im;
import org.example.enums.CommandEnum;
import org.example.packets.bean.Emoticon;
import org.example.packets.handler.emoticon.EmoticonSearchReqBody;
import org.example.packets.handler.system.RespBody;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

import java.util.List;


public class EmoticonSearchReqHandler extends AbstractCmdHandler {

    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_EMOTICON_SEARCH_REQ;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext channelContext) {
        WsRequest request = (WsRequest) packet;

        EmoticonSearchReqBody emoticonSearchReqBody = JSON.parseObject(request.getBody(), EmoticonSearchReqBody.class);

        List<Emoticon> emoticons = emoticonService.getEmoticons(emoticonSearchReqBody.getId(), emoticonSearchReqBody.getContent());

        String success = RespBody.success(CommandEnum.COMMAND_EMOTICON_SEARCH_RESP, emoticons);
        WsResponse response = WsResponse.fromText(success, Im.CHARSET);
        Im.send(channelContext, response);
        return null;
    }
}
