package org.example.config;

import org.example.listener.ImGroupListenerAdapter;
import org.example.listener.ImServerAioListener;
import org.example.listener.ImServerGroupListener;
import org.example.listener.ImServerUserListener;
import org.example.protocol.ws.WsMsgHandler;
import org.example.store.RedisMessageHelper;
import org.tio.server.ServerTioConfig;
import org.tio.websocket.server.WsServerStarter;

import java.io.IOException;

public class ImServerWebSocketStart {

    private WsServerStarter wsServerStarter;
    private ServerTioConfig serverTioConfig;

    public ImServerWebSocketStart(int port, WsMsgHandler wsMsgHandler) throws IOException {
        wsServerStarter = new WsServerStarter(port, wsMsgHandler);

        serverTioConfig = wsServerStarter.getServerTioConfig();
        serverTioConfig.setGroupListener(new ImGroupListenerAdapter(new ImServerGroupListener()));
        serverTioConfig.setName(ImServerConfig.PROTOCOL_NAME);
        serverTioConfig.setServerAioListener(ImServerAioListener.me);
        serverTioConfig.ipStats.addDurations(ImServerConfig.IpStatDuration.IPSTAT_DURATIONS);
        serverTioConfig.setHeartbeatTimeout(ImServerConfig.HEARTBEAT_TIMEOUT);
    }

    public static void start() throws Exception {

        ImServerConfig imServerConfig = new ImServerConfig();
        imServerConfig.setMessageHelper(new RedisMessageHelper());
        imServerConfig.setImUserListener(new ImServerUserListener());


        ImServerWebSocketStart appStarter = new ImServerWebSocketStart(ImServerConfig.SERVER_PORT, WsMsgHandler.me);
        appStarter.wsServerStarter.start();
    }
}
