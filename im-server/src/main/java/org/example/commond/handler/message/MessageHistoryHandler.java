package org.example.commond.handler.message;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import org.example.commond.AbstractCmdHandler;
import org.example.config.Im;
import org.example.enums.CommandEnum;
import org.example.packets.bean.Message;
import org.example.packets.bean.User;
import org.example.packets.handler.message.ChatRespBody;
import org.example.packets.handler.system.RespBody;
import org.example.packets.handler.message.MessageReqBody;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

import java.util.List;
import java.util.stream.Collectors;

public class MessageHistoryHandler extends AbstractCmdHandler {

    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_MESSAGE_HISTORY_REQ;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext channelContext) {

        WsRequest request = (WsRequest) packet;

        MessageReqBody messageReqBody = JSON.parseObject(request.getWsBodyText(), MessageReqBody.class);
        List<Message> messages = messageService.getHistoryMessage(messageReqBody.getRoomId(), messageReqBody.getPage() - 1, messageReqBody.getNumber(), -1);
        int count = messageService.getCount(messageReqBody.getRoomId());
        User user = Im.getUser(channelContext);

        List<ChatRespBody> collect = messages.stream().map(x -> {
            ChatRespBody chatRespBody = BeanUtil.copyProperties(x, ChatRespBody.class);
            User userInfo = userService.getUserInfo(chatRespBody.getSenderId());
            chatRespBody.setAvatar(userInfo.getAvatar());
            chatRespBody.setUsername(userInfo.getUsername());
            chatRespBody.setCurrentUserId(user.getId());
            return chatRespBody;
        }).collect(Collectors.toList());
        int page = count / messageReqBody.getNumber();

        String success = RespBody.successPage(CommandEnum.COMMAND_MESSAGE_HISTORY_RESP, collect, count % messageReqBody.getNumber() == 0 ? page : page + 1, count);
        WsResponse response = WsResponse.fromText(success, Im.CHARSET);
        Im.send(channelContext, response);

        return null;
    }
}
