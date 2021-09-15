package org.example.listener;

import org.example.config.ImChannelContext;
import org.example.packets.User;

public abstract class ImUserListenerAdapter implements ImUserListener {

    public abstract void doAfterBind(ImChannelContext imChannelContext, User user);

    public abstract void doAfterUnbind(ImChannelContext imChannelContext, User user);

    @Override
    public void onAfterBind(ImChannelContext imChannelContext, User user) {
        doAfterBind(imChannelContext, user);
    }

    @Override
    public void onAfterUnbind(ImChannelContext imChannelContext, User user) {
        doAfterUnbind(imChannelContext, user);
    }
}
