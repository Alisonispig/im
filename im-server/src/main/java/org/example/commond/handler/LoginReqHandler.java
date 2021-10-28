package org.example.commond.handler;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.commond.AbstractCmdHandler;
import org.example.config.Im;
import org.example.config.ImConfig;
import org.example.enums.CommandEnum;
import org.example.enums.DefaultEnum;
import org.example.packets.Group;
import org.example.packets.Status;
import org.example.packets.User;
import org.example.packets.handler.LoginRespBody;
import org.example.packets.handler.RespBody;
import org.example.packets.handler.UserStatusBody;
import org.example.protocol.http.service.UploadService;
import org.example.store.MessageHelper;
import org.example.util.TestUtil;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.http.common.HttpRequest;
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
        MessageHelper messageHelper = ImConfig.get().messageHelper;
        HttpRequest httpRequest = (HttpRequest) packet;
        String account = httpRequest.getParam("account");
        String password = httpRequest.getParam("password");

        if (StrUtil.isBlank(account)) {
            return null;
        }

        // 持久化获取用户信息
        User user = messageHelper.getByAccount(account);
        if (user == null) {
            String url = UploadService.uploadDefault(DefaultEnum.ACCOUNT);
            log.info("未查询到用户信息，模拟创建用户");
            user = User.builder()._id(IdUtil.getSnowflake().nextIdStr()).username(TestUtil.chineseName()).status(Status.online())
                    .avatar(url).build();
            Im.get().messageHelper.initAccount(account, user.get_id());
        }

        // 获取持久化用户群组信息
        List<Group> groups = messageHelper.getUserGroups(user.get_id());
        user.setStatus(Status.online());
        user.setGroups(groups);

        String success = RespBody.success(CommandEnum.COMMAND_LOGIN_RESP, new LoginRespBody(user.get_id()));
        Im.bSend(channelContext, WsResponse.fromText(success, ImConfig.CHARSET));


        log.info("登录{},{}", account, password);
        Im.bindUser(channelContext, user);

        UserStatusBody build = UserStatusBody.builder().user(Im.getUser(channelContext, false)).build();

        for (Group group : groups) {
            // 绑定群组
            Im.bindGroup(channelContext, group);
            // 给所在群组发送上线消息 用户状态更新
            List<User> groupUsers = messageHelper.getGroupUsers(group.getRoomId());
            group.setUsers(groupUsers);
            build.setGroup(group);
            Im.sendToGroup(build, channelContext);
        }
        return null;
    }

}
