package org.example.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import org.example.dao.MessageRepository;
import org.example.packets.bean.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.*;

public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService() {
        messageRepository = new MessageRepository();
    }

    public List<Message> getHistoryMessage(String roomId, Integer page, Integer number, int asc) {

        if (page == null || number == null) {
            return messageRepository.findSort(eq("roomId", roomId), eq("_id", 1));
        }
        return messageRepository.find(eq("roomId", roomId), eq("_id", asc), page, number);
    }

    public List<Message> getHistoryMessage(String roomId, Integer page, Integer number) {
       return this.getHistoryMessage(roomId, page, number, -1);
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
        return messageRepository.findOneLimit(eq("roomId", roomId), eq("_id", 1), 1);
    }

    public int getCount(String roomId) {
        return messageRepository.count(eq("roomId", roomId));
    }

    public void update(Message message) {
        messageRepository.updateById(message);
    }
}
