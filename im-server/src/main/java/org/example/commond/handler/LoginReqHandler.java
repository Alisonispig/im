package org.example.commond.handler;

import lombok.extern.slf4j.Slf4j;
import org.example.commond.AbstractCmdHandler;
import org.example.config.Im;
import org.example.config.ImConfig;
import org.example.enums.CommandEnum;
import org.example.packets.RespBody;
import org.example.packets.Status;
import org.example.packets.User;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.http.common.HttpRequest;
import org.tio.websocket.common.WsResponse;

@Slf4j
public class LoginReqHandler extends AbstractCmdHandler {

    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_LOGIN_REQ;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext channelContext) {
        HttpRequest httpRequest = (HttpRequest) packet;
        String username = httpRequest.getParam("username");
        String password = httpRequest.getParam("password");

        String success = RespBody.success(CommandEnum.COMMAND_LOGIN_RESP);
        Im.bSend(channelContext, WsResponse.fromText(success, ImConfig.CHARSET));

        User build = User.builder()._id(username).username(username).status(Status.online()).avatar("123").build();
        log.info("登录{},{}", username, password);
        Im.bindUser(channelContext, build);
        return null;
    }

}
