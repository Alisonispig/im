package org.example.packets;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class User {

    private String _id;

    private String username;

    private String avatar;

    private Status status;

}
