package org.example.commond.handler.room;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import org.example.commond.AbstractCmdHandler;
import org.example.config.Chat;
import org.example.config.Im;
import org.example.enums.CommandEnum;
import org.example.packets.bean.Group;
import org.example.packets.handler.RespBody;
import org.example.packets.handler.room.GroupUserReqBody;
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
        GroupUserReqBody groupUserReqBody = JSON.parseObject(request.getWsBodyText(), GroupUserReqBody.class);

        // 给剩下的人发送人员离开消息
        WsResponse response = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_REMOVE_GROUP_USER_RESP, groupUserReqBody), Im.CHARSET);
        Im.sendToGroup(groupUserReqBody.getRoomId(), response);

        Group group = groupService.getGroupInfo(groupUserReqBody.getRoomId());
        if (group.getIsFriend()) {

        }
        userGroupService.remove(groupUserReqBody.getRoomId(), groupUserReqBody.getUserId());

        // 将当前所有的连接端都解除掉
        List<ChannelContext> channelContexts = Im.getChannelByUserId(groupUserReqBody.getUserId());
        if (CollUtil.isNotEmpty(channelContexts)) {
            channelContexts.forEach(x -> {
                Im.unBindGroup(x, groupUserReqBody.getRoomId());
            });
        }

        return null;
    }
}
