package org.example.packets.bean;

import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonId;

@Data
public class Auth {

    @BsonId
    private String id;

    private String password;
}
