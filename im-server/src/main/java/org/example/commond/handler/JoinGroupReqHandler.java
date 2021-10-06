package org.example.commond.handler;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.commond.AbstractCmdHandler;
import org.example.config.Im;
import org.example.enums.CommandEnum;
import org.example.packets.Group;
import org.example.packets.handler.JoinGroupNotifyBody;
import org.example.packets.User;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

@Slf4j
public class JoinGroupReqHandler extends AbstractCmdHandler {

    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_JOIN_GROUP_REQ;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext channelContext) {

        WsRequest request = (WsRequest) packet;
        Group group = JSON.parseObject(request.getWsBodyText(), Group.class);

        User user = Im.getUser(channelContext, false);
        // 发送用户绑定群组通知

        JoinGroupNotifyBody joinGroupNotifyBody = new JoinGroupNotifyBody();
        joinGroupNotifyBody.setRoomId(group.getRoomId());
        joinGroupNotifyBody.setUser(user);
        // TODO 加入群组是否成功
        joinGroupNotifyBody.setCode(0);
        log.info("加入群组消息：" + JSON.toJSONString(joinGroupNotifyBody));

        // 发送加入群组消息
        Im.sendToGroup(joinGroupNotifyBody, channelContext);

//        Im.bindGroup(channelContext, group);
        return null;
    }
}