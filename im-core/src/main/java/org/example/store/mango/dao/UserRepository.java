package org.example.store.mango.dao;

import com.mongodb.client.model.Filters;
import org.example.packets.User;
import org.example.store.mango.MongoRepository;

public class UserRepository extends MongoRepository<User> {

    public User findById(String userId) {
        return findOne(Filters.eq("_id", userId));
    }
}
