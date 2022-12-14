package org.example.listener;

import org.example.packets.bean.Group;
import org.tio.core.ChannelContext;

public interface ImGroupListener {

    void onAfterBind(ChannelContext channelContext, Group build);

    void onAfterUnbind(ChannelContext channelContext, Group build);
}
