package org.example.config;

import org.example.listener.*;
import org.example.protocol.ws.WsMsgHandler;
import org.tio.server.TioServerConfig;
import org.tio.websocket.server.WsServerStarter;

import java.io.IOException;

public class ImServerWebSocketStart {

    private final WsServerStarter wsServerStarter;
    private final TioServerConfig serverTioConfig;


    public ImServerWebSocketStart(int port, WsMsgHandler wsMsgHandler) throws IOException {
        wsServerStarter = new WsServerStarter(port, wsMsgHandler);

        serverTioConfig = wsServerStarter.getTioServerConfig();
        serverTioConfig.setGroupListener(new ImGroupListenerAdapter(new ImServerGroupListener()));
        serverTioConfig.setName(ImConfig.PROTOCOL_NAME);
        serverTioConfig.setTioServerListener(ImTioServerListener.me);
        serverTioConfig.ipStats.addDurations(ImConfig.IpStatDuration.IPSTAT_DURATIONS);
        serverTioConfig.setHeartbeatTimeout(CourierConfig.socketHeartbeat);
    }

    public static void start() throws Exception {
        ImServerWebSocketStart appStarter = new ImServerWebSocketStart(CourierConfig.socketPort, WsMsgHandler.me);

        ImConfig imServerConfig = new ImConfig();
//        imServerConfig.setMessageHelper(new MongoMessageHelper());
        imServerConfig.setImUserListener(new ImUserListenerAdapter(new ImServerUserListener()));
        imServerConfig.setTioConfig(appStarter.serverTioConfig);

        appStarter.wsServerStarter.start();
    }
}
