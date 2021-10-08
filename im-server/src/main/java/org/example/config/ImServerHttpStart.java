package org.example.config;

import org.example.ImServer;
import org.tio.http.common.HttpConfig;
import org.tio.http.common.handler.HttpRequestHandler;
import org.tio.http.server.HttpServerStarter;
import org.tio.http.server.handler.DefaultHttpRequestHandler;
import org.tio.server.ServerTioConfig;

public class ImServerHttpStart {

    public static HttpConfig httpConfig;

    public static HttpRequestHandler requestHandler;

    public static HttpServerStarter httpServerStarter;

    public static ServerTioConfig serverTioConfig;

    public static void start() throws Exception{
        httpConfig = new HttpConfig(80, null, null, null);
        httpConfig.setPageRoot("classpath:page");
        httpConfig.setMaxLiveTimeOfStaticRes(2000);
        httpConfig.setPage404("404.html");
        httpConfig.setPage500("500.html");
        httpConfig.setUseSession(false);
        httpConfig.setCheckHost(false);

        requestHandler = new DefaultHttpRequestHandler(httpConfig, ImServer.class);//第二个参数也可以是数组

        httpServerStarter = new HttpServerStarter(httpConfig, requestHandler);
        serverTioConfig = httpServerStarter.getServerTioConfig();
        httpServerStarter.start(); //启动http服务器
    }
}
