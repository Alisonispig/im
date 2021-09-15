package org.example.commond;

import org.example.commond.handler.HeartbeatReqHandler;
import org.example.commond.handler.LoginReqHandler;
import org.example.enums.CommandEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CommandManager {

    private static Map<Integer, AbstractCmdHandler> handlerMap = new HashMap<>();

    static {
        try {
            registerCommand(new HeartbeatReqHandler());
            registerCommand(new LoginReqHandler());
        } catch (Exception e) {

        }
    }

    public static AbstractCmdHandler registerCommand(AbstractCmdHandler imCommandHandler) throws Exception {
        if (imCommandHandler == null || imCommandHandler.command() == null) {
            return null;
        }
        int cmd_number = imCommandHandler.command().getValue();
        if (Objects.isNull(CommandEnum.forNumber(cmd_number))) {
            throw new RuntimeException("failed to register cmd handler, illegal cmd code:" + cmd_number + ",use Command.addAndGet () to add in the enumerated Command class!");
        }
        if (Objects.isNull(handlerMap.get(cmd_number))) {
            return handlerMap.put(cmd_number, imCommandHandler);
        } else {
            throw new RuntimeException("cmd code:" + cmd_number + ",has been registered, please correct!");
        }
    }

    public static <T> T getCommand(CommandEnum command, Class<T> clazz) {
        AbstractCmdHandler cmdHandler = getCommand(command);
        if (cmdHandler != null) {
            return (T) cmdHandler;
        }
        return null;
    }

    public static AbstractCmdHandler getCommand(CommandEnum command) {
        if (command == null) {
            return null;
        }
        return handlerMap.get(command.getValue());
    }
}
