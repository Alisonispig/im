package org.example.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import com.mongodb.client.model.Filters;
import org.example.dao.AuthRepository;
import org.example.packets.bean.Auth;
import org.example.packets.handler.system.RegisterReqBody;

import java.util.Date;

public class AuthService {

    private final AuthRepository authRepository;

    public AuthService() {
        authRepository = new AuthRepository();
    }

    public Auth getByUserId(String userId) {
        return authRepository.findOne(Filters.eq("userId",userId));
    }

    public Auth getByAccount(String account) {
       return authRepository.findOne(Filters.eq("account",account));
    }

    public Auth createAccount(RegisterReqBody reqBody, String userId) {
        Auth auth = new Auth();
        auth.setId(IdUtil.getSnowflake().nextIdStr());
        auth.setPassword(SecureUtil.md5(reqBody.getPassword()));
        auth.setAccount(reqBody.getAccount());
        auth.setRegisterDate(new Date());
        auth.setUserId(userId);
        authRepository.insert(auth);
        return auth;
    }

    public void update(Auth auth) {
        authRepository.updateById(auth);
    }
}
