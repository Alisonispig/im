package org.example.commond.handler.message;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import org.example.commond.AbstractCmdHandler;
import org.example.config.Im;
import org.example.enums.CommandEnum;
import org.example.packets.bean.Message;
import org.example.packets.bean.User;
import org.example.packets.handler.ChatRespBody;
import org.example.packets.handler.RespBody;
import org.example.packets.handler.message.MessageReqBody;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

import java.util.List;
import java.util.stream.Collectors;

public class MessageSearchReqHandler extends AbstractCmdHandler {

    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_SEARCH_MESSAGE_REQ;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext channelContext) {

        WsRequest request = (WsRequest) packet;

        MessageReqBody messageReqBody = JSON.parseObject(request.getWsBodyText(), MessageReqBody.class);

        List<Message> messages = messageService.getMessage(messageReqBody.getRoomId(),messageReqBody.getContent());

        User user = Im.getUser(channelContext);

        List<ChatRespBody> collect = messages.stream().map(x -> {
            ChatRespBody chatRespBody = BeanUtil.copyProperties(x, ChatRespBody.class);
            User userInfo = userService.getUserInfo(chatRespBody.getSenderId());
            chatRespBody.setAvatar(userInfo.getAvatar());
            chatRespBody.setUsername(userInfo.getUsername());
            chatRespBody.setCurrentUserId(user.getId());
            return chatRespBody;
        }).collect(Collectors.toList());

        String success = RespBody.success(CommandEnum.COMMAND_SEARCH_MESSAGE_RESP, collect);
        WsResponse response = WsResponse.fromText(success, Im.CHARSET);
        Im.send(channelContext, response);

        return null;
    }
}
