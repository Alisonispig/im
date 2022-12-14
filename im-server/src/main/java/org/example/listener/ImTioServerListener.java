package org.example.listener;

import org.example.config.Chat;
import org.example.config.Im;
import org.example.config.ImConfig;
import org.example.config.ImSessionContext;
import org.example.enums.KeyEnum;
import org.example.packets.ImClientNode;
import org.example.packets.bean.Group;
import org.example.packets.bean.User;
import org.example.packets.bean.UserGroup;
import org.example.packets.handler.user.UserStatusBody;
import org.example.service.UserGroupService;
import org.example.service.UserService;
import org.tio.core.ChannelContext;
import org.tio.core.Node;
import org.tio.core.Tio;
import org.tio.core.intf.Packet;
import org.tio.utils.lock.SetWithLock;
import org.tio.websocket.server.WsTioServerListener;


import java.util.List;

public class ImTioServerListener extends WsTioServerListener {

    private final UserService userService = new UserService();

    private final UserGroupService userGroupService = new UserGroupService();

    public static final ImTioServerListener me = new ImTioServerListener();

    @Override
    public boolean onHeartbeatTimeout(ChannelContext channelContext, Long interval, int heartbeatTimeoutCount) {
        return false;
    }

    @Override
    public void onAfterConnected(ChannelContext channelContext, boolean isConnected, boolean isReconnect) throws Exception {
        ImSessionContext sessionContext = new ImSessionContext();
        Node clientNode = channelContext.getClientNode();
        ImClientNode build = ImClientNode.builder().ip(clientNode.getIp()).port(clientNode.getPort()).build();
        build.setId(channelContext.getId());
        sessionContext.setImClientNode(build);
        channelContext.set(KeyEnum.IM_CHANNEL_SESSION_CONTEXT_KEY.getKey(), sessionContext);
        super.onAfterConnected(channelContext, isConnected, isReconnect);
    }

    @Override
    public void onAfterDecoded(ChannelContext channelContext, Packet packet, int packetSize) throws Exception {
        super.onAfterDecoded(channelContext, packet, packetSize);
    }

    @Override
    public void onAfterReceivedBytes(ChannelContext channelContext, int receivedBytes) throws Exception {
        super.onAfterReceivedBytes(channelContext, receivedBytes);
    }

    @Override
    public void onAfterSent(ChannelContext channelContext, Packet packet, boolean isSentSuccess) throws Exception {
        super.onAfterSent(channelContext, packet, isSentSuccess);
    }

    @Override
    public void onAfterHandled(ChannelContext channelContext, Packet packet, long cost) throws Exception {
        super.onAfterHandled(channelContext, packet, cost);
    }

    @Override
    public void onBeforeClose(ChannelContext channelContext, Throwable throwable, String remark, boolean isRemove) throws Exception {


        User user = Im.getUser(channelContext);
        // ?????????????????????????????????
        if (user != null) {
            // ????????????????????????????????????, ????????????????????????, ???????????????????????????
            SetWithLock<ChannelContext> userChannelContexts = Tio.getByUserid(ImConfig.get().getTioConfig(), user.getId());
            if(userChannelContexts.size() == 1){
                // ???????????????????????????
                userService.userOffline(user.getId());

                UserStatusBody build = UserStatusBody.builder().user(userService.getUserInfo(user.getId())).build();

                for (Group group : user.getGroups()) {
                    UserGroup userGroup = userGroupService.getUserGroup(group.getRoomId(), user.getId());
                    build.getUser().setRole(userGroup.getRole());
                    // ????????????????????????????????? ??????????????????
                    List<User> groupUsers = userGroupService.getGroupUsers(group.getRoomId());
                    group.setUsers(groupUsers);
                    build.setGroup(group);
                    Chat.sendToGroup(build, channelContext);
                }
            }

        }

        super.onBeforeClose(channelContext, throwable, remark, isRemove);
    }
}
