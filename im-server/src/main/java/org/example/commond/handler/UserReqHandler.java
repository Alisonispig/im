package org.example.commond.handler;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.commond.AbstractCmdHandler;
import org.example.config.Im;
import org.example.config.ImConfig;
import org.example.enums.CommandEnum;
import org.example.packets.Group;
import org.example.packets.handler.RespBody;
import org.example.packets.User;
import org.example.packets.handler.UserReqBody;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

import java.util.List;

@Slf4j
public class UserReqHandler extends AbstractCmdHandler {

    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_GET_USER_REQ;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext channelContext) {
        WsRequest wsRequest = (WsRequest) packet;
        UserReqBody userReqBody = JSON.parseObject(wsRequest.getWsBodyText(), UserReqBody.class);
        log.info("userReqBody : {}", userReqBody);
        User user = Im.getUser(channelContext);

        for (Group group : user.getGroups()) {
            String roomId = group.getRoomId();
            List<User> groupUsers = ImConfig.get().messageHelper.getGroupUsers(roomId);
            group.setUsers(groupUsers);
        }

        return WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_GET_USER_RESP, user), ImConfig.CHARSET);
    }
}
