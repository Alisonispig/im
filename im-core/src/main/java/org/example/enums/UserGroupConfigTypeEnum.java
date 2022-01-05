package org.example.enums;

import lombok.Getter;

/**
 * 用户对群组的配置类型枚举
 *
 * @author smart
 * @since 1.0.0
 */
@Getter
public enum UserGroupConfigTypeEnum {

    /**
     * 打开或关闭通知
     */
    NOTICE,

    /**
     * 群组备注 / 个人时为对方备注 / 群组时为对群组的备注
     */
    GROUP_REMARK,

}
