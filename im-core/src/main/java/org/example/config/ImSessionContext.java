package org.example.config;

import lombok.Data;
import org.example.packets.ImClientNode;
import org.tio.websocket.common.WsSessionContext;

@Data
public class ImSessionContext extends WsSessionContext {

    /**
     * 客户端Node信息
     */
    protected ImClientNode imClientNode = null;

    /**
     * 客户端票据token,一般业务设置用
     */
    protected String token = null;

    /**
     * sessionContext唯一ID
     */
    protected String id;
}
