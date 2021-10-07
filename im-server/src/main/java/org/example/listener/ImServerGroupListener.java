package org.example.listener;

import lombok.extern.slf4j.Slf4j;
import org.example.packets.Group;
import org.tio.core.ChannelContext;

@Slf4j
public class ImServerGroupListener extends AbstractImGroupListener {

    @Override
    public void doAfterBind(ChannelContext channelContext, Group group) {
        log.info("群组绑定监听");
    }

    @Override
    public void doAfterUnbind(ChannelContext channelContext, Group group) {
        log.info("群组绑监听");
    }

}
