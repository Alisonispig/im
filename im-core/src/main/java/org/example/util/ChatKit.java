package org.example.util;

import com.alibaba.fastjson.JSON;
import org.example.config.ImConfig;
import org.example.config.ImSessionContext;
import org.example.enums.KeyEnum;
import org.example.packets.ChatBody;
import org.example.packets.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.ChannelContext;


public class ChatKit {

   private static final Logger log = LoggerFactory.getLogger(ChatKit.class);


    private static ChatBody parseChatBody(byte[] body) {
        if(body == null) {
            return null;
        }
        ChatBody chatBody = null;
        try{
            String text = new String(body, ImConfig.CHARSET);
            chatBody = JSON.parseObject(text, ChatBody.class);
        }catch (Exception e){
            log.error(e.toString());
        }
        return chatBody;
    }
}
