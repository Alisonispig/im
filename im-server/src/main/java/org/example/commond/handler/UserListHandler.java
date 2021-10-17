package org.example.commond.handler;

import org.example.commond.AbstractCmdHandler;
import org.example.config.Im;
import org.example.enums.CommandEnum;
import org.example.packets.User;
import org.example.packets.handler.RespBody;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsResponse;

import java.util.List;
import java.util.stream.Collectors;

public class UserListHandler extends AbstractCmdHandler {

    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_USER_LIST_REQ;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext channelContext) {

        User user = Im.getUser(channelContext);

        List<User> userList = Im.get().messageHelper.getUserList();
        List<User> collect = userList.stream().filter(x -> !x.get_id().equals(user.get_id())).collect(Collectors.toList());

        WsResponse response = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_USER_LIST_RESP, collect), Im.CHARSET);

        Im.send(channelContext,response);

        return null;
    }
}
