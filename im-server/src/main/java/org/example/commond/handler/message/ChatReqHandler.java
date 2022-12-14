package org.example.commond.handler.message;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.URLUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.example.commond.AbstractCmdHandler;
import org.example.config.Chat;
import org.example.config.CourierConfig;
import org.example.config.Im;
import org.example.enums.CommandEnum;
import org.example.packets.bean.Message;
import org.example.packets.bean.User;
import org.example.packets.handler.message.ChatReqBody;
import org.example.packets.handler.message.ChatRespBody;
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
        log.info(httpPacket.getWsBodyText());
        User user = Im.getUser(channelContext);
        ChatReqBody request = JSONObject.parseObject(httpPacket.getBody(), ChatReqBody.class);
        Date date = new Date();
        request.setDate(DateUtil.formatDate(date));
        request.setTimestamp(DateUtil.formatTime(date));

        if (CollUtil.isNotEmpty(request.getFiles())) {
            request.getFiles().forEach(x -> {
                if (!x.getUrl().startsWith("https") && !x.getUrl().startsWith("http")) {
                    x.setUrl(CourierConfig.fileUrl + x.getUrl());
                }
            });
        }

        Message message = BeanUtil.copyProperties(request, Message.class);
        message.setId(ObjectUtil.defaultIfNull(request.get_id(), IdUtil.getSnowflake().nextIdStr()));
        message.setSystem(ObjectUtil.defaultIfNull(message.getSystem(), false));
        message.setSenderId(user.getId());
        message.setDeleted(false);
        message.setSaved(true);
        message.setDistributed(true);
        message.setSeen(false);
        message.setSendTime(System.currentTimeMillis());

        // ???????????????redis
        messageService.putGroupMessage(message);
        ChatRespBody response = BeanUtil.copyProperties(message, ChatRespBody.class);
        // ?????????????????????
        Chat.sendToGroup(response);

        // ??????????????????????????????
        groupService.updateLastMessage(message);

        return null;
    }
}
