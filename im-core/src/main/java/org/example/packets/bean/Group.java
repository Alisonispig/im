package org.example.packets.bean;

import cn.hutool.core.bean.BeanUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.example.packets.LastMessage;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Group implements Serializable {

    /**
     * 群组ID
     */
    @BsonId
    private String roomId;

    /**
     * 排序
     */
    private long index;

    /**
     * 是否好友分组
     */
    private Boolean isFriend;

    /**
     * 群组名称
     */
    private String roomName;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 好友ID
     */
    @BsonIgnore
    private String friendId;

    /**
     * 最后一条消息
     */
    private LastMessage lastMessage;

    /**
     * 组用户
     */
    private List<User> users;

    public Group clone(){
        return BeanUtil.copyProperties(this,Group.class,"users");
    }

}
