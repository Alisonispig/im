package org.example.service;

import org.example.dao.UnReadMessageRepository;
import org.example.packets.bean.UnReadMessage;

import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class UnReadMessageService {

    private final UnReadMessageRepository unReadMessageRepository;

    public UnReadMessageService() {
        unReadMessageRepository = new UnReadMessageRepository();
    }


    public void putUnReadMessage(String userId, String roomId, String messageId) {
        UnReadMessage unReadMessage = new UnReadMessage(messageId, roomId, userId);
        unReadMessageRepository.insert(unReadMessage);
    }

    public List<UnReadMessage> getUnReadMessage(String userId, String roomId) {
        return unReadMessageRepository.find(and(eq("userId", userId), eq("roomId", roomId)));
    }

    public void clearUnReadMessage(String userId, String roomId) {
        unReadMessageRepository.delete(and(eq("userId", userId), eq("roomId", roomId)));
    }
}
