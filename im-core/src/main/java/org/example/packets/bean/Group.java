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
     * 是否系统会话
     */
    private Boolean isSystem;

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
     * 是否删除
     */
    private Boolean isDeleted;

    /**
     * 是否打开通知
     */
    private Boolean notice;

    /**
     * 组用户
     */
    private List<User> users;

    /**
     * 是否公开群组
     */
    private Boolean publicRoom;

    public Group clone(){
        return BeanUtil.copyProperties(this,Group.class,"users");
    }

}
