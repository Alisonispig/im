package org.example.commond.handler;

import cn.hutool.core.util.ByteUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
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

import java.util.Arrays;
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
        String token = new String(httpRequest.getBody());
//        String token = StrUtil.toString(httpRequest.getBody());
        System.out.println(token);
//        String token = (String) JSON.parseObject(Arrays.toString(httpRequest.getBody())).get("token");

        JWT jwt = JWTUtil.parseToken(token);
        String uid = (String) jwt.getPayload("uid");

        Auth auth = authService.getByUserId(uid);

        // 持久化获取用户信息
        User user = userService.getUserInfo(uid);

        // 获取持久化用户群组信息
        List<Group> groups = userGroupService.getUserGroups(user.getId());
        user.setStatus(Status.online());
        user.setGroups(groups);

//        String success = RespBody.success(CommandEnum.COMMAND_LOGIN_RESP, new LoginRespBody(user.getId()));
//        Im.bSend(channelContext, WsResponse.fromText(success, ImConfig.CHARSET));

        log.info("登录{}", uid);
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
