package org.example.listener;

import org.example.config.ImChannelContext;
import org.example.enums.KeyEnum;
import org.example.packets.Group;
import org.tio.core.ChannelContext;
import org.tio.core.intf.GroupListener;

/**
 * T-io 群组适配器 用来适配转发来自Tio的消息
 */
public class ImGroupListenerAdapter implements GroupListener {

    private ImGroupListener imGroupListener;

    public ImGroupListenerAdapter(ImGroupListener imGroupListener) {
        this.imGroupListener = imGroupListener;
    }

    @Override
    public void onAfterBind(ChannelContext channelContext, String group) {
        ImChannelContext imChannelContext = (ImChannelContext) channelContext.get(KeyEnum.IM_CHANNEL_CONTEXT_KEY.getKey());
        imGroupListener.onAfterBind(imChannelContext, Group.builder().roomId(group).build());
    }

    @Override
    public void onAfterUnbind(ChannelContext channelContext, String group) {
        ImChannelContext imChannelContext = (ImChannelContext) channelContext.get(KeyEnum.IM_CHANNEL_CONTEXT_KEY.getKey());
        imGroupListener.onAfterUnbind(imChannelContext, Group.builder().roomId(group).build());
    }

}
