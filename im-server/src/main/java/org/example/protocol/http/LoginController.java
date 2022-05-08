package org.example.protocol.http;


import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.jwt.JWTUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.enums.CommandEnum;
import org.example.packets.bean.Auth;
import org.example.packets.handler.system.LoginReqBody;
import org.example.packets.handler.system.RespBody;
import org.example.service.AuthService;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.http.server.annotation.RequestPath;
import org.tio.http.server.util.Resps;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Slf4j
@RequestPath("/user")
public class LoginController {

    private final AuthService authService;

    public LoginController() {
        authService = new AuthService();
    }

    @RequestPath("/login")
    public HttpResponse login(HttpRequest httpRequest) {
        LoginReqBody loginReq = JSON.parseObject(httpRequest.getBodyString(), LoginReqBody.class);

        log.info(String.valueOf(loginReq));

        if (StrUtil.isBlank(loginReq.getAccount())) {
            return Resps.txt(httpRequest, RespBody.fail(CommandEnum.COMMAND_LOGIN_RESP, "用户名不能为空"));
        }

        if (StrUtil.isBlank(loginReq.getPassword())) {
            return Resps.txt(httpRequest, RespBody.fail(CommandEnum.COMMAND_LOGIN_RESP, "密码不能为空"));
        }

        Auth auth = authService.getByAccount(loginReq.getAccount());
        if (auth == null) {
            return Resps.txt(httpRequest, RespBody.fail(CommandEnum.COMMAND_LOGIN_RESP, "账号不存在"));
        }

        if (!auth.getPassword().equals(SecureUtil.md5(loginReq.getPassword()))) {
            return Resps.txt(httpRequest, RespBody.fail(CommandEnum.COMMAND_LOGIN_RESP, "密码错误"));
        }

        HashMap<String, Object> hashMap = new HashMap<>() {
            private static final long serialVersionUID = 1L;

            {
                put("uid", auth.getUserId());
                put("expire_time", System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 60);
            }
        };

        String token = JWTUtil.createToken(hashMap, "123456".getBytes(StandardCharsets.UTF_8));

        return Resps.txt(httpRequest, RespBody.success(CommandEnum.COMMAND_LOGIN_RESP, token));
    }
}
