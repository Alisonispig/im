package org.example.config;

import lombok.Data;
import org.example.listener.ImGroupListener;
import org.example.listener.ImUserListener;
import org.example.store.MessageHelper;
import org.tio.utils.prop.MapWithLockPropSupport;

@Data
public abstract class ImConfig extends MapWithLockPropSupport {

    public static final String CHARSET = "utf-8";

    /**
     * 消息处理器，Redis持久化， 处理在线离线消息
     */
    public MessageHelper messageHelper;

    /**
     * 群组绑定监听器
     */
    public ImGroupListener imGroupListener;

    /**
     * 用户绑定监听器
     */
    public ImUserListener imUserListener;
}
