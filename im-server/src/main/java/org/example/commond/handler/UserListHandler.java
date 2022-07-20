package org.example.commond.handler;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.commond.AbstractCmdHandler;
import org.example.config.Im;
import org.example.enums.CommandEnum;
import org.example.packets.bean.User;
import org.example.packets.handler.system.RespBody;
import org.example.packets.handler.user.SearchUserReqBody;
import org.example.packets.handler.user.SearchUserRespBody;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class UserListHandler extends AbstractCmdHandler {

    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_SEARCH_USER_REQ;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext channelContext) {

        WsRequest request = (WsRequest) packet;
        User user = Im.getUser(channelContext);
        if(user == null){
            log.info("当前用户获取失败");
            return null;
        }

        SearchUserReqBody reqBody = JSON.parseObject(request.getBody(),SearchUserReqBody.class);

        SearchUserRespBody respBody = new SearchUserRespBody();
        respBody.setSearchId(reqBody.getSearchId());

        List<User> userList = userService.getUserList(reqBody.getName(), reqBody.getUserId(),user.getId());

        respBody.setUserList(userList);
        WsResponse response = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_SEARCH_USER_RESP, respBody), Im.CHARSET);

        Im.send(channelContext,response);

        return null;
    }
}
