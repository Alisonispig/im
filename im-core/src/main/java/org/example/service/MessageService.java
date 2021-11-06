package org.example.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import org.example.dao.MessageRepository;
import org.example.packets.bean.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.eq;

public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(){
        messageRepository = new MessageRepository();
    }

    public List<Message> getHistoryMessage(String roomId) {
       return messageRepository.find(eq("roomId",roomId));
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
}
