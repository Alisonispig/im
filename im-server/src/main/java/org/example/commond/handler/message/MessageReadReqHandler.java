package org.example.commond.handler.message;

import com.alibaba.fastjson.JSONObject;
import org.example.commond.AbstractCmdHandler;
import org.example.config.Im;
import org.example.enums.CommandEnum;
import org.example.packets.bean.Group;
import org.example.packets.bean.Message;
import org.example.packets.bean.UnReadMessage;
import org.example.packets.handler.message.MessageReadReqBody;
import org.example.packets.handler.RespBody;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.utils.hutool.CollUtil;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

import java.util.List;

public class MessageReadReqHandler extends AbstractCmdHandler {

    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_MESSAGE_READ_REQ;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext channelContext) {

        WsRequest request = (WsRequest) packet;

        // 清理当前房间未读消息
        MessageReadReqBody messageReadReqBody = JSONObject.parseObject(request.getWsBodyText(), MessageReadReqBody.class);
        List<UnReadMessage> unReadMessage = unReadMessageService.getUnReadMessage(Im.getUser(channelContext).getId(), messageReadReqBody.getRoomId());

        unReadMessageService.clearUnReadMessage(Im.getUser(channelContext).getId(), messageReadReqBody.getRoomId());

        Group groupInfo = groupService.getGroupInfo(messageReadReqBody.getRoomId());

        // 检查这个消息是不是全部已读
        for (UnReadMessage readMessage : unReadMessage) {
            List<UnReadMessage> messageUnReads = unReadMessageService.getMessageUnReads(readMessage.getMessageId());
            // 如果为空 全部已读
            if (CollUtil.isEmpty(messageUnReads)) {
                messageReadReqBody.setMessageId(readMessage.getMessageId());

                // 更新消息为已读状态
                messageService.read(readMessage.getMessageId());

                // 如果当前群组消息等于最后一条消息
                if (groupInfo.getLastMessage().getMessageId().equals(readMessage.getMessageId())) {
                    groupService.readLastMessage(groupInfo);
                }

                Message message = messageService.getMessage(readMessage.getMessageId());
                // 发送给消息发送者
                WsResponse response = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_MESSAGE_READ_RESP, messageReadReqBody), Im.CHARSET);
                List<ChannelContext> channelContexts = Im.getChannelByUserId(message.getSenderId());
                for (ChannelContext context : channelContexts) {
                    Im.send(context, response);
                }
            }
        }


        return null;
    }
}
