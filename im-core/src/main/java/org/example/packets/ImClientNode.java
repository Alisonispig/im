package org.example.packets;

import lombok.Builder;
import lombok.Data;
import org.example.packets.bean.User;

@Builder
@Data
public class ImClientNode {

    private String id;

    /**
     * 客户端ip
     */
    private String ip;
    /**
     * 客户端远程port
     */
    private int port;
    /**
     * 如果没登录过，则为null
     */
    private User user;
    /**
     * 地区
     */
    private String region;
    /**
     * 浏览器信息(这里暂时放在这,后面会扩展出比如httpImClientNode、TcpImClientNode等)
     */
    private String useragent;
}
