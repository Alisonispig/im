package org.example.config;

import lombok.Data;
import org.example.listener.ImGroupListener;
import org.example.listener.ImUserListener;
import org.tio.core.TioConfig;
import org.tio.utils.prop.MapWithLockPropSupport;

@Data
public abstract class ImConfig extends MapWithLockPropSupport {

    public static final String CHARSET = "utf-8";

    protected TioConfig tioConfig;
}
