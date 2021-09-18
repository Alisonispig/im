package org.example.packets;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import org.example.enums.CommandEnum;
import org.tio.core.intf.Packet;


public class ImPacket extends Packet {

    /**
     * 包状态码;
     */
    protected Status status;
    /**
     * 消息体;
     */
    protected String body;
    /**
     * 消息命令;
     */
    private CommandEnum command;


    public ImPacket(CommandEnum command, Object data) {
        if (ObjectUtil.isNotNull(data)) {
            this.body = JSON.toJSONString(data);
        }
        this.command = command;
    }

}
