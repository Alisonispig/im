package org.example.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import org.bson.conversions.Bson;
import org.example.dao.MessageRepository;
import org.example.enums.MessageFetchTypeEnum;
import org.example.packets.bean.Message;

import java.util.*;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.*;

public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService() {
        messageRepository = new MessageRepository();
    }

    public List<Message> getHistoryMessage(String roomId, String messageId, int asc) {
        Message message = messageId == null ? getLastMessage(roomId) : getMessage(messageId);
        if (asc == 1) {
            var a = message == null ? and(eq("roomId", roomId)) : and(eq("roomId", roomId), lte(Message.COL_SEND_TIME, message.getSendTime()));
            return messageRepository.findSortLimit(a, eq(Message.COL_SEND_TIME, -1), 20);
        } else {
            return messageRepository.findSortLimit(and(eq("roomId", roomId), gte(Message.COL_SEND_TIME, message.getSendTime())), eq(Message.COL_SEND_TIME, 1), 20);
        }
    }

    public List<Message> getHistoryMessage(String roomId, String messageId, MessageFetchTypeEnum type) {
        return this.getHistoryMessage(roomId, messageId, MessageFetchTypeEnum.DOWN.equals(type) ? -1 : 1);
    }

    public void addReaction(String messageId, String reaction, Boolean remove, String userId) {

        Message message = messageRepository.findById(messageId);

        Map<String, List<String>> reactions = ObjectUtil.defaultIfNull(message.getReactions(), new HashMap<>());

        List<String> userIds = reactions.get(reaction);
        if (Boolean.TRUE.equals(remove)) {
            userIds.remove(userId);
        } else {
            if (CollUtil.isEmpty(userIds)) {
                reactions.put(reaction, CollUtil.newArrayList(userId));
            } else {
                userIds.add(userId);
            }
        }
        message.setReactions(reactions);
        messageRepository.updateById(message);
    }

    public Map<String, List<String>> getReaction(String messageId) {
        Message message = messageRepository.findById(messageId);
        return message.getReactions();
    }


    public Message getMessage(String messageId) {
        return messageRepository.findById(messageId);
    }

    public void putGroupMessage(Message message) {
        messageRepository.insert(message);
    }

    public List<Message> getFileHistory(String roomId, String date) {
        return messageRepository.find(and(eq("roomId", roomId), eq("date", date), not(size("files", 0))));
    }

    public Message getStartMessage(String roomId) {
        return messageRepository.findOneLimit(eq("roomId", roomId), eq(Message.COL_SEND_TIME, 1), 1);
    }

    public int getCount(String roomId) {
        return messageRepository.count(eq("roomId", roomId));
    }

    public void update(Message message) {
        messageRepository.updateById(message);
    }

    public Message getLastMessage(String roomId) {
        return messageRepository.findOne(eq("roomId", roomId), eq(Message.COL_SEND_TIME, -1));
    }

    public void read(String messageId) {
        Message message = messageRepository.findById(messageId);
        message.setSeen(true);
        messageRepository.updateById(message);
    }

    public List<Message> getMessage(String roomId, String content, Long startTime, Long endTime) {
        Pattern pattern = Pattern.compile("^.*" + content + ".*$", Pattern.CASE_INSENSITIVE);
        Bson filter = and(eq("roomId", roomId), regex("content", pattern), ne("deleted", true));
        if (startTime != null && endTime != null) {
            filter = and(eq("roomId", roomId), regex("content", pattern), gte(Message.COL_SEND_TIME, startTime), lte(Message.COL_SEND_TIME, endTime), ne("deleted", true));
        }
        return messageRepository.findSort(filter, eq(Message.COL_SEND_TIME, -1));
    }
}
