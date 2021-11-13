package org.example.commond;

import lombok.extern.slf4j.Slf4j;
import org.example.commond.handler.*;
import org.example.commond.handler.message.ChatReqHandler;
import org.example.commond.handler.message.MessageReactionReqHandler;
import org.example.commond.handler.message.MessageReadReqHandler;
import org.example.commond.handler.message.MessageReqHandler;
import org.example.commond.handler.room.CreatGroupReqHandler;
import org.example.commond.handler.room.JoinGroupReqHandler;
import org.example.commond.handler.room.RemoveGroupUserReqHandler;
import org.example.enums.CommandEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class CommandManager {

    private static final Map<Integer, AbstractCmdHandler> handlerMap = new HashMap<>();

    static {
        try {
            registerCommand(new HeartbeatReqHandler());
            registerCommand(new LoginReqHandler());
            registerCommand(new UserReqHandler());
            registerCommand(new MessageReqHandler());
            registerCommand(new JoinGroupReqHandler());
            registerCommand(new ChatReqHandler());
            registerCommand(new CreatGroupReqHandler());
            registerCommand(new CloseReqHandler());
            registerCommand(new MessageReadReqHandler());
            registerCommand(new UserListHandler());
            registerCommand(new MessageReactionReqHandler());
            registerCommand(new EditProfileHandler());
            registerCommand(new RemoveGroupUserReqHandler());
        } catch (Exception e) {
            log.info("注册处理器失败");
        }

    }

    public static void registerCommand(AbstractCmdHandler imCommandHandler) {
        if (imCommandHandler == null || imCommandHandler.command() == null) {
            return;
        }
        int cmd_number = imCommandHandler.command().getValue();
        if (Objects.isNull(CommandEnum.forNumber(cmd_number))) {
            throw new RuntimeException("failed to register cmd handler, illegal cmd code:" + cmd_number + ",use Command.addAndGet () to add in the enumerated Command class!");
        }
        if (Objects.isNull(handlerMap.get(cmd_number))) {
            handlerMap.put(cmd_number, imCommandHandler);
        } else {
            throw new RuntimeException("cmd code:" + cmd_number + ",has been registered, please correct!");
        }
    }

    public static AbstractCmdHandler getCommand(CommandEnum command) {
        if (command == null) {
            throw new RuntimeException("不存在的包解析指令");
        }
        return handlerMap.get(command.getValue());
    }
}
