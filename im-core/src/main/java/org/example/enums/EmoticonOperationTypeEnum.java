package org.example.enums;

public enum EmoticonOperationTypeEnum {

    /**
     * 添加表情包
     */
    INSERT_TO_USER,
    /**
     * 删除用户的表情包
     */
    DELETE,
    /**
     * 添加表情包到商店
     */
    INSERT_TO_STORE,
    /**
     * 添加到用户和商店
     */
    INSERT_TO_USER_AND_STORE,
    /**
     * 添加已存在的表情包到自己的库
     */
    INSERT_EMOTICON_TO_USER,
    /**
     * 移动表情包到最前面
     */
    MOVE_TOP

}
