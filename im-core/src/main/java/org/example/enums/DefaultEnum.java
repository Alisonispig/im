package org.example.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DefaultEnum {

    /**
     * 默认用户头像
     */
    ACCOUNT("account-default", "account.png"),

    /**
     * 默认群组头像
     */
    ACCOUNT_GROUP("account-group-default", "accountGroup.png"),

    /**
     * 系统消息默认头像
     */
    LOGO("logo","logo.png")

    ;

    private final String key;

    private final String value;

}
