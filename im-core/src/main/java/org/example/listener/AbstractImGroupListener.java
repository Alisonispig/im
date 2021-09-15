package org.example.listener;

import org.example.config.ImChannelContext;
import org.example.packets.Group;

public abstract class AbstractImGroupListener implements ImGroupListener {

    public abstract void doAfterBind(ImChannelContext imChannelContext, Group group);

    public abstract void doAfterUnbind(ImChannelContext imChannelContext, Group group);

    // TODO 消息持久化
    @Override
    public void onAfterBind(ImChannelContext imChannelContext, Group group) {
        doAfterBind(imChannelContext, group);
    }

    // TODO 消息持久化
    @Override
    public void onAfterUnbind(ImChannelContext imChannelContext, Group group) {
        doAfterUnbind(imChannelContext, group);
    }
}
