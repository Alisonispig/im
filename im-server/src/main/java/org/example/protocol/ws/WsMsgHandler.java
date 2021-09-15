package org.example.protocol.ws;

import org.example.commond.CommandManager;
import org.example.commond.handler.LoginReqHandler;
import org.example.config.Im;
import org.example.enums.CommandEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.ChannelContext;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;
import org.tio.websocket.server.handler.IWsMsgHandler;

public class WsMsgHandler implements IWsMsgHandler {
    private static final Logger log = LoggerFactory.getLogger(WsMsgHandler.class);

    public static final WsMsgHandler me = new WsMsgHandler();


    @Override
    public HttpResponse handshake(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
        String clientip = httpRequest.getClientIp();
        String username = httpRequest.getParam("username");

//        Tio.bindUser(channelContext, username);
//		channelContext.setUserid(username);
        log.info("收到来自{}的ws握手包\r\n{}", clientip, httpRequest);
//        RespBody heartbeatBody = new RespBody(CommandEnum.COMMAND_HANDSHAKE_REQ).setData(new HeartbeatBody((byte) -128));
//        httpResponse.setBody(heartbeatBody.toByte());
//        ImPacket imPacket = new ImPacket(CommandEnum.COMMAND_HANDSHAKE_RESP, );
//        String str = "{cmd:13,data:-128}";
//        WsResponse wsResponse = WsResponse.fromText(str, ImConfig.CHARSET);
//        Tio.send(channelContext, wsResponse);
        return httpResponse;
    }

    @Override
    public void onAfterHandshaked(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
        LoginReqHandler loginHandler = (LoginReqHandler) CommandManager.getCommand(CommandEnum.COMMAND_LOGIN_REQ);
        WsResponse response = loginHandler.handler(httpRequest, channelContext);
        Im.send(channelContext, response);
        log.info("握手完毕{},{}", "66", "666");
    }

    @Override
    public Object onBytes(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {

        return null;
    }

    @Override
    public Object onClose(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
        return null;
    }

    @Override
    public Object onText(WsRequest wsRequest, String text, ChannelContext channelContext) throws Exception {
        log.info("socket消息:{}", text);
//        ChatBody chatBody = ChatKit.toChatBody(wsRequest.getBody(), channelContext);

        return "{cmd:13,data:-128}";
    }
}
