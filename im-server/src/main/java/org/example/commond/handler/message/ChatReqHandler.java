package org.example.commond.handler.message;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.example.commond.AbstractCmdHandler;
import org.example.config.Chat;
import org.example.config.ImConfig;
import org.example.enums.CommandEnum;
import org.example.packets.bean.Message;
import org.example.packets.handler.ChatReqBody;
import org.example.packets.handler.ChatRespBody;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

import java.util.Date;

@Slf4j
public class ChatReqHandler extends AbstractCmdHandler {

    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_CHAT_REQ;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext channelContext) {
        WsRequest httpPacket = (WsRequest) packet;
        System.out.println(httpPacket.getWsBodyText());
        ChatReqBody request = JSONObject.parseObject(httpPacket.getWsBodyText(), ChatReqBody.class);
        Date date = new Date();
        request.setDate(DateUtil.formatDate(date));
        request.setTimestamp(DateUtil.formatTime(date));
        request.setId(IdUtil.getSnowflake().nextIdStr());
        if(CollUtil.isNotEmpty(request.getFiles())){
            request.getFiles().forEach(x -> x.setUrl(ImConfig.fileUrl + x.getUrl()));
        }

        Message message = BeanUtil.copyProperties(request, Message.class);
        message.setDeleted(false);
        // 消息缓存至redis
        messageService.putGroupMessage(message);
        ChatRespBody response = BeanUtil.copyProperties(request, ChatRespBody.class);

        // 发送给群组用户
        Chat.sendToGroup(response);

        // 更新群组最后一条信息
        groupService.updateLastMessage(message);

        return null;
    }
}
