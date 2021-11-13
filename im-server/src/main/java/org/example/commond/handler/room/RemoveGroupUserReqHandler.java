package org.example.commond.handler.room;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import org.example.commond.AbstractCmdHandler;
import org.example.config.Chat;
import org.example.config.Im;
import org.example.enums.CommandEnum;
import org.example.packets.bean.Group;
import org.example.packets.handler.room.RemoveGroupUserReqBody;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

import java.util.List;

public class RemoveGroupUserReqHandler extends AbstractCmdHandler {

    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_REMOVE_GROUP_USER_REQ;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext channelContext) {
        WsRequest request = (WsRequest) packet;
        RemoveGroupUserReqBody removeGroupUserReqBody = JSON.parseObject(request.getWsBodyText(), RemoveGroupUserReqBody.class);


        // 给剩下的人发送人员离开消息
        Chat.sendToGroup(removeGroupUserReqBody);

        Group group = groupService.getGroupInfo(removeGroupUserReqBody.getRoomId());
        if (group.getIsFriend()) {
        }
        userGroupService.remove(removeGroupUserReqBody.getRoomId(), removeGroupUserReqBody.getUserId());

        List<ChannelContext> channelContexts = Im.getChannelByUserId(removeGroupUserReqBody.getUserId());

        if (CollUtil.isNotEmpty(channelContexts)) {
            channelContexts.forEach(x -> {
                Im.unBindGroup(x, removeGroupUserReqBody.getRoomId());
            });
        }

        return null;
    }
}
