package org.example.commond.handler.video;


import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import org.example.commond.AbstractCmdHandler;
import org.example.config.Im;
import org.example.enums.CommandEnum;
import org.example.enums.VideoCommandEnum;
import org.example.packets.handler.system.RespBody;
import org.example.packets.handler.video.VideoReqBody;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

import java.util.List;

public class VideoReqHandler extends AbstractCmdHandler {

    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_VIDEO_REQ;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext channelContext) {

        WsRequest request = (WsRequest) packet;

        VideoReqBody body = JSON.parseObject(request.getWsBodyText(), VideoReqBody.class);
//        body.setFromId(Im.getUser(channelContext).getId());

        List<ChannelContext> channels = Im.getChannelByUserId(body.getUserId());

        // 呼叫请求
        if (body.getCommand().equals(VideoCommandEnum.CALL)) {

            // 如果当前被呼叫人不在线
            if (CollUtil.isEmpty(channels)) {
                // 给呼叫人发送被呼叫人不在线的消息
                body.setCommand(VideoCommandEnum.NOT_ONLINE);
                WsResponse wsResponse = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_VIDEO_RESP, body), Im.CHARSET);
                Im.send(channelContext, wsResponse);
                return null;
            }

            // 给所有通道发送呼叫请求
            for (ChannelContext channel : channels) {
                WsResponse wsResponse = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_VIDEO_RESP, body), Im.CHARSET);
                Im.send(channel, wsResponse);
            }

            // 通知呼叫人呼叫成功
            body.setCommand(VideoCommandEnum.REQ_SUCCESS);
            WsResponse wsResponse = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_VIDEO_RESP, body), Im.CHARSET);
            Im.send(channelContext, wsResponse);
            return null;
        }

        // 同意通话(被呼叫人指令)
        if (body.getCommand().equals(VideoCommandEnum.AGREE)) {

            //
            List<ChannelContext> waits = Im.getChannelByUserId(body.getFromId());
            // 如果发起端已下线
            if (CollUtil.isEmpty(waits)) {
                body.setCommand(VideoCommandEnum.NOT_ONLINE);
                WsResponse wsResponse = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_VIDEO_RESP, body), Im.CHARSET);
                Im.send(channelContext, wsResponse);
                return null;
            }

            // 如果在线,那么就发送同意
            for (ChannelContext wait : waits) {
                body.setCommand(VideoCommandEnum.AGREE);
                WsResponse wsResponse = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_VIDEO_RESP, body), Im.CHARSET);
                Im.send(wait, wsResponse);
            }

        }

        // 拒绝通话(被呼叫人指令)
        if(body.getCommand().equals(VideoCommandEnum.CALLED_REFUSE)){
            List<ChannelContext> waits = Im.getChannelByUserId(body.getUserId());
            // 如果在线,那么就发送拒绝
            for (ChannelContext wait : waits) {
                body.setCommand(VideoCommandEnum.CALLED_REFUSE);
                WsResponse wsResponse = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_VIDEO_RESP, body), Im.CHARSET);
                Im.send(wait, wsResponse);
            }
        }

        // 拒绝通话(被呼叫人指令)
        if(body.getCommand().equals(VideoCommandEnum.BE_CALLED_REFUSE)){
            List<ChannelContext> waits = Im.getChannelByUserId(body.getFromId());
            // 如果在线,那么就发送拒绝
            for (ChannelContext wait : waits) {
                body.setCommand(VideoCommandEnum.BE_CALLED_REFUSE);
                WsResponse wsResponse = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_VIDEO_RESP, body), Im.CHARSET);
                Im.send(wait, wsResponse);
            }
        }

        // 超时未响应
        if(body.getCommand().equals(VideoCommandEnum.TIME_OUT)){
            List<ChannelContext> waits = Im.getChannelByUserId(body.getFromId());
            // 如果在线,那么就发送同意
            for (ChannelContext wait : waits) {
                body.setCommand(VideoCommandEnum.TIME_OUT);
                WsResponse wsResponse = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_VIDEO_RESP, body), Im.CHARSET);
                Im.send(wait, wsResponse);
            }
        }
        return null;
    }
}
