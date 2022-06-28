package org.example.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommandEnum {
    COMMAND_UN_KNOW(0),
    /**
     * <pre>
     * 握手请求，含http的websocket握手请求
     * </pre>
     *
     * <code>COMMAND_HANDSHAKE_REQ = 1;</code>
     */
    COMMAND_HANDSHAKE_REQ(1),
    /**
     * <pre>
     * 握手响应，含http的websocket握手响应
     * </pre>
     *
     * <code>COMMAND_HANDSHAKE_RESP = 2;</code>
     */
    COMMAND_HANDSHAKE_RESP(2),
    /**
     * <pre>
     * 鉴权请求
     * </pre>
     *
     * <code>COMMAND_AUTH_REQ = 3;</code>
     */
    COMMAND_AUTH_REQ(3),
    /**
     * <pre>
     * 鉴权响应
     * </pre>
     *
     * <code>COMMAND_AUTH_RESP = 4;</code>
     */
    COMMAND_AUTH_RESP(4),
    /**
     * <pre>
     * 登录请求
     * </pre>
     *
     * <code>COMMAND_LOGIN_REQ = 5;</code>
     */
    COMMAND_LOGIN_REQ(5),
    /**
     * <pre>
     * 登录响应
     * </pre>
     *
     * <code>COMMAND_LOGIN_RESP = 6;</code>
     */
    COMMAND_LOGIN_RESP(6),
    /**
     * <pre>
     * 申请进入群组
     * </pre>
     *
     * <code>COMMAND_JOIN_GROUP_REQ = 7;</code>
     */
    COMMAND_JOIN_GROUP_REQ(7),
    /**
     * <pre>
     * 申请进入群组响应
     * </pre>
     *
     * <code>COMMAND_JOIN_GROUP_RESP = 8;</code>
     */
    COMMAND_JOIN_GROUP_RESP(8),
    /**
     * <pre>
     * 进入群组通知
     * </pre>
     *
     * <code>COMMAND_JOIN_GROUP_NOTIFY_RESP = 9;</code>
     */
    COMMAND_JOIN_GROUP_NOTIFY_RESP(9),
    /**
     * <pre>
     * 退出群组通知
     * </pre>
     *
     * <code>COMMAND_EXIT_GROUP_NOTIFY_RESP = 10;</code>
     */
    COMMAND_EXIT_GROUP_NOTIFY_RESP(10),
    /**
     * <pre>
     * 聊天请求
     * </pre>
     *
     * <code>COMMAND_CHAT_REQ = 11;</code>
     */
    COMMAND_CHAT_REQ(11),
    /**
     * <pre>
     * 聊天响应
     * </pre>
     *
     * <code>COMMAND_CHAT_RESP = 12;</code>
     */
    COMMAND_CHAT_RESP(12),
    /**
     * <pre>
     * 心跳请求
     * </pre>
     *
     * <code>COMMAND_HEARTBEAT_REQ = 13;</code>
     */
    COMMAND_HEARTBEAT_REQ(13),
    /**
     * <pre>
     * 关闭请求
     * </pre>
     *
     * <code>COMMAND_CLOSE_REQ = 14;</code>
     */
    COMMAND_CLOSE_REQ(14),
    /**
     * <pre>
     * 发出撤消消息指令(管理员可以撤消所有人的消息，自己可以撤消自己的消息)
     * </pre>
     *
     * <code>COMMAND_CANCEL_MSG_REQ = 15;</code>
     */
    COMMAND_CANCEL_MSG_REQ(15),
    /**
     * <pre>
     * 收到撤消消息指令
     * </pre>
     *
     * <code>COMMAND_CANCEL_MSG_RESP = 16;</code>
     */
    COMMAND_CANCEL_MSG_RESP(16),
    /**
     * <pre>
     * 获取用户信息;
     * </pre>
     *
     * <code>COMMAND_GET_USER_REQ = 17;</code>
     */
    COMMAND_GET_USER_REQ(17),
    /**
     * <pre>
     * 获取用户信息响应;
     * </pre>
     *
     * <code>COMMAND_GET_USER_RESP = 18;</code>
     */
    COMMAND_GET_USER_RESP(18),
    /**
     * <pre>
     * 获取聊天消息;
     * </pre>
     *
     * <code>COMMAND_GET_MESSAGE_REQ = 19;</code>
     */
    COMMAND_GET_MESSAGE_REQ(19),
    /**
     * <pre>
     * 获取聊天消息响应;
     * </pre>
     *
     * <code>COMMAND_GET_MESSAGE_RESP = 20;</code>
     */
    COMMAND_GET_MESSAGE_RESP(20),
    /**
     * <pre>
     * 用户上下线状态消息
     * </pre>
     *
     * <code>COMMAND_USER_STATUS_RESP = 21;</code>
     */
    COMMAND_USER_STATUS_RESP(21),
    /**
     * <pre>
     * 创建群组请求;
     * </pre>
     *
     * <code>COMMAND_CREATE_GROUP_REQ = 22;</code>
     */
    COMMAND_CREATE_GROUP_REQ(22),
    /**
     * <pre>
     * 创建群组响应;
     * </pre>
     * <code>COMMAND_CREATE_GROUP_RESP = 23;</code>
     */
    COMMAND_CREATE_GROUP_RESP(23),
    /**
     * <pre>
     * 消息已读请求;
     * </pre>
     * <code>COMMAND_MESSAGE_READ_REQ = 24;</code>
     */
    COMMAND_MESSAGE_READ_REQ(24),
    /**
     * <pre>
     * 消息已读响应;
     * </pre>
     * <code>COMMAND_MESSAGE_READ_RESP = 25;</code>
     */
    COMMAND_MESSAGE_READ_RESP(25),

    /**
     * <pre>
     * 获取当前所有用户请求;
     * </pre>
     * <code>COMMAND_USER_LIST_REQ = 26;</code>
     */
    COMMAND_USER_LIST_REQ(26),

    /**
     * <pre>
     * 获取当前所有用户响应;
     * </pre>
     * <code>COMMAND_USER_LIST_RESP = 27;</code>
     */
    COMMAND_USER_LIST_RESP(27),

    /**
     * <pre>
     * 发送表情回复;
     * </pre>
     * <code>COMMAND_SEND_MESSAGE_REACTION_REQ = 28;</code>
     */
    COMMAND_SEND_MESSAGE_REACTION_REQ(28),

    /**
     * <pre>
     * 发送表情回复响应;
     * </pre>
     * <code>COMMAND_SEND_MESSAGE_REACTION_RESP = 29;</code>
     */
    COMMAND_SEND_MESSAGE_REACTION_RESP(29),

    /**
     * <pre>
     * 修改资料请求
     * </pre>
     * <code>COMMAND_EDIT_PROFILE_REQ = 30</code>
     */
    COMMAND_EDIT_PROFILE_REQ(30),

    /**
     * <pre>
     * 修改资料响应
     * </pre>
     * <code>COMMAND_EDIT_PROFILE_REST = 31</code>
     */
    COMMAND_EDIT_PROFILE_RESP(31),

    /**
     * <pre>
     * 移除用户请求
     * </pre>
     * <code>COMMAND_REMOVE_GROUP_USER_REQ = 32</code>
     */
    COMMAND_REMOVE_GROUP_USER_REQ(32),

    /**
     * <pre>
     * 移除用户响应
     * </pre>
     * <code>COMMAND_REMOVE_GROUP_USER_RESP = 33</code>
     */
    COMMAND_REMOVE_GROUP_USER_RESP(33),

    // TODO 36 37
    /**
     * <pre>
     * 解散群聊请求
     * </pre>
     * <code>COMMAND_DISBAND_GROUP_REQ = 38</code>
     */
    COMMAND_DISBAND_GROUP_REQ(38),

    /**
     * <pre>
     * 解散群聊响应
     * </pre>
     * <code>COMMAND_DISBAND_GROUP_RESP = 39</code>
     */
    COMMAND_DISBAND_GROUP_RESP(39),

    /**
     * <pre>
     * 移交群组请求
     * </pre>
     * <code>COMMAND_HANDOVER_GROUP_REQ = 40</code>
     */
    COMMAND_HANDOVER_GROUP_REQ(40),

    /**
     * <pre>
     * 移交群组响应
     * </pre>
     * <code>COMMAND_HANDOVER_GROUP_RESP = 41</code>
     */
    COMMAND_HANDOVER_GROUP_RESP(41),

    /**
     * <pre>
     * 修改群组资料请求
     * </pre>
     * <code>COMMAND_EDIT_GROUP_PROFILE_REQ = 42</code>
     */
    COMMAND_EDIT_GROUP_PROFILE_REQ(42),

    /**
     * <pre>
     * 修改群组资料响应
     * </pre>
     * <code>COMMAND_EDIT_GROUP_PROFILE_RESP = 43</code>
     */
    COMMAND_EDIT_GROUP_PROFILE_RESP(43),

    /**
     * <pre>
     * 消息撤回请求
     * </pre>
     * <code>COMMAND_EDIT_GROUP_PROFILE_REQ = 42</code>
     */
    COMMAND_MESSAGE_DELETE_REQ(44),

    /**
     * <pre>
     * 消息撤回响应
     * </pre>
     * <code>COMMAND_EDIT_GROUP_PROFILE_RESP = 43</code>
     */
    COMMAND_MESSAGE_DELETE_RESP(45),

    /**
     * <pre>
     * 设置管理员请求
     * </pre>
     * <code>COMMAND_SET_ROOM_ADMIN_REQ = 46</code>
     */
    COMMAND_SET_ROOM_ADMIN_REQ(46),

    /**
     * <pre>
     * 设置管理员响应
     * </pre>
     * <code>COMMAND_SET_ROOM_ADMIN_RESP = 47</code>
     */
    COMMAND_SET_ROOM_ADMIN_RESP(47),

    /**
     * <pre>
     * 搜索消息
     * </pre>
     * <code>COMMAND_SEARCH_MESSAGE_REQ = 48</code>
     */
    COMMAND_SEARCH_MESSAGE_REQ(48),

    /**
     * <pre>
     *      * 搜索消息
     *      * </pre>
     *      * <code>COMMAND_SEARCH_MESSAGE_RESP = 49</code>
     */
    COMMAND_SEARCH_MESSAGE_RESP(49),

    /**
     * <pre>
     * 系统消息请求
     * </pre>
     * <code>COMMAND_SYSTEM_MESSAGE_REQ = 50</code>
     */
    COMMAND_SYSTEM_MESSAGE_REQ(50),

    /**
     * <pre>
     * 系统消息响应
     * </pre>
     * <code>COMMAND_SYSTEM_MESSAGE_RESP = 51</code>
     */
    COMMAND_SYSTEM_MESSAGE_RESP(51),

    /**
     * <pre>
     * 用户群组配置请求
     * </pre>
     * <code>COMMAND_USER_GROUP_CONFIG_REQ = 52</code>
     */
    COMMAND_USER_GROUP_CONFIG_REQ(52),

    /**
     * <pre>
     * 用户群组配置响应
     * </pre>
     * <code>COMMAND_USER_GROUP_CONFIG_RESP = 53</code>
     */
    COMMAND_USER_GROUP_CONFIG_RESP(53),

    /**
     * <pre>
     * 注册响应
     * </pre>
     * <code>COMMAND_REGISTER_RESP = 54</code>
     */
    COMMAND_REGISTER_RESP(54),

    /**
     * <pre>
     * 音视频通话请求
     * </pre>
     * <code>COMMAND_VIDEO_REQ = 55</code>
     */
    COMMAND_VIDEO_REQ(55),

    /**
     * <pre>
     * 音视频通话响应
     * </pre>
     * <code>COMMAND_VIDEO_RESP = 56</code>
     */
    COMMAND_VIDEO_RESP(56)

    ;

    private final int value;

    public static CommandEnum forNumber(int value) {
        for (CommandEnum command : CommandEnum.values()) {
            if (command.getValue() == value) {
                return command;
            }
        }
        return null;
    }
}
