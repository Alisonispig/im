package org.example.listener;

import org.example.packets.bean.User;
import org.tio.core.ChannelContext;

public class ImUserListenerAdapter implements ImUserListener {

    private ImUserListener imUserListener;

    public ImUserListenerAdapter(ImUserListener imUserListener) {
        this.imUserListener = imUserListener;
    }

    @Override
    public void onAfterBind(ChannelContext channelContext, User user) {
        imUserListener.onAfterBind(channelContext, user);
    }

    @Override
    public void onAfterUnbind(ChannelContext channelContext, User user) {
        imUserListener.onAfterUnbind(channelContext, user);
    }
}
