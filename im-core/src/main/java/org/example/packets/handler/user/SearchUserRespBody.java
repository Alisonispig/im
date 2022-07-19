package org.example.packets.handler.user;

import lombok.Data;
import org.example.packets.bean.User;

import java.util.List;

@Data
public class SearchUserRespBody {

    private String searchId;

    private List<User> userList;
}
