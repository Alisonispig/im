package org.example.commond.handler.message;

import com.alibaba.fastjson.JSON;
import org.example.commond.AbstractCmdHandler;
import org.example.enums.CommandEnum;
import org.example.packets.bean.Message;
import org.example.packets.handler.message.MessageFileHistoryReqBody;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

import java.util.List;

/**
 * 文件历史
 */

public class MessageFileHistoryHandler extends AbstractCmdHandler {

    @Override
    public CommandEnum command() {
        return null;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext channelContext) {

        WsRequest request = (WsRequest) packet;

        MessageFileHistoryReqBody body = JSON.parseObject(request.getWsBodyText(),MessageFileHistoryReqBody.class);

        Message message = messageService.getStartMessage(body.getRoomId());
//        if(message.get){
//
//        }

        List<Message> messages = messageService.getFileHistory(body.getRoomId(),body.getDate());


        return null;
    }

}
