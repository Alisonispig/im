package org.example.commond.handler.room;

import com.alibaba.fastjson.JSON;
import org.example.commond.AbstractCmdHandler;
import org.example.config.Im;
import org.example.enums.CommandEnum;
import org.example.packets.bean.User;
import org.example.packets.bean.UserGroup;
import org.example.packets.handler.system.RespBody;
import org.example.packets.handler.room.UserGroupConfigReqBody;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

/**
 * 群组用户修改自己的配置
 * <p>
 * 通知 / 群组备注 / 成员备注 等
 * </p>
 *
 * @author smart
 * @since 1.0.0
 */
public class UserGroupConfigReqHandler extends AbstractCmdHandler {

    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_USER_GROUP_CONFIG_REQ;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext channelContext) {

        WsRequest request = (WsRequest) packet;

        UserGroupConfigReqBody body = JSON.parseObject(request.getWsBodyText(), UserGroupConfigReqBody.class);

        switch (body.getType()) {
            case NOTICE:
                setNotice(body, channelContext);
                break;
            case GROUP_REMARK:
                setGroupRemark(body, channelContext);
                break;
            default:
                break;
        }

        return null;
    }

    /**
     * 设置通知
     *
     * @param config         配置信息
     * @param channelContext 上下文信息
     */
    private void setNotice(UserGroupConfigReqBody config, ChannelContext channelContext) {

        User user = Im.getUser(channelContext);
        UserGroup userGroup = userGroupService.getUserGroup(config.getRoomId(), user.getId());
        userGroup.setNotice(config.getNotice());
        userGroupService.update(userGroup);

        WsResponse wsResponse = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_USER_GROUP_CONFIG_RESP, config), Im.CHARSET);
        Im.send(channelContext, wsResponse);
    }

    /**
     * 设置群组备注
     *
     * @param config         配置信息
     * @param channelContext 上下文信息
     */
    private void setGroupRemark(UserGroupConfigReqBody config, ChannelContext channelContext) {

    }
}
