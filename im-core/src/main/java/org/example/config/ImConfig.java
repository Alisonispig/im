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

    /**
     * 群组绑定监听器
     */
    protected ImGroupListener imGroupListener;
    /**
     * 用户绑定监听器
     */
    protected ImUserListener imUserListener;

    public static class Global{

        private static ImConfig global;

        public static <C extends ImConfig> C get(){
            return (C) global;
        }

        public static <C extends ImConfig> C set(C c){
            global = (ImConfig) c;
            return (C) global;
        }
    }
}
