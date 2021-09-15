package org.example.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommandEnum {
    COMMAND_UN_KNOW(0),
    /**
     * 握手请求
     */
    COMMAND_HANDSHAKE_REQ(1),

    /**
     * 握手相应
     */
    COMMAND_HANDSHAKE_RESP(2),

    /**
     * 登录请求
     */
    COMMAND_LOGIN_REQ(5),

    /**
     * 登录响应
     */
    COMMAND_LOGIN_RESP(6)
    ;


    private final int value;

    public static CommandEnum forNumber(int value) {
        for(CommandEnum command : CommandEnum.values()){
            if(command.getValue() == value){
                return command;
            }
        }
        return null;
    }
}
