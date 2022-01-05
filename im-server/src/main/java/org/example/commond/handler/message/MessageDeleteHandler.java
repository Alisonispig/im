package org.example.commond.handler.message;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import org.example.commond.AbstractCmdHandler;
import org.example.config.Im;
import org.example.enums.CommandEnum;
import org.example.packets.bean.Message;
import org.example.packets.bean.User;
import org.example.packets.handler.RespBody;
import org.example.packets.handler.message.MessageDeleteReqBody;
import org.example.packets.handler.message.MessageDeleteRespBody;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

public class MessageDeleteHandler extends AbstractCmdHandler {

    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_MESSAGE_DELETE_REQ;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext channelContext) {

        WsRequest request = (WsRequest) packet;

        MessageDeleteReqBody body = JSON.parseObject(request.getBody(), MessageDeleteReqBody.class);

        Message message = messageService.getMessage(body.getMessageId());
        message.setDeleted(true);
        messageService.update(message);

        MessageDeleteRespBody messageDeleteRespBody = BeanUtil.copyProperties(message, MessageDeleteRespBody.class);

        Message lastMessage = messageService.getLastMessage(message.getRoomId());
        if (lastMessage.getId().equals(message.getId())) {

            User userInfo = userService.getUserInfo(message.getSenderId());
            // 更新群组最后一条信息
            groupService.updateLastMessage(message);
            messageDeleteRespBody.setDeleteUserName(userInfo.getUsername());
            messageDeleteRespBody.setIsLastMessage(true);
        } else {
            messageDeleteRespBody.setIsLastMessage(false);
        }

        WsResponse response = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_MESSAGE_DELETE_RESP, messageDeleteRespBody), Im.CHARSET);
        Im.sendToGroup(message.getRoomId(), response);

        return null;
    }
}
