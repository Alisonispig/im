package org.example.packets.bean;

import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonId;

import java.util.Date;

@Data
public class Auth {

    @BsonId
    private String id;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 登录名
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 注册时间
     */
    private Date registerDate;

    /**
     * 最后登录IP
     */
    private String lastLoginIp;

    /**
     * 问题
     */
    private String question;

    /**
     * 答案
     */
    private String answer;
}
