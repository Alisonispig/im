package org.example.commond;

import org.example.service.*;

public abstract class AbstractCmdHandler implements CmdHandler {

    public UserService userService;
    public FriendInfoService friendInfoService;
    public GroupService groupService;
    public UserGroupService userGroupService;
    public MessageService messageService;
    public UnReadMessageService unReadMessageService;

    public AbstractCmdHandler() {
        userService = new UserService();
        friendInfoService = new FriendInfoService();
        groupService = new GroupService();
        userGroupService = new UserGroupService();
        messageService = new MessageService();
        unReadMessageService = new UnReadMessageService();
    }

}
