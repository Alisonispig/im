package org.example.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum KeyEnum {

    IM_CHANNEL_CONTEXT_KEY("im_channel_context_key"),
    IM_CHANNEL_SESSION_CONTEXT_KEY("im_channel_session_context_key");

    private final String key;
}
