package org.example.packets.handler.room;

import lombok.Data;
import org.example.packets.bean.Group;
import org.example.packets.bean.User;

import java.util.List;

@Data
public class SearchRoomRespBody {

    private String searchId;

    private List<Group> roomList;
}
