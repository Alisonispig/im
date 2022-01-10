package org.example.config;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.Setting;
import lombok.Data;

import java.nio.charset.Charset;

public class CourierConfig {

    /**
     * 应用名称
     */
    public static String applicationName;

    /**
     * Socket端口
     */
    public static Integer socketPort;

    /**
     * 心跳间隔
     */
    public static Integer socketHeartbeat;

    /**
     * Http端口
     */
    public static Integer httpPort;

    /**
     * 最大保持
     */
    public static Integer httpMaxLiveTime;

    /**
     * 是否使用session
     */
    public static Boolean httpUseSession;

    /**
     * 检查Host
     */
    public static Boolean httpCheckHost;

    /**
     * 检查Md5
     */
    public static Boolean checkFileMd5;

    /**
     * 数据库端口
     */
    public static String mongoHost;

    /**
     * 文件地址
     */
    public static String fileUrl;

    /**
     * minio地址
     */
    public static String minioUrl;

    public static String env;

    static {
        Setting setting = new Setting("application.setting", Charset.defaultCharset(), true);
        String property = System.getProperty("ENV");
        env = StrUtil.isNotBlank(property) ? property : "dev";

        applicationName = setting.get("applicationName");

        socketPort = setting.getInt("socketPort");
        socketHeartbeat = setting.getInt("socketHeartbeat");

        httpPort = setting.getInt("httpPort");
        httpMaxLiveTime = setting.getInt("httpMaxLiveTime");
        httpUseSession = setting.getBool("httpUseSession");
        httpCheckHost = setting.getBool("httpCheckHost");

        checkFileMd5 = setting.getBool("checkFileMd5");

        mongoHost = setting.get(env, "mongoHost");
        fileUrl = setting.get(env, "fileUrl");
        minioUrl = setting.get(env, "minioUrl");
    }
}
