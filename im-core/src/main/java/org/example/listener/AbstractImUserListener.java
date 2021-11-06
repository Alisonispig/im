package org.example.listener;

import org.example.packets.bean.User;
import org.tio.core.ChannelContext;

public abstract class AbstractImUserListener implements ImUserListener {

    public abstract void doAfterBind(ChannelContext channelContext, User user);

    public abstract void doAfterUnbind(ChannelContext channelContext, User user);

    @Override
    public void onAfterBind(ChannelContext channelContext, User user) {
        doAfterBind(channelContext, user);
    }

    @Override
    public void onAfterUnbind(ChannelContext channelContext, User user) {
        doAfterUnbind(channelContext, user);
    }
}
