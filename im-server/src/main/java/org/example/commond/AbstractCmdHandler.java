package org.example.commond;

import org.example.config.Im;
import org.example.store.MessageHelper;

public abstract class AbstractCmdHandler implements CmdHandler {

    public MessageHelper messageHelper = Im.get().messageHelper;

}
