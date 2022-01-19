package org.example.commond.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.commond.AbstractCmdHandler;
import org.example.config.Chat;
import org.example.config.Im;
import org.example.config.ImConfig;
import org.example.enums.CommandEnum;
import org.example.packets.Status;
import org.example.packets.bean.Auth;
import org.example.packets.bean.Group;
import org.example.packets.bean.User;
import org.example.packets.bean.UserGroup;
import org.example.packets.handler.system.LoginReqBody;
import org.example.packets.handler.system.LoginRespBody;
import org.example.packets.handler.system.RespBody;
import org.example.packets.handler.user.UserStatusBody;
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

        WsRequest httpRequest = (WsRequest) packet;
        LoginReqBody loginReq = JSON.parseObject(httpRequest.getBody(), LoginReqBody.class);

        if (StrUtil.isBlank(loginReq.getAccount())) {
            String success = RespBody.fail(CommandEnum.COMMAND_LOGIN_RESP, "用户名不能为空");
            Im.bSend(channelContext, WsResponse.fromText(success, ImConfig.CHARSET));
            return null;
        }

        Auth auth = authService.getByAccount(loginReq.getAccount());
        if(auth == null){
            String success = RespBody.fail(CommandEnum.COMMAND_LOGIN_RESP, "账号不存在");
            Im.bSend(channelContext, WsResponse.fromText(success, ImConfig.CHARSET));
            return null;
        }

        if(!auth.getPassword().equals(SecureUtil.md5(loginReq.getPassword()))){
            String success = RespBody.fail(CommandEnum.COMMAND_LOGIN_RESP, "密码错误");
            Im.bSend(channelContext, WsResponse.fromText(success, ImConfig.CHARSET));
            return null;
        }

        // 持久化获取用户信息
        User user = userService.getUserInfo(auth.getUserId());

        // 获取持久化用户群组信息
        List<Group> groups = userGroupService.getUserGroups(user.getId());
        user.setStatus(Status.online());
        user.setGroups(groups);

        String success = RespBody.success(CommandEnum.COMMAND_LOGIN_RESP, new LoginRespBody(user.getId()));
        Im.bSend(channelContext, WsResponse.fromText(success, ImConfig.CHARSET));

        log.info("登录{}", loginReq);
        Im.bindUser(channelContext, user);

        UserStatusBody build = UserStatusBody.builder().user(Im.getUser(channelContext, false)).build();

        for (Group group : groups) {
            UserGroup userGroup = userGroupService.getUserGroup(group.getRoomId(), user.getId());
            build.getUser().setRole(userGroup.getRole());
            // 绑定群组
            Im.bindGroup(channelContext, group);
            // 给所在群组发送上线消息 用户状态更新
            List<User> groupUsers = userGroupService.getGroupUsers(group.getRoomId());
            group.setUsers(groupUsers);
            build.setGroup(group);
            Chat.sendToGroup(build, channelContext);
        }
        auth.setLastLoginIp(channelContext.getClientNode().getIp());
        authService.update(auth);
        return null;
    }

}
