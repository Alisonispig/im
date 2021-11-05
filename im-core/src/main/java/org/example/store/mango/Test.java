package org.example.store.mango;

import cn.hutool.core.util.IdUtil;
import org.example.packets.User;
import org.example.store.mango.dao.UserRepository;

public class Test {
    public static void main(String[] args) {
        UserRepository userRepository = new UserRepository();
        userRepository.insert(User.builder().id(IdUtil.getSnowflake().nextIdStr()).username("跟猴子").build());
        User byId = userRepository.findById("1456278922603159552");
        System.out.println(byId);
    }
}
