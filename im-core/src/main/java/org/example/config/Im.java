package org.example.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.example.enums.KeyEnum;
import org.example.listener.ImUserListener;
import org.example.packets.User;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.core.intf.Packet;
import org.tio.utils.lock.SetWithLock;

import java.util.Objects;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
public class Im {

    public static boolean bSend(ChannelContext channelContext, Packet packet){
        if(channelContext == null || packet == null){
            return false;
        }
        return Tio.bSend(channelContext,packet);
    }

    public static boolean send(ChannelContext channelContext, Packet packet){
        if(channelContext == null || packet == null){
            return false;
        }
        return Tio.send(channelContext,packet);
    }

    /**
     * 绑定用户(如果配置了回调函数执行回调)
     * @param channelContext IM通道上下文
     * @param user 绑定用户信息
     */
    public static boolean bindUser(ChannelContext channelContext, User user){
        if(Objects.isNull(user)|| StrUtil.isBlank(user.get_id())){
            log.error("user or userId is null");
            return false;
        }
        String userId = user.get_id();
        ImSessionContext imSessionContext = (ImSessionContext)channelContext.get(KeyEnum.IM_CHANNEL_SESSION_CONTEXT_KEY.getKey());
        Tio.bindUser(channelContext, userId);
        SetWithLock<ChannelContext> channelContextSetWithLock = Tio.getByUserid(channelContext.tioConfig, userId);
        ReentrantReadWriteLock.ReadLock lock = channelContextSetWithLock.getLock().readLock();
        try {
            lock.lock();
            if(CollUtil.isEmpty(channelContextSetWithLock.getObj())){
                return false;
            }
            imSessionContext.getImClientNode().setUser(user);
//            ImUserListener imUserListener = imConfig.getImUserListener();
//            if(Objects.nonNull(imUserListener)){
//                imUserListener.onAfterBind(imChannelContext, user);
//            }
        }catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }finally {
            lock.unlock();
        }
        return true;
    }
}
