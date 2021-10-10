package org.example.commond.handler;

import cn.hutool.core.util.StrUtil;
import org.example.commond.AbstractCmdHandler;
import org.example.config.Im;
import org.example.enums.CommandEnum;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

public class CloseReqHandler extends AbstractCmdHandler {

    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_CLOSE_REQ;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext channelContext) {
        WsRequest request = (WsRequest) packet;
        String userId = request.getWsBodyText();


        if(StrUtil.isBlank(userId)){
            Im.remove(channelContext, "收到关闭请求");
        }else{
            Im.remove(userId, "收到关闭请求!");
        }
        return null;
    }
}
