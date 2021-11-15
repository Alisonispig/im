package org.example.config;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.Setting;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.listener.ImUserListener;
import org.tio.core.TioConfig;
import org.tio.utils.prop.MapWithLockPropSupport;
import org.tio.utils.time.Time;

@EqualsAndHashCode(callSuper = true)
@Data
public class ImConfig extends MapWithLockPropSupport {

    public static final String CHARSET = "utf-8";

    public TioConfig tioConfig;

    /**
     * 消息处理器，Redis持久化， 处理在线离线消息
     */
//    public MessageHelper messageHelper;

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

    public static String minioUrl;

    /**
     * Http 端口 最大活跃时间
     */
    public static int httpPort, httpMaxLiveTime;

    public static boolean httpUseSession, httpCheckHost;

    public static int socketPort, socketHeartbeat;

    public static String fileUrl;

    public static String GLOBAL_PATH;

    private static final String GLOBAL_CONFIG_PATH = "global.config.path";

    static {
        Setting setting = new Setting("application.setting");
        String property = System.getProperty(GLOBAL_CONFIG_PATH);
        GLOBAL_PATH = StrUtil.isNotBlank(property) ? property : "";

        if (StrUtil.isNotBlank(property)) {
            fileUrl = setting.getByGroup("file.url", "prod");
            minioUrl = setting.getByGroup("minio.url", "prod");
        } else {
            fileUrl = setting.getByGroup("file.url", "dev");
            minioUrl = setting.getByGroup("minio.url", "dev");
        }
        httpPort = setting.getInt("http.port");
        httpMaxLiveTime = setting.getInt("http.max.live.time");
        httpUseSession = setting.getBool("http.use.session");
        httpCheckHost = setting.getBool("http.check.host");
        socketPort = setting.getInt("socket.port");
        socketHeartbeat = setting.getInt("socket.heartbeat");

    }

    /**
     * 监听的ip
     */
    public static final String SERVER_IP = null;//null表示监听所有，并不指定ip

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