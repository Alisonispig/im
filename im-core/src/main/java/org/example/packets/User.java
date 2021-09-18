package org.example.packets;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
public class User implements Serializable {

    private String _id;

    private String username;

    private String avatar;

    private Status status;

    private List<Group> groups;

    public void addGroup(Group group){
        if(groups == null){
            groups = new ArrayList<>();
        }
        groups.add(group);
    }

}
