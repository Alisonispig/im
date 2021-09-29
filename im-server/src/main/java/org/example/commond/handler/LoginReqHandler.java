package org.example.commond.handler;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.commond.AbstractCmdHandler;
import org.example.commond.CommandManager;
import org.example.config.Im;
import org.example.config.ImConfig;
import org.example.enums.CommandEnum;
import org.example.packets.Group;
import org.example.packets.RespBody;
import org.example.packets.Status;
import org.example.packets.User;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.http.common.HttpRequest;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

import java.util.List;

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

        User user = User.builder()._id(username).username("吴迪").status(Status.online()).avatar("https://t1.huishahe.com/uploads/tu/202107/9999/7690765ea7.jpg").build();
        Group group = Group.builder().roomId("100").roomName("朋友圈").build();
        user.addGroup(group);
        log.info("登录{},{}", username, password);
        Im.bindUser(channelContext, user);

        List<Group> groups = user.getGroups();
        for (Group group1 : groups) {
            WsRequest hr = new WsRequest();
            hr.setWsBodyText(JSON.toJSONString(group1));
            // 绑定群组
            AbstractCmdHandler command = CommandManager.getCommand(CommandEnum.COMMAND_JOIN_GROUP_REQ);
            command.handler(hr, channelContext);
        }
        return null;
    }

}
