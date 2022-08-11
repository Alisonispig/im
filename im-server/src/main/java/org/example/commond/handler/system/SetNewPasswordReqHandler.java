package org.example.commond.handler.system;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import org.example.commond.AbstractCmdHandler;
import org.example.config.Im;
import org.example.enums.CommandEnum;
import org.example.packets.bean.Auth;
import org.example.packets.bean.User;
import org.example.packets.handler.system.RespBody;
import org.example.packets.handler.system.SetNewPasswordReqBody;
import org.example.packets.handler.user.EditProfileReqBody;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

public class SetNewPasswordReqHandler extends AbstractCmdHandler {
    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_SET_NEW_PASSWORD_REQ;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext channelContext) {
        WsRequest request = (WsRequest) packet;

        SetNewPasswordReqBody newPasswordReqBody = JSON.parseObject(request.getBody(), SetNewPasswordReqBody.class);

        if(!newPasswordReqBody.getPassword().equals(newPasswordReqBody.getRepeatPassword())){
            // 发送修改响应消息
            WsResponse response = WsResponse.fromText(RespBody.fail(CommandEnum.COMMAND_SET_NEW_PASSWORD_RESP, "密码与重复密码不相同"), Im.CHARSET);
            Im.send(channelContext, response);
            return null;
        }
        User user = Im.getUser(channelContext);
        Auth auth = authService.getByUserId(user.getId());
        if(!auth.getPassword().equals(SecureUtil.md5(newPasswordReqBody.getOldPassword()))){
            // 发送修改响应消息
            WsResponse response = WsResponse.fromText(RespBody.fail(CommandEnum.COMMAND_SET_NEW_PASSWORD_RESP, "旧密码不正确"), Im.CHARSET);
            Im.send(channelContext, response);
            return null;
        }
        auth.setPassword(SecureUtil.md5(newPasswordReqBody.getPassword()));
        authService.update(auth);

        // 发送修改响应消息
        WsResponse response = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_SET_NEW_PASSWORD_RESP), Im.CHARSET);
        Im.send(channelContext, response);
        return null;
    }
}
