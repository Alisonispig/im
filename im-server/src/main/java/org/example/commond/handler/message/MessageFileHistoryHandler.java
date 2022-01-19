package org.example.commond.handler.message;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import org.example.commond.AbstractCmdHandler;
import org.example.config.Im;
import org.example.enums.CommandEnum;
import org.example.packets.bean.Message;
import org.example.packets.handler.message.FileMessageBody;
import org.example.packets.handler.system.RespBody;
import org.example.packets.handler.message.MessageFileHistoryReqBody;
import org.example.packets.handler.message.MessageFileHistoryRespBody;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件历史
 */

public class MessageFileHistoryHandler extends AbstractCmdHandler {

    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_MESSAGE_FILE_HISTORY_REQ;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext channelContext) {

        WsRequest request = (WsRequest) packet;

        MessageFileHistoryReqBody body = JSON.parseObject(request.getWsBodyText(), MessageFileHistoryReqBody.class);

        MessageFileHistoryRespBody respBody = new MessageFileHistoryRespBody();

        Message message = messageService.getStartMessage(body.getRoomId());

        respBody.setHasNext(DateUtil.parse(message.getDate()).isBefore(DateUtil.parse(body.getDate())));

        List<Message> messages = messageService.getFileHistory(body.getRoomId(), body.getDate());
        List<FileMessageBody> fileMessageBodies = new ArrayList<>();
        messages.forEach(x -> fileMessageBodies.addAll(x.getFiles()));
        respBody.setFiles(fileMessageBodies);

        WsResponse response = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_MESSAGE_FILE_HISTORY_RESP, respBody), Im.CHARSET);
        Im.send(channelContext, response);

        return null;
    }

}
