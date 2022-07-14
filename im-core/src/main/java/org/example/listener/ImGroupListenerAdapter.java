package org.example.listener;

import org.example.packets.bean.Group;
import org.tio.core.ChannelContext;
import org.tio.core.intf.GroupListener;

/**
 * T-io 群组适配器 用来适配转发来自Tio的消息
 */
public class ImGroupListenerAdapter implements GroupListener {

    private final ImGroupListener imGroupListener;

    public ImGroupListenerAdapter(ImGroupListener imGroupListener) {
        this.imGroupListener = imGroupListener;
    }

    @Override
    public void onAfterBind(ChannelContext channelContext, String group) {
        imGroupListener.onAfterBind(channelContext, Group.builder().roomId(group).build());
    }

    @Override
    public void onAfterUnbind(ChannelContext channelContext, String group) {
        imGroupListener.onAfterUnbind(channelContext, Group.builder().roomId(group).build());
    }

}
