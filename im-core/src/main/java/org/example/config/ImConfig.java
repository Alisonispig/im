package org.example.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.listener.ImGroupListener;
import org.example.listener.ImUserListener;
import org.example.store.MessageHelper;
import org.tio.utils.prop.MapWithLockPropSupport;
import org.tio.utils.time.Time;

import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Data
public class ImConfig extends MapWithLockPropSupport {

    public static final String CHARSET = "utf-8";

    /**
     * 消息处理器，Redis持久化， 处理在线离线消息
     */
    public MessageHelper messageHelper;

    /**
     * 用户绑定监听器
     */
    public ImUserListener imUserListener;

    ImConfig() {
        ImConfig.Global.set(this);
    }

    public static ImConfig get() {
        return ImConfig.Global.get();
    }

    /**
     * 协议名字(可以随便取，主要用于开发人员辨识)
     */
    public static final String PROTOCOL_NAME = "IM";

    /**
     * 监听的ip
     */
    public static final String SERVER_IP = null;//null表示监听所有，并不指定ip

    /**
     * 监听端口
     */
    public static final int SERVER_PORT = 9326;

    /**
     * 心跳超时时间，单位：毫秒
     */
    public static final int HEARTBEAT_TIMEOUT = 1000 * 60;

    /**
     * ip数据监控统计，时间段
     *
     * @author tanyaowu
     */
    public static interface IpStatDuration {
        public static final Long DURATION_1 = Time.MINUTE_1 * 5;
        public static final Long[] IPSTAT_DURATIONS = new Long[]{DURATION_1};
    }

    public static class Global {
        private static ImConfig imConfig;

        public static ImConfig get() {
            return imConfig;
        }

        public static void set(ImConfig imConfig) {
            Global.imConfig = imConfig;
        }
    }
}