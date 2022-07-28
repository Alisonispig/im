package org.example.packets.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonId;

@Data
public class UserEmoticon {

    @BsonId
    @JSONField(name = "_id")
    private String id;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 表情包ID
     */
    private String emoticonId;

    /**
     * 排序号
     */
    private Long index;

}
