package org.example.config;

import org.example.enums.KeyEnum;
import org.tio.core.ChannelContext;
import org.tio.core.TioConfig;

import java.nio.channels.AsynchronousSocketChannel;

public abstract class ImChannelContext extends ChannelContext {

    public ImChannelContext(TioConfig tioConfig, AsynchronousSocketChannel asynchronousSocketChannel) {
        super(tioConfig, asynchronousSocketChannel);
    }

    public ImSessionContext getSessionContext(){
        return (ImSessionContext) this.getAttribute(KeyEnum.IM_CHANNEL_SESSION_CONTEXT_KEY.getKey());
    }
}
