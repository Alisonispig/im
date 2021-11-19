package org.example.commond.handler.room;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import org.example.commond.AbstractCmdHandler;
import org.example.config.Im;
import org.example.enums.CommandEnum;
import org.example.packets.bean.Group;
import org.example.packets.handler.RespBody;
import org.example.packets.handler.room.GroupProfileReqBody;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

public class EditGroupProfileReqHandler extends AbstractCmdHandler {

    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_EDIT_GROUP_PROFILE_REQ;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext channelContext) {

        WsRequest request = (WsRequest) packet;

        GroupProfileReqBody body = JSON.parseObject(request.getBody(), GroupProfileReqBody.class);

        Group groupInfo = groupService.getGroupInfo(body.getRoomId());
        groupInfo.setRoomName(body.getRoomName());
        if (StrUtil.isNotBlank(body.getAvatar())) {
            groupInfo.setAvatar(body.getAvatar());
        } else {
            body.setAvatar(groupInfo.getAvatar());
        }

        groupService.updateById(groupInfo);

        WsResponse response = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_EDIT_GROUP_PROFILE_RESP, body), Im.CHARSET);
        Im.sendToGroup(body.getRoomId(), response);
        return null;
    }
}
