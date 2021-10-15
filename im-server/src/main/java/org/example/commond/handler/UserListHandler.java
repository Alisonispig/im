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

public class UserListHandler extends AbstractCmdHandler {

    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_USER_LIST_REQ;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext channelContext) {

        List<User> userList = Im.get().messageHelper.getUserList();
        WsResponse response = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_USER_LIST_RESP, userList), Im.CHARSET);

        Im.send(channelContext,response);

        return null;
    }
}
