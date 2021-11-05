package org.example.commond.handler;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import org.example.commond.AbstractCmdHandler;
import org.example.config.Im;
import org.example.enums.CommandEnum;
import org.example.packets.User;
import org.example.packets.handler.ChatReqBody;
import org.example.packets.handler.ChatRespBody;
import org.example.packets.handler.MessageReqBody;
import org.example.packets.handler.RespBody;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MessageReqHandler extends AbstractCmdHandler {
    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_GET_MESSAGE_REQ;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext channelContext) {

        WsRequest request = (WsRequest) packet;
        MessageReqBody messageReqBody = JSON.parseObject(request.getWsBodyText(), MessageReqBody.class);
        List<String> messages = messageHelper.getHistoryMessage(messageReqBody.getRoomId());

        User user = Im.getUser(channelContext);

        List<ChatRespBody> collect = messages.stream().map(x -> {
            ChatReqBody chatReqBody = JSON.parseObject(x, ChatReqBody.class);
            ChatRespBody chatRespBody = BeanUtil.copyProperties(chatReqBody, ChatRespBody.class);
            User userInfo = messageHelper.getUserInfo(chatRespBody.getSenderId());
            chatRespBody.setAvatar(userInfo.getAvatar());
            chatRespBody.setUsername(userInfo.getUsername());
            chatRespBody.setCurrentUserId(user.getId());
            chatRespBody.setDeleted(false);
            chatRespBody.setSystem(false);
            Map<String, List<String>> reaction = messageHelper.getReaction(messageReqBody.getRoomId(), chatRespBody.get_id());
            chatRespBody.setReactions(reaction);
            return chatRespBody;
        }).collect(Collectors.toList());
        String success = RespBody.success(CommandEnum.COMMAND_GET_MESSAGE_RESP, collect);
        WsResponse response = WsResponse.fromText(success, Im.CHARSET);
        Im.send(channelContext, response);

        return null;
    }
}
