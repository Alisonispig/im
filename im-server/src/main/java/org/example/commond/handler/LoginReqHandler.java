package org.example.commond.handler;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.commond.AbstractCmdHandler;
import org.example.commond.CommandManager;
import org.example.config.Im;
import org.example.config.ImConfig;
import org.example.enums.CommandEnum;
import org.example.packets.Group;
import org.example.packets.handler.RespBody;
import org.example.packets.Status;
import org.example.packets.User;
import org.example.packets.handler.UserStatusBody;
import org.example.util.TestUtil;
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

        // 持久化获取用户信息
        User user = ImConfig.get().messageHelper.getUserInfo(username);
        if (user == null) {
            log.info("未查询到用户信息，模拟创建用户");
            user = User.builder()._id(username).username(TestUtil.chineseName()).status(Status.online()).avatar("https://t1.huishahe.com/uploads/tu/202107/9999/7690765ea7.jpg").build();
        }

        // 获取持久化用户群组信息
        List<Group> groups = ImConfig.get().messageHelper.getUserGroups(username);
        Group groupNew = null;
        if (CollUtil.isEmpty(groups)) {
            log.info("未查询到群组信息，模拟创建群组");
            groupNew = Group.builder().roomId("100").roomName("朋友圈").build();
            groups.add(groupNew);
        }

        user.setStatus(Status.online());
        user.setGroups(groups);

        log.info("登录{},{}", username, password);
        Im.bindUser(channelContext, user);

        if (groupNew != null) {
            // 发送加入群组消息
            WsRequest hr = new WsRequest();
            hr.setWsBodyText(JSON.toJSONString(groupNew));
            // 绑定群组 用于接收即时消息
            AbstractCmdHandler command = CommandManager.getCommand(CommandEnum.COMMAND_JOIN_GROUP_REQ);
            command.handler(hr, channelContext);
        }

        UserStatusBody build = UserStatusBody.builder().user(Im.getUser(channelContext,false)).build();

        for (Group group : groups) {
            // 绑定群组
            Im.bindGroup(channelContext, group);
            // 给所在群组发送上线消息 用户状态更新

            build.setGroup(group);
            Im.sendToGroup(build, channelContext);
        }
        return null;
    }

}
