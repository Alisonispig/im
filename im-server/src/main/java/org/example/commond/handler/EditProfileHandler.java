package org.example.commond.handler;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import org.example.commond.AbstractCmdHandler;
import org.example.config.Chat;
import org.example.config.Im;
import org.example.enums.CommandEnum;
import org.example.packets.bean.Group;
import org.example.packets.bean.User;
import org.example.packets.handler.EditProfileReqBody;
import org.example.packets.handler.RespBody;
import org.example.packets.handler.UserStatusBody;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

import java.util.List;

public class EditProfileHandler extends AbstractCmdHandler {

    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_EDIT_PROFILE_REQ;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext channelContext) {

        WsRequest request = (WsRequest) packet;

        EditProfileReqBody editProfileReqBody = JSON.parseObject(request.getWsBodyText(), EditProfileReqBody.class);

        if (editProfileReqBody.getIsGroup()) {

            return null;
        }

        User userInfo = userService.getUserInfo(editProfileReqBody.getRoomId());
        if (StrUtil.isNotBlank(editProfileReqBody.getAvatar())) {
            userInfo.setAvatar(editProfileReqBody.getAvatar());
        }

        if (StrUtil.isNotBlank(editProfileReqBody.getName())) {
            userInfo.setUsername(editProfileReqBody.getName());
        }
        userService.updateById(userInfo);

        // 发送修改响应消息
        UserStatusBody userStatusBody = new UserStatusBody();
        userStatusBody.setUser(userInfo);
        WsResponse response = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_EDIT_PROFILE_RESP, userStatusBody), Im.CHARSET);

        Im.send(channelContext, response);

        // 给用户所在的群组发送消息
        List<Group> userGroups = userGroupService.getUserGroups(userInfo.getId());
        for (Group userGroup : userGroups) {
/*            List<User> groupUsers = messageHelper.getGroupUsers(userGroup.getRoomId());
            userGroup.setUsers(groupUsers);*/
            userStatusBody.setGroup(userGroup);
            Chat.sendToGroup(userStatusBody, channelContext, true);
        }

        return null;
    }
}
