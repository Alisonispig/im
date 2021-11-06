package org.example.listener;

import lombok.extern.slf4j.Slf4j;
import org.example.config.Im;
import org.example.packets.bean.Group;
import org.example.packets.bean.User;
import org.example.service.GroupService;
import org.example.service.UserGroupService;
import org.tio.core.ChannelContext;

@Slf4j
public class ImServerGroupListener extends AbstractImGroupListener {

    public final UserGroupService userGroupService = new UserGroupService();
    public final GroupService groupService = new GroupService();

    @Override
    public void doAfterBind(ChannelContext channelContext, Group group) {
        log.info("群组绑定监听");
        // 将绑定信息持久化到Redis
//        ImConfig.get().messageHelper.onAfterGroupBind(channelContext, group);

        String roomId = group.getRoomId();
        User user = Im.getUser(channelContext);
        // 添加用户群组信息 顺手就完成了群组用户 用户群组的绑定
        userGroupService.addGroupUser(user.getId(), roomId);

        for (Group userGroup : user.getGroups()) {
            if (!userGroup.getRoomId().equals(roomId)) {
                continue;
            }
            groupService.updateById(userGroup);
        }
    }

    @Override
    public void doAfterUnbind(ChannelContext channelContext, Group group) {
        log.info("群组绑监听");
    }

}
