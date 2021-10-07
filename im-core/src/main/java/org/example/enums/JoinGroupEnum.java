package org.example.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JoinGroupEnum {

    /**
     * 群组创建
     */
    STATE_CREATE(0),

    /**
     * 加入成功
     */
    STATE_JOIN_SUCCESS(1);

    private final Integer value;
}
