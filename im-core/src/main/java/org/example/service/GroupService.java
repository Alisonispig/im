package org.example.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import org.bson.conversions.Bson;
import org.example.dao.GroupRepository;
import org.example.packets.LastMessage;
import org.example.packets.bean.Group;
import org.example.packets.bean.Message;

import java.util.List;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.*;

public class GroupService {
    private final GroupRepository groupRepository;

    public GroupService() {
        groupRepository = new GroupRepository();
    }

    public void insertOne(Group group) {
        groupRepository.insert(group);
    }

    public void saveOrUpdate(Group build) {
        groupRepository.saveOrUpdate(eq("roomId", build.getRoomId()), build);
    }

    public void saveOrUpdateById(Group build) {
        groupRepository.saveOrUpdateById(build.clone());
    }

    public Group getGroupInfo(String roomId) {
        return groupRepository.findById(roomId);
    }

    public void updateLastMessage(Message message) {
        Group group = groupRepository.findById(message.getRoomId());

        LastMessage lastMessage = BeanUtil.copyProperties(message, LastMessage.class);
        lastMessage.setMessageId(message.getId());
        if (message.getDeleted()) {
            lastMessage.setContent("删除了一条消息");
        }
        lastMessage.setIndexId(message.getSendTime());
        if (StrUtil.isBlank(message.getContent()) && CollUtil.isNotEmpty(message.getFiles()) && !message.getDeleted()) {
            if (message.getFiles().size() == 1 && Boolean.TRUE.equals(message.getFiles().get(0).getIsEmoticon())) {
                lastMessage.setContent("[表情包]");
            } else if (message.getFiles().size() == 1 && !Boolean.TRUE.equals(message.getFiles().get(0).getIsEmoticon())) {
                lastMessage.setContent("[文件] - " + message.getFiles().get(0).getName());
            } else {
                lastMessage.setContent("[文件] - " + message.getFiles().get(0).getName() + "等多个文件");
            }
        }

        group.setIndex(System.currentTimeMillis());
        group.setLastMessage(lastMessage);
        groupRepository.updateById(group);
    }

    public void updateById(Group userGroup) {
        groupRepository.updateById(userGroup.clone());
    }

    public void delete(String roomId) {
        Group group = groupRepository.findById(roomId);
        group.setIsDeleted(true);
        groupRepository.updateById(group);
    }

    public void readLastMessage(Group groupInfo) {
        groupInfo.getLastMessage().setSeen(true);
        groupRepository.updateById(groupInfo);
    }

    public List<Group> getRoomList(String name, String roomId) {
        Bson filter = null;
        if (StrUtil.isNotBlank(name) && StrUtil.isNotBlank(roomId)) {
            Pattern pattern = Pattern.compile("^.*" + name + ".*$", Pattern.CASE_INSENSITIVE);
            filter = and(gte("_id", roomId), regex("roomName", pattern),eq("publicRoom",true),ne("isDeleted", true));
        }
        if (StrUtil.isNotBlank(name) && StrUtil.isBlank(roomId)) {
            Pattern pattern = Pattern.compile("^.*" + name + ".*$", Pattern.CASE_INSENSITIVE);
            filter =  and(regex("roomName", pattern),eq("publicRoom",true),ne("isDeleted", true));
        }
        if (StrUtil.isBlank(name) && StrUtil.isNotBlank(roomId)) {
            filter = and(gte("_id", roomId),eq("publicRoom",true),ne("isDeleted", true));
        }
        if(StrUtil.isBlank(name) && StrUtil.isBlank(roomId)){
            filter = and(eq("publicRoom",true),ne("isDeleted", true));
        }
        return groupRepository.findSortLimit(filter, eq("_id", 1), 20);
    }
}
