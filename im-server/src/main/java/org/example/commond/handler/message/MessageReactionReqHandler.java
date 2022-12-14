package org.example.commond.handler.message;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.commond.AbstractCmdHandler;
import org.example.config.Im;
import org.example.enums.CommandEnum;
import org.example.packets.bean.User;
import org.example.packets.handler.system.RespBody;
import org.example.packets.handler.message.MessageReactionReqBody;
import org.example.packets.handler.message.MessageReactionRespBody;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

import java.util.List;
import java.util.Map;

@Slf4j
public class MessageReactionReqHandler extends AbstractCmdHandler {

    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_SEND_MESSAGE_REACTION_REQ;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext channelContext) {

        WsRequest request = (WsRequest) packet;

        MessageReactionReqBody messageReactionReqBody = JSON.parseObject(request.getBody(), MessageReactionReqBody.class);

        log.info("{}", messageReactionReqBody);

        User user = Im.getUser(channelContext);

        MessageReactionRespBody messageReactionRespBody = new MessageReactionRespBody();
        messageReactionRespBody.setRoomId(messageReactionReqBody.getRoomId());
        messageReactionRespBody.setMessageId(messageReactionReqBody.getMessageId());

        // 添加一个表情回复
        messageService.addReaction(messageReactionReqBody.getMessageId()
                , messageReactionReqBody.getReaction(), messageReactionReqBody.getRemove(), user.getId());

        Map<String, List<String>> reaction = messageService.getReaction(messageReactionReqBody.getMessageId());

        messageReactionRespBody.setReactions(reaction);

        WsResponse wsResponse = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_SEND_MESSAGE_REACTION_RESP, messageReactionRespBody), Im.CHARSET);
        Im.sendToGroup(messageReactionRespBody.getRoomId(), wsResponse);

        return null;
    }
}
