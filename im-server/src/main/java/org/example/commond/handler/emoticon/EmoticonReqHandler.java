package org.example.commond.handler.emoticon;

import com.alibaba.fastjson.JSON;
import org.example.commond.AbstractCmdHandler;
import org.example.config.Im;
import org.example.enums.CommandEnum;
import org.example.packets.bean.Emoticon;
import org.example.packets.bean.User;
import org.example.packets.bean.UserEmoticon;
import org.example.packets.handler.emoticon.EmoticonSearchReqBody;
import org.example.packets.handler.system.RespBody;
import org.example.service.UserEmoticonService;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

import java.util.List;
import java.util.stream.Collectors;

public class EmoticonReqHandler extends AbstractCmdHandler {

    private final UserEmoticonService userEmoticonService;

    public EmoticonReqHandler () {
        userEmoticonService = new UserEmoticonService();
    }

    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_EMOTICON_REQ;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext channelContext) {
        WsRequest request = (WsRequest) packet;

        User user = Im.getUser(channelContext);

        EmoticonSearchReqBody emoticonSearchReqBody = JSON.parseObject(request.getBody(), EmoticonSearchReqBody.class);

        List<UserEmoticon> userEmoticons = userEmoticonService.getUserEmoticons(emoticonSearchReqBody.getId(),user.getId());

        List<Emoticon> emoticons = userEmoticons.stream().map(x -> emoticonService.getEmoticon(x.getEmoticonId())).collect(Collectors.toList());

        String success = RespBody.success(CommandEnum.COMMAND_EMOTICON_RESP, emoticons);
        WsResponse response = WsResponse.fromText(success, Im.CHARSET);
        Im.send(channelContext, response);
        return null;
    }
}
