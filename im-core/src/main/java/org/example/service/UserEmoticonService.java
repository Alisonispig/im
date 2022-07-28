package org.example.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import org.bson.conversions.Bson;
import org.example.dao.EmoticonRepository;
import org.example.dao.UserEmoticonRepository;
import org.example.packets.bean.Emoticon;
import org.example.packets.bean.UserEmoticon;

import java.util.List;

import static com.mongodb.client.model.Filters.*;

public class UserEmoticonService {

    private final UserEmoticonRepository userEmoticonRepository;
    private final EmoticonRepository emoticonRepository;

    public UserEmoticonService() {
        userEmoticonRepository = new UserEmoticonRepository();
        emoticonRepository = new EmoticonRepository();
    }


    public List<UserEmoticon> getUserEmoticons(String emoticonId, String userId) {

        Bson filter = and( eq("userId", userId));
        if(StrUtil.isNotBlank(emoticonId)){
            UserEmoticon one = userEmoticonRepository.findOne(and(eq("emoticonId", emoticonId), eq("userId", userId)));
            filter = and(lt("index", one.getIndex()), eq("userId", userId));
        }
        return userEmoticonRepository.findSort(filter, eq("index", -1));
    }

    public void insert(String emoticonId, String userId) {

        UserEmoticon one = userEmoticonRepository.findOne(and(eq("emoticonId", emoticonId), eq("userId", userId)));
        if (one != null) {
            return;
        }

        UserEmoticon userEmoticon = new UserEmoticon();
        userEmoticon.setId(IdUtil.getSnowflakeNextIdStr());
        userEmoticon.setEmoticonId(emoticonId);
        userEmoticon.setUserId(userId);
        userEmoticon.setIndex(System.currentTimeMillis());
        userEmoticonRepository.insert(userEmoticon);
    }

    public void delete(String emoticonId, String userId) {
        userEmoticonRepository.delete(and(eq("emoticonId", emoticonId), eq("userId", userId)));
    }
}
