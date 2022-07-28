package org.example.commond.handler.emoticon;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import org.example.commond.AbstractCmdHandler;
import org.example.config.Im;
import org.example.enums.CommandEnum;
import org.example.packets.bean.Emoticon;
import org.example.packets.bean.User;
import org.example.packets.handler.emoticon.EmoticonOperationReqBody;
import org.example.packets.handler.emoticon.EmoticonOperationRespBody;
import org.example.packets.handler.system.RespBody;
import org.example.service.UserEmoticonService;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

public class EmoticonOperationReqHandler extends AbstractCmdHandler {

    private final UserEmoticonService userEmoticonService;

    public EmoticonOperationReqHandler() {
        userEmoticonService = new UserEmoticonService();
    }

    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_EMOTICON_OPERATION_REQ;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext channelContext) {
        WsRequest request = (WsRequest) packet;

        User user = Im.getUser(channelContext);

        EmoticonOperationReqBody reqBody = JSON.parseObject(request.getBody(), EmoticonOperationReqBody.class);
        EmoticonOperationRespBody respBody = new EmoticonOperationRespBody();
        respBody.setType(reqBody.getType());

        switch (reqBody.getType()) {
            case INSERT_TO_USER:
                respBody.setEmoticon(this.insertToUser(reqBody, user));
                break;
            case DELETE:
                respBody.setEmoticon(this.deleteUserEmoticon(reqBody, user));
                break;
            case INSERT_TO_STORE:
                this.insertToStore(reqBody, user);
                break;
            case INSERT_TO_USER_AND_STORE:
                this.insertToUserAndStore(reqBody, user);
                break;
            case INSERT_EMOTICON_TO_USER:
                respBody.setEmoticon(this.insertEmoticonToUser(reqBody, user));
                break;
        }

        String success = RespBody.success(CommandEnum.COMMAND_EMOTICON_OPERATION_RESP, respBody);
        WsResponse response = WsResponse.fromText(success, Im.CHARSET);
        Im.send(channelContext, response);
        return null;
    }

    private Emoticon insertEmoticonToUser(EmoticonOperationReqBody reqBody, User user) {
        userEmoticonService.insert(reqBody.getEmoticonId(), user.getId());
        return emoticonService.getEmoticon(reqBody.getEmoticonId());
    }

    private void insertToUserAndStore(EmoticonOperationReqBody body, User user) {
        String suffix = FileNameUtil.getSuffix(body.getUrl());
        String name = body.getName() + (StrUtil.isNotBlank(suffix) ? CharUtil.DOT : "") + suffix;
        String id = emoticonService.insert(name, body.getSize(), body.getUrl(), false);

        userEmoticonService.insert(id, user.getId());
    }

    private void insertToStore(EmoticonOperationReqBody reqBody, User user) {
        Emoticon emoticon = emoticonService.getEmoticon(reqBody.getEmoticonId());
        emoticon.setIsPrivate(false);
        emoticonService.update(emoticon);
    }

    private Emoticon insertToUser(EmoticonOperationReqBody body, User user) {
        String suffix = FileNameUtil.getSuffix(body.getUrl());
        String name = IdUtil.getSnowflakeNextIdStr() + (StrUtil.isNotBlank(suffix) ? CharUtil.DOT : "") + suffix;
        String id = emoticonService.insert(name, body.getSize(), body.getUrl(), true);

        userEmoticonService.insert(id, user.getId());
        return emoticonService.getEmoticon(id);
    }

    private Emoticon deleteUserEmoticon(EmoticonOperationReqBody body, User user) {
        userEmoticonService.delete(body.getEmoticonId(), user.getId());
        return emoticonService.getEmoticon(body.getEmoticonId());
    }
}
