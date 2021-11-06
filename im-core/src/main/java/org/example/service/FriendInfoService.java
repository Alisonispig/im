package org.example.service;

import org.example.dao.FriendInfoRepository;
import org.example.packets.bean.FriendInfo;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class FriendInfoService {

    private final FriendInfoRepository friendInfoRepository;

    public FriendInfoService() {
        this.friendInfoRepository = new FriendInfoRepository();
    }

    /**
     * 创建双向好友
     * @param roomId 房间号
     * @param self 自己
     * @param friendId 好友ID
     */
    public void createFriendTwoWay(String roomId, String self, String friendId) {
        FriendInfo friendInfo = new FriendInfo();
        friendInfo.setRoomId(roomId);
        friendInfo.setRemark("");
        friendInfo.setSelf(self);
        friendInfo.setFriendId(friendId);
        friendInfoRepository.insert(friendInfo);
        friendInfo.setSelf(friendId);
        friendInfo.setFriendId(self);
        friendInfoRepository.insert(friendInfo);
    }

    public FriendInfo getFriendInfo(String roomId, String userId) {
        return  friendInfoRepository.findOne(and(eq("roomId", roomId), eq("self", userId)));
    }
}
