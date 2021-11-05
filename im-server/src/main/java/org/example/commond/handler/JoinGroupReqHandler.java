package org.example.commond.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.example.commond.AbstractCmdHandler;
import org.example.config.Im;
import org.example.enums.CommandEnum;
import org.example.packets.Group;
import org.example.packets.User;
import org.example.packets.handler.JoinGroupNotifyBody;
import org.example.packets.handler.RespBody;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

import java.util.List;

@Slf4j
public class JoinGroupReqHandler extends AbstractCmdHandler {

    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_JOIN_GROUP_REQ;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext channelContext) {

        WsRequest request = (WsRequest) packet;
        String str = StrUtil.str(request.getBody(), Im.CHARSET);

        JoinGroupNotifyBody joinGroupNotifyBody = JSON.parseObject(str, JoinGroupNotifyBody.class);

        if (CollUtil.isEmpty(joinGroupNotifyBody.getUsers())) {
            return null;
        }

        Group group = messageHelper.getGroupInfo(joinGroupNotifyBody.getGroup().getRoomId());
        List<User> groupUsers = messageHelper.getGroupUsers(group.getRoomId());
        group.setUsers(groupUsers);

        // TODO 加入群组是否成功
        log.info("加入群组消息：" + JSON.toJSONString(joinGroupNotifyBody, SerializerFeature.DisableCircularReferenceDetect));

        // 绑定到群聊
        for (User addUser : joinGroupNotifyBody.getUsers()) {
            // 重写群组名称
            Im.resetGroup(group, addUser.getId(), null);
            List<ChannelContext> channelByUserId = Im.getChannelByUserId(addUser.getId());
            if (CollUtil.isNotEmpty(channelByUserId)) {
                for (ChannelContext context : channelByUserId) {
                    Im.addGroup(context, group);
                    Im.bindGroup(context, group);
                }
            }
            messageHelper.addGroupUser(addUser.getId(), group.getRoomId());
            messageHelper.initUserGroups(addUser.getId(), group.getRoomId());
            messageHelper.addChat(addUser.getId(), group.getRoomId());
        }

        joinGroupNotifyBody.setGroup(group);
        WsResponse wsResponse = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_JOIN_GROUP_RESP, joinGroupNotifyBody), Im.CHARSET);
        Im.send(channelContext, wsResponse);

        // 发送加入群组消息
        Im.sendToGroup(joinGroupNotifyBody);

        return null;
    }
}