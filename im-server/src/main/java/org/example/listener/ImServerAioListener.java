package org.example.listener;

import org.example.config.ImConfig;
import org.example.config.ImSessionContext;
import org.example.enums.KeyEnum;
import org.example.packets.ImClientNode;
import org.tio.core.ChannelContext;
import org.tio.core.Node;
import org.tio.core.intf.Packet;
import org.tio.websocket.server.WsServerAioListener;

public class ImServerAioListener extends WsServerAioListener {

    public static final ImServerAioListener me = new ImServerAioListener();

    @Override
    public boolean onHeartbeatTimeout(ChannelContext channelContext, Long interval, int heartbeatTimeoutCount) {
        return false;
    }

    @Override
    public void onAfterConnected(ChannelContext channelContext, boolean isConnected, boolean isReconnect) throws Exception {
        ImSessionContext sessionContext = new ImSessionContext();
        Node clientNode = channelContext.getClientNode();
        ImClientNode build = ImClientNode.builder().ip(clientNode.getIp()).port(clientNode.getPort()).build();
        build.setId(channelContext.getId());
        sessionContext.setImClientNode(build);
        channelContext.set(KeyEnum.IM_CHANNEL_SESSION_CONTEXT_KEY.getKey(), sessionContext);
        super.onAfterConnected(channelContext,isConnected,isReconnect);
    }

    @Override
    public void onAfterDecoded(ChannelContext channelContext, Packet packet, int packetSize) throws Exception {
        super.onAfterDecoded(channelContext, packet, packetSize);
    }

    @Override
    public void onAfterReceivedBytes(ChannelContext channelContext, int receivedBytes) throws Exception {
        super.onAfterReceivedBytes(channelContext, receivedBytes);
    }

    @Override
    public void onAfterSent(ChannelContext channelContext, Packet packet, boolean isSentSuccess) throws Exception {
        super.onAfterSent(channelContext, packet, isSentSuccess);
    }

    @Override
    public void onAfterHandled(ChannelContext channelContext, Packet packet, long cost) throws Exception {
        super.onAfterHandled(channelContext, packet, cost);
    }

    @Override
    public void onBeforeClose(ChannelContext channelContext, Throwable throwable, String remark, boolean isRemove) throws Exception {
        super.onBeforeClose(channelContext, throwable, remark, isRemove);
        // 更新用户状态
        ImConfig.get().messageHelper.userOffline(channelContext);
    }
}
