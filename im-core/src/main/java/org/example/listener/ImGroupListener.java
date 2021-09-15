package org.example.listener;

import org.example.config.ImChannelContext;
import org.example.packets.Group;

public interface ImGroupListener {

    void onAfterBind(ImChannelContext imChannelContext, Group build);

    void onAfterUnbind(ImChannelContext imChannelContext, Group build);
}
