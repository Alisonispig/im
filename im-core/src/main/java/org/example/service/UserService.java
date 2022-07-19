package org.example.service;

import cn.hutool.core.util.StrUtil;
import org.bson.conversions.Bson;
import org.example.dao.UserRepository;
import org.example.packets.Status;
import org.example.packets.bean.Message;
import org.example.packets.bean.User;

import java.util.List;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.*;

public class UserService {

    private final UserRepository userRepository;

    public UserService() {
        userRepository = new UserRepository();
    }

    public User getUserInfo(String userId) {
        return userRepository.findById(userId);
    }

    public void updateById(User userInfo) {
        userRepository.updateById(userInfo);
    }

    public User getByAccount(String account) {
        return userRepository.findOne(eq("account", account));
    }

    public void saveOrUpdate(User user) {
        userRepository.saveOrUpdateById(user.clone());
    }

    public List<User> getUserList(String name, String userId) {

        Bson filter = null;
        if (StrUtil.isNotBlank(name) && StrUtil.isNotBlank(userId)) {
            Pattern pattern = Pattern.compile("^.*" + name + ".*$", Pattern.CASE_INSENSITIVE);
            filter = and(gte("id", userId), regex("username", pattern),ne("isSystem",true));
        }
        if (StrUtil.isNotBlank(name) && StrUtil.isBlank(userId)) {
            Pattern pattern = Pattern.compile("^.*" + name + ".*$", Pattern.CASE_INSENSITIVE);
            filter =  and(regex("username", pattern),ne("isSystem",true));
        }
        if (StrUtil.isBlank(name) && StrUtil.isNotBlank(userId)) {
            filter = and(gte("id", userId),ne("isSystem",true));
        }
        if(StrUtil.isBlank(name) && StrUtil.isBlank(userId)){
            filter = and(ne("isSystem",true));
        }
        return userRepository.findSortLimit(filter, eq("id", 1), 20);
    }

    public void userOffline(String userId) {
        User user = userRepository.findById(userId);
        user.setStatus(Status.offline());
        userRepository.updateById(user);
    }
}
