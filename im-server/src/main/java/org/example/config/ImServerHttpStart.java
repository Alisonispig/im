package org.example.config;

import org.example.ImServer;
import org.tio.http.common.HttpConfig;
import org.tio.http.common.handler.HttpRequestHandler;
import org.tio.http.server.HttpServerStarter;
import org.tio.http.server.handler.DefaultHttpRequestHandler;
import org.tio.server.TioServerConfig;

public class ImServerHttpStart {

    public static HttpConfig httpConfig;

    public static HttpRequestHandler requestHandler;

    public static HttpServerStarter httpServerStarter;

    public static TioServerConfig serverTioConfig;

    public static void start() throws Exception {

        httpConfig = new HttpConfig(CourierConfig.httpPort, null, null, null);
        httpConfig.setPageRoot("classpath:page");
        httpConfig.setMaxLiveTimeOfStaticRes(CourierConfig.httpMaxLiveTime);
        httpConfig.setPage404("404.html");
        httpConfig.setPage500("500.html");
        httpConfig.setUseSession(CourierConfig.httpUseSession);
        httpConfig.setCheckHost(CourierConfig.httpCheckHost);

        requestHandler = new DefaultHttpRequestHandler(httpConfig, ImServer.class);//第二个参数也可以是数组

        httpServerStarter = new HttpServerStarter(httpConfig, requestHandler);
        serverTioConfig = httpServerStarter.getTioServerConfig();
        httpServerStarter.start(); //启动http服务器
    }
}
