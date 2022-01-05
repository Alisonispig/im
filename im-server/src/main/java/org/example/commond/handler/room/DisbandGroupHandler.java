package org.example.commond.handler.room;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.example.commond.AbstractCmdHandler;
import org.example.commond.CommandManager;
import org.example.config.Im;
import org.example.enums.CommandEnum;
import org.example.packets.bean.Group;
import org.example.packets.bean.User;
import org.example.packets.handler.RespBody;
import org.example.packets.handler.room.GroupUserReqBody;
import org.example.packets.handler.system.SystemMessageReqBody;
import org.example.packets.handler.system.SystemTextMessage;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

import java.util.List;
import java.util.stream.Collectors;

public class DisbandGroupHandler extends AbstractCmdHandler {

    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_DISBAND_GROUP_REQ;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext channelContext) {

        WsRequest request = (WsRequest) packet;

        GroupUserReqBody body = JSON.parseObject(request.getBody(), GroupUserReqBody.class);

        WsResponse response = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_DISBAND_GROUP_RESP, body), Im.CHARSET);

        Im.bSendToGroup(body.getRoomId(), response);


        Group groupInfo = groupService.getGroupInfo(body.getRoomId());

        groupService.delete(body.getRoomId());


        // 移除群组 (给当前群组所有的人员发送系统消息)
        List<User> groupUsers = userGroupService.getGroupUsers(body.getRoomId());
        List<String> userIds = groupUsers.stream().map(User::getId).collect(Collectors.toList());

        User user = Im.getUser(channelContext);

        SystemTextMessage systemMessageReqBody = SystemTextMessage.create("群聊" + groupInfo.getRoomName() + "已被群主" + user.getUsername() + "解散!", userIds);

        WsRequest wsRequest = WsRequest.fromText(JSON.toJSONString(systemMessageReqBody), Im.CHARSET);
        AbstractCmdHandler command = CommandManager.getCommand(CommandEnum.COMMAND_SYSTEM_MESSAGE_REQ);
        command.handler(wsRequest, channelContext);

        userGroupService.delete(body.getRoomId());

        Im.removeGroup(body.getRoomId());

        return null;
    }
}
