package org.example.commond.handler.message;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.example.commond.AbstractCmdHandler;
import org.example.commond.CommandManager;
import org.example.commond.handler.LoginReqHandler;
import org.example.commond.handler.room.CreatGroupReqHandler;
import org.example.config.Im;
import org.example.config.ImConfig;
import org.example.enums.CommandEnum;
import org.example.packets.bean.FriendInfo;
import org.example.packets.bean.Group;
import org.example.packets.bean.Message;
import org.example.packets.bean.User;
import org.example.packets.handler.message.MessageForwardReqBody;
import org.example.packets.handler.room.CreateGroupReqBody;
import org.example.packets.handler.system.RespBody;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsPacket;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MessageForwardReqHandler extends AbstractCmdHandler {

    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_FORWARD_MESSAGE_REQ;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext channelContext) {

        User self = Im.getUser(channelContext);
        WsPacket wsPacket = (WsPacket) packet;
        MessageForwardReqBody reqBody = JSON.parseObject(wsPacket.getBody(), MessageForwardReqBody.class);

        ChatReqHandler chatReqHandler = (ChatReqHandler) CommandManager.getCommand(CommandEnum.COMMAND_CHAT_REQ);

        List<Message> messages = reqBody.getMessages().stream().sorted(Comparator.comparing(Message::getSendTime)).map(x -> {
            Message message = new Message();
            message.setContent(x.getContent());
            message.setSenderId(self.getId());
            message.setFiles(x.getFiles());
            return message;
        }).collect(Collectors.toList());

        // 给这些chat发消息，不需要审核。
        for (String chat : reqBody.getChats()) {
            for (Message message : messages) {
                message.setId(IdUtil.getSnowflake().nextIdStr());
                message.setRoomId(chat);
                WsRequest wsRequest = WsRequest.fromText(JSONObject.toJSONString(message), ImConfig.CHARSET);
                chatReqHandler.handler(wsRequest, channelContext);
            }
        }

        CreatGroupReqHandler command = (CreatGroupReqHandler)CommandManager.getCommand(CommandEnum.COMMAND_CREATE_GROUP_REQ);
        for (String user : reqBody.getUsers()) {
            FriendInfo room = friendInfoService.getRoomInfo(self.getId(), user);
            if(room == null){
                CreateGroupReqBody createGroupReqBody = new CreateGroupReqBody();
                createGroupReqBody.setIsFriend(true);
                createGroupReqBody.setRoomName("好友会话");
                createGroupReqBody.setUsers(List.of(User.builder().id(user).build()));
                WsRequest wsRequest = WsRequest.fromText(JSONObject.toJSONString(createGroupReqBody), ImConfig.CHARSET);
                command.handler(wsRequest,channelContext);
                room = friendInfoService.getRoomInfo(self.getId(), user);
            }
            if(reqBody.getChats().contains(room.getRoomId())){
                continue;
            }
            for (Message message : reqBody.getMessages()) {
                message.setId(IdUtil.getSnowflake().nextIdStr());
                message.setRoomId(room.getRoomId());
                WsRequest wsRequest = WsRequest.fromText(JSONObject.toJSONString(message), ImConfig.CHARSET);
                chatReqHandler.handler(wsRequest, channelContext);
            }
        }

        WsResponse wsResponse = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_FORWARD_MESSAGE_RESP), Im.CHARSET);
        Im.send(channelContext,wsResponse);

        return null;
    }
}
