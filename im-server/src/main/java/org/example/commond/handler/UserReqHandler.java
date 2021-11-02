package org.example.commond.handler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.commond.AbstractCmdHandler;
import org.example.config.Im;
import org.example.config.ImConfig;
import org.example.enums.CommandEnum;
import org.example.packets.Group;
import org.example.packets.User;
import org.example.packets.handler.ChatReqBody;
import org.example.packets.handler.ChatRespBody;
import org.example.packets.handler.RespBody;
import org.example.packets.handler.UserReqBody;
import org.example.store.MessageHelper;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class UserReqHandler extends AbstractCmdHandler {

    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_GET_USER_REQ;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext channelContext) {

        MessageHelper messageHelper = Im.get().messageHelper;
        WsRequest wsRequest = (WsRequest) packet;
        UserReqBody userReqBody = JSON.parseObject(wsRequest.getWsBodyText(), UserReqBody.class);
        log.info("userReqBody : {}", userReqBody);
        User user = Im.getUser(channelContext);


        // 好友信息
        Map<String, String> userFriends = messageHelper.getUserFriends(user.get_id());

        List<Group> chats = new ArrayList<>();
        List<String> chatKeys = messageHelper.getUserChats(user.get_id());

        for (Group group : user.getGroups()) {
            // 组织群组用户信息
            String roomId = group.getRoomId();
            List<User> groupUsers = ImConfig.get().messageHelper.getGroupUsers(roomId);
            group.setUsers(groupUsers);

            Im.resetGroup(group, user.get_id(), userFriends);

            // 解析会话信息
            if (chatKeys.contains(roomId)) {
                chats.add(group);
            }
        }
        user.setChats(chats);

        WsResponse response = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_GET_USER_RESP, user), ImConfig.CHARSET);
        Im.send(channelContext, response);

        for (Group group : user.getGroups()) {
            // 获取到最后一条消息未读消息,并且发重新发送
            List<String> unReadMessage = messageHelper.getUnReadMessage(user.get_id(), group.getRoomId());
            if (CollUtil.isNotEmpty(unReadMessage)) {
                String messageId = unReadMessage.get(0);
                ChatReqBody chatReqBody = messageHelper.getGroupMessage(group.getRoomId(), messageId);
                ChatRespBody chatRespBody = BeanUtil.copyProperties(chatReqBody, ChatRespBody.class);
                User userInfo = messageHelper.getUserInfo(chatRespBody.getSenderId());
                chatRespBody.setAvatar(userInfo.getAvatar());
                chatRespBody.setUsername(userInfo.getUsername());
                chatRespBody.setDeleted(false);
                chatRespBody.setSystem(false);
                chatRespBody.setUnreadCount(CollUtil.isEmpty(unReadMessage) ? 0 : unReadMessage.size());
                WsResponse wsResponse = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_CHAT_REQ, chatRespBody), Im.CHARSET);
                Im.send(channelContext, wsResponse);
            }
        }

        return null;
    }
}
