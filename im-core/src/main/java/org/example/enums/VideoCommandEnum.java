package org.example.enums;

import lombok.Getter;

@Getter
public enum VideoCommandEnum {

    /**
     * 呼叫
     */
    CALL,

    /**
     * 拒绝
     */
    REFUSE,

    /**
     * 同意
     */
    AGREE,

    /**
     * 流已准备好
     */
    STREAM_OK,

    /**
     * 对方不在线
     */
    NOT_ONLINE,

    /**
     * 超时未响应
     */
    TIME_OUT,

    /**
     * 命令发送成功
     */
    REQ_SUCCESS,
}
