package org.example.commond.handler.room;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.example.commond.AbstractCmdHandler;
import org.example.commond.CommandManager;
import org.example.config.Im;
import org.example.enums.CommandEnum;
import org.example.enums.GroupOutTypeEnum;
import org.example.packets.bean.Group;
import org.example.packets.bean.User;
import org.example.packets.handler.message.ChatReqBody;
import org.example.packets.handler.system.RespBody;
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
        GroupUserReqBody body = JSON.parseObject(request.getWsBodyText(), GroupUserReqBody.class);

        // 给剩下的人发送人员离开消息
        WsResponse response = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_REMOVE_GROUP_USER_RESP, body), Im.CHARSET);
        Im.sendToGroup(body.getRoomId(), response);

        Group group = groupService.getGroupInfo(body.getRoomId());
        if (group.getIsFriend()) {

        }
        userGroupService.remove(body.getRoomId(), body.getUserId());

        // 将当前所有的连接端都解除掉
        List<ChannelContext> channelContexts = Im.getChannelByUserId(body.getUserId());
        if (CollUtil.isNotEmpty(channelContexts)) {
            channelContexts.forEach(x -> {
                Im.unBindGroup(x, body.getRoomId());
            });
        }

        User user = Im.getUser(channelContext);
        AbstractCmdHandler command = CommandManager.getCommand(CommandEnum.COMMAND_CHAT_REQ);
        // 发送退出群聊消息
        String content = "\"" + userService.getUserInfo(body.getUserId()).getUsername() + "\" " + (body.getType().equals(GroupOutTypeEnum.OUT) ? "已退出群聊" : "已被移出群聊");
        ChatReqBody chatReqBody = ChatReqBody.buildSystem(body.getRoomId(), user.getId(), content);
        WsRequest wsRequest = WsRequest.fromText(JSON.toJSONString(chatReqBody, SerializerFeature.DisableCircularReferenceDetect), Im.CHARSET);

        command.handler(wsRequest, channelContext);

        return null;
    }
}
