package org.example.commond.handler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.example.commond.AbstractCmdHandler;
import org.example.config.Chat;
import org.example.config.Im;
import org.example.enums.CommandEnum;
import org.example.packets.bean.Group;
import org.example.packets.bean.User;
import org.example.packets.handler.JoinGroupNotifyBody;
import org.example.packets.handler.RespBody;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

import java.util.List;

@Slf4j
public class JoinGroupReqHandler extends AbstractCmdHandler {

    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_JOIN_GROUP_REQ;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext channelContext) {

        WsRequest request = (WsRequest) packet;
        String str = StrUtil.str(request.getBody(), Im.CHARSET);

        // 转换为加入群组消息
        JoinGroupNotifyBody joinGroupNotifyBody = JSON.parseObject(str, JoinGroupNotifyBody.class);
        // 异常判断: 如果加入的用户未空, 则返回
        if (CollUtil.isEmpty(joinGroupNotifyBody.getUsers())) {
            return null;
        }

        // 获取完整的群组信息并返回
        Group group = groupService.getGroupInfo(joinGroupNotifyBody.getGroup().getRoomId());
        joinGroupNotifyBody.setGroup(group);

        // 处理要加入的完整信息
        for (User user : joinGroupNotifyBody.getUsers()) {
            User userInfo = userService.getUserInfo(user.getId());
            BeanUtil.copyProperties(userInfo, user);
        }

        // TODO 加入群组是否成功
        log.info("加入群组消息：" + JSON.toJSONString(joinGroupNotifyBody, SerializerFeature.DisableCircularReferenceDetect));

        // 绑定到群聊
        for (User addUser : joinGroupNotifyBody.getUsers()) {

            // 用户是否在线,在线直接绑定一个群组
            List<ChannelContext> channelByUserId = Im.getChannelByUserId(addUser.getId());
            if (CollUtil.isNotEmpty(channelByUserId)) {
                for (ChannelContext context : channelByUserId) {
                    Im.addGroup(context, group);
                    Im.bindGroup(context, group);
                }
            }
            // 持久化到数据库
            userGroupService.addGroupUser(group.getRoomId(), addUser.getId());
        }

        // 发送申请加入群组响应
        WsResponse wsResponse = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_JOIN_GROUP_RESP, joinGroupNotifyBody), Im.CHARSET);
        Im.send(channelContext, wsResponse);

        // 发送加入群组消息
        Chat.sendToGroup(joinGroupNotifyBody);

        return null;
    }
}