package org.example.packets;

import com.alibaba.fastjson.JSONObject;
import lombok.Builder;
import lombok.Data;

@Data
public class Message {

    /**
     * 消息创建时间
     * new Date().getTime()
     */
    protected Long createTime;
    /**
     * 消息id，全局唯一
     * UUIDSessionIdGenerator.instance.sessionId(null)
     */
    protected String id ;
    /**
     * 消息cmd命令码
     */
    protected Integer cmd ;
    /**
     * 扩展参数字段
     */
    protected JSONObject extras;
}
