package org.example.listener;

import org.example.packets.bean.User;
import org.example.service.UserService;
import org.tio.core.ChannelContext;

public class ImServerUserListener extends AbstractImUserListener{

    private final UserService userService ;

    public ImServerUserListener() {
        userService = new UserService();
    }

    @Override
    public void doAfterBind(ChannelContext channelContext, User user) {
        userService.saveOrUpdate(user);
    }

    @Override
    public void doAfterUnbind(ChannelContext channelContext, User user) {

    }
}
