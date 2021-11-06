package org.example.service;

import org.example.dao.GroupRepository;
import org.example.dao.UserGroupRepository;
import org.example.dao.UserRepository;
import org.example.packets.bean.Group;
import org.example.packets.bean.User;
import org.example.packets.bean.UserGroup;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class UserGroupService {

    private final UserGroupRepository userGroupRepository;

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    public UserGroupService() {
        userRepository = new UserRepository();
        userGroupRepository = new UserGroupRepository();
        groupRepository = new GroupRepository();
    }

    public void addGroupUser(String roomId, String userId) {
        UserGroup userGroup = new UserGroup();
        userGroup.setUserId(userId);
        userGroup.setRoomId(roomId);
        userGroupRepository.saveOrUpdate(and(eq("userId", userId), eq("roomId", roomId)), userGroup);
    }

    public List<User> getGroupUsers(String roomId) {
        List<UserGroup> roomIds = userGroupRepository.find(eq("roomId", roomId));
        return roomIds.stream().map(userGroup -> userRepository.findById(userGroup.getUserId())).collect(Collectors.toList());
    }


    public List<Group> getUserGroups(String userId) {
        List<UserGroup> userIds = userGroupRepository.find(eq("userId", userId));
        return userIds.stream().map(userGroup -> groupRepository.findById(userGroup.getRoomId())).sorted(Comparator.comparing(Group::getIndex).reversed()).collect(Collectors.toList());
    }
}
