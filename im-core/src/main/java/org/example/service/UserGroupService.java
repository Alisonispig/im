package org.example.service;

import com.mongodb.client.model.Field;
import org.example.dao.GroupRepository;
import org.example.dao.UserGroupRepository;
import org.example.dao.UserRepository;
import org.example.enums.RoomRoleEnum;
import org.example.packets.LastMessage;
import org.example.packets.bean.Group;
import org.example.packets.bean.User;
import org.example.packets.bean.UserGroup;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Aggregates.set;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.combine;

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
        addGroupUser(roomId, userId, RoomRoleEnum.GENERAL);
    }

    public void addGroupUser(String roomId, String userId,Boolean isSystem) {
        addGroupUser(roomId, userId, RoomRoleEnum.GENERAL,isSystem);
    }

    public void addGroupUser(String roomId, String userId, RoomRoleEnum role, Boolean isSystem) {
        UserGroup userGroup = new UserGroup();
        userGroup.setUserId(userId);
        userGroup.setRoomId(roomId);
        userGroup.setNotice(true);
        userGroup.setRole(role);
        userGroup.setIsSystem(isSystem);
        userGroupRepository.saveOrUpdate(and(eq("userId", userId), eq("roomId", roomId)), userGroup);
    }

    public void addGroupUser(String roomId, String userId, RoomRoleEnum role) {
        addGroupUser(roomId, userId, role, false);
    }

    public List<User> getGroupUsers(String roomId) {
        List<UserGroup> roomIds = userGroupRepository.find(eq("roomId", roomId));
        return roomIds.stream().map(userGroup -> {
            User user = userRepository.findById(userGroup.getUserId());
            user.setRole(userGroup.getRole());
            return user;
        }).collect(Collectors.toList());
    }

    public List<User> getGroupUsers(String roomId, List<String> ids) {
        List<UserGroup> roomIds = userGroupRepository.find(eq("roomId", roomId));
        return roomIds.stream().filter(x -> !ids.contains(x.getUserId())).map(userGroup -> {
            User user = userRepository.findById(userGroup.getUserId());
            user.setRole(userGroup.getRole());
            return user;
        }).collect(Collectors.toList());
    }

    public List<Group> getUserGroups(String userId) {
        List<UserGroup> userIds = userGroupRepository.find(and(eq("userId", userId), ne("roomDeleted", true)));
        return userIds.stream().map(userGroup ->  groupRepository.findById(userGroup.getRoomId()))
                .sorted(Comparator.comparing(Group::getIndex).reversed()).collect(Collectors.toList());
    }

    public void remove(String roomId, String userId) {
        userGroupRepository.delete(and(eq("roomId", roomId), eq("userId", userId)));
    }

    public UserGroup getUserGroup(String roomId, String userId) {
        return userGroupRepository.findOne(and(eq("roomId", roomId), eq("userId", userId)));
    }

    public void update(UserGroup userGroup) {
        userGroupRepository.replace(and(eq("roomId", userGroup.getRoomId()), eq("userId", userGroup.getUserId())), userGroup);
    }

    public void delete(String roomId) {
        userGroupRepository.updateMany(eq("roomId", roomId), set(new Field<>("roomDeleted", true)));
    }

    public UserGroup getSystemUserGroup(String userId) {
        return userGroupRepository.findOne(and(eq("isSystem", true), eq("userId", userId)));
    }
}
