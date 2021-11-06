package org.example.service;

import org.example.dao.UserRepository;
import org.example.packets.Status;
import org.example.packets.bean.User;

import java.util.List;

import static com.mongodb.client.model.Filters.eq;

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

    public List<User> getUserList() {
        return userRepository.find();
    }

    public void userOffline(String userId) {
        User user = userRepository.findById(userId);
        user.setStatus(Status.offline());
        userRepository.updateById(user);
    }
}
