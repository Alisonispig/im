package org.example.listener;

import org.example.config.ImChannelContext;
import org.example.packets.User;

public interface ImUserListener {

    void onAfterBind(ImChannelContext imChannelContext, User user);

    void onAfterUnbind(ImChannelContext imChannelContext, User user);
}
