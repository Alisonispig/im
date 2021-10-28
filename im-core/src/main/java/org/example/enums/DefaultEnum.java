package org.example.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DefaultEnum {


    ACCOUNT("account-default", "account.png"),
    ACCOUNT_GROUP("account-group-default", "accountGroup.png");

    private final String key;

    private final String value;

}
