package org.example.service;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import org.bson.conversions.Bson;
import org.example.config.CourierConfig;
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

    public Emoticon getEmoticon(String emoticonId) {
        return emoticonRepository.findById(emoticonId);
    }

    public String insert(String name, int size, String url, Boolean isPrivate) {
        if(!url.startsWith("http://") && !url.startsWith("https://")){
            url = CourierConfig.fileUrl + url;
        }

        Emoticon emoticon = new Emoticon();
        emoticon.setId(IdUtil.getSnowflakeNextIdStr());
        emoticon.setName(name);
        emoticon.setSize((long) size);
        emoticon.setIsPrivate(isPrivate);
        emoticon.setType(FileNameUtil.getSuffix(name));
        emoticon.setUrl(url);

        emoticonRepository.insert(emoticon);
        return emoticon.getId();
    }

    public void update(Emoticon emoticon) {
        emoticonRepository.updateById(emoticon);
    }
}
