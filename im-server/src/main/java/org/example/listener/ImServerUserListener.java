package org.example.listener;

import org.example.packets.User;
import org.tio.core.ChannelContext;

public class ImServerUserListener extends AbstractImUserListener{


    @Override
    public void doAfterBind(ChannelContext channelContext, User user) {

    }

    @Override
    public void doAfterUnbind(ChannelContext channelContext, User user) {

    }
}
