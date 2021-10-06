package org.example.listener;

import org.example.config.ImConfig;
import org.example.packets.Group;
import org.tio.core.ChannelContext;

public abstract class AbstractImGroupListener implements ImGroupListener {

    public abstract void doAfterBind(ChannelContext channelContext, Group group);

    public abstract void doAfterUnbind(ChannelContext channelContext, Group group);

    // TODO 消息持久化
    @Override
    public void onAfterBind(ChannelContext channelContext, Group group) {
        // 将绑定信息持久化到Redis
        ImConfig.get().messageHelper.onAfterGroupBind(channelContext, group);
        doAfterBind(channelContext, group);
    }

    // TODO 消息持久化
    @Override
    public void onAfterUnbind(ChannelContext channelContext, Group group) {
        doAfterUnbind(channelContext, group);
    }
}
