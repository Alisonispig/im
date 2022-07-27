package org.example.service;

import cn.hutool.core.util.StrUtil;
import org.bson.conversions.Bson;
import org.example.dao.EmoticonRepository;
import org.example.packets.bean.Emoticon;

import java.util.List;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Filters.eq;

public class EmoticonService {

    private final EmoticonRepository emoticonRepository;

    public EmoticonService() {
        emoticonRepository = new EmoticonRepository();
    }

    public List<Emoticon> getEmoticons(String id, String content) {

        Pattern pattern = Pattern.compile("^.*" + content + ".*$", Pattern.CASE_INSENSITIVE);
        Bson filter = and(eq("isPrivate", false));
        if (StrUtil.isNotBlank(id) && StrUtil.isNotBlank(content)) {
            filter = and(eq("isPrivate", false), gt("_id", id), regex("name", pattern));
        }

        if(StrUtil.isNotBlank(id) && StrUtil.isBlank(content)) {
            filter = and(eq("isPrivate", false), gt("_id", id));
        }

        if(StrUtil.isBlank(id) && StrUtil.isNotBlank(content)) {
            filter = and(eq("isPrivate", false), regex("name", pattern));
        }

        return emoticonRepository.findSortLimit(filter, eq("_id", 1), 20);
    }
}
