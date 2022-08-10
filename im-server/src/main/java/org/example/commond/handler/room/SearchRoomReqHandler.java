package org.example.commond.handler.room;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.commond.AbstractCmdHandler;
import org.example.config.Im;
import org.example.enums.CommandEnum;
import org.example.packets.bean.Group;
import org.example.packets.bean.User;
import org.example.packets.handler.room.SearchRoomReqBody;
import org.example.packets.handler.room.SearchRoomRespBody;
import org.example.packets.handler.system.RespBody;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

import java.util.List;

@Slf4j
public class SearchRoomReqHandler extends AbstractCmdHandler {


    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_SEARCH_ROOM_REQ;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext channelContext) {
        WsRequest request = (WsRequest) packet;
        User user = Im.getUser(channelContext);
        if(user == null){
            log.info("当前用户获取失败");
            return null;
        }

        SearchRoomReqBody reqBody = JSON.parseObject(request.getBody(), SearchRoomReqBody.class);

        SearchRoomRespBody respBody = new SearchRoomRespBody();
        respBody.setSearchId(reqBody.getSearchId());

        List<Group> roomList = groupService.getUserList(reqBody.getName(), reqBody.getRoomId());

        respBody.setRoomList(roomList);
        WsResponse response = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_SEARCH_ROOM_RESP, respBody), Im.CHARSET);

        Im.send(channelContext,response);
        return null;
    }
}
