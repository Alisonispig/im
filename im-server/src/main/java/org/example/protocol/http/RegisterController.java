package org.example.protocol.http;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.example.enums.CommandEnum;
import org.example.enums.DefaultEnum;
import org.example.packets.Status;
import org.example.packets.bean.Auth;
import org.example.packets.bean.User;
import org.example.packets.handler.system.RegisterReqBody;
import org.example.packets.handler.system.RespBody;
import org.example.protocol.http.service.UploadService;
import org.example.service.AuthService;
import org.example.service.UserService;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.http.server.annotation.RequestPath;
import org.tio.http.server.util.Resps;

@Slf4j
@RequestPath("/account")
public class RegisterController {

    private final AuthService authService;

    private final UserService userService;

    public RegisterController() {
        authService = new AuthService();
        userService = new UserService();
    }

    @RequestPath("/register")
    public HttpResponse register(HttpRequest httpRequest) {
        RegisterReqBody reqBody = JSONObject.parseObject(httpRequest.getBodyString(), RegisterReqBody.class);

        log.info(String.valueOf(reqBody));

        Auth authData = authService.getByAccount(reqBody.getAccount());
        if (authData != null) {
           return Resps.txt(httpRequest, RespBody.fail(CommandEnum.COMMAND_REGISTER_RESP, "该登录账户已存在"));
        }

        String url = UploadService.uploadDefault(DefaultEnum.ACCOUNT);
        log.info("未查询到用户信息，创建用户");
        User user = User.builder().id(IdUtil.getSnowflake().nextIdStr()).username(reqBody.getUsername()).status(Status.offline())
                .avatar(url).build();

        userService.saveOrUpdate(user);

        Auth auth = authService.createAccount(reqBody,user.getId());

        return Resps.txt(httpRequest, RespBody.success(CommandEnum.COMMAND_REGISTER_RESP));
    }

    @RequestPath(value = "/check")
    public HttpResponse checkAccount(String account, HttpRequest request) {
        Auth auth = authService.getByAccount(account);
        if (auth != null) {
           return Resps.txt(request, RespBody.fail(CommandEnum.COMMAND_REGISTER_RESP, "该登录账户已存在"));
        }
        return Resps.txt(request, RespBody.success(CommandEnum.COMMAND_REGISTER_RESP));
    }

}
