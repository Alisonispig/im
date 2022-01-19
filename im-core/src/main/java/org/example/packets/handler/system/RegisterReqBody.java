package org.example.packets.handler.system;

import lombok.Data;

@Data
public class RegisterReqBody {

    /**
     * 账号
     */
    private String account;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 重复密码
     */
    private String repeatPassword;

}
