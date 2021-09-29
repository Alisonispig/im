package org.example.util;

import com.alibaba.fastjson.JSON;
import org.example.config.ImConfig;
import org.example.packets.ChatReqBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ChatKit {

   private static final Logger log = LoggerFactory.getLogger(ChatKit.class);


    private static ChatReqBody parseChatBody(byte[] body) {
        if(body == null) {
            return null;
        }
        ChatReqBody chatReqBody = null;
        try{
            String text = new String(body, ImConfig.CHARSET);
            chatReqBody = JSON.parseObject(text, ChatReqBody.class);
        }catch (Exception e){
            log.error(e.toString());
        }
        return chatReqBody;
    }
}
