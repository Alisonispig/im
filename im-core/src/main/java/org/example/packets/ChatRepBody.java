package org.example.packets;

import lombok.Data;

@Data
public class ChatRepBody {

    private Integer _id;

    private String content;

    private String senderId;


}
