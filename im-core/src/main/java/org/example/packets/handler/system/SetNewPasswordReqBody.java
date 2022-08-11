package org.example.packets.handler.system;

import lombok.Data;

@Data
public class SetNewPasswordReqBody {

    private String oldPassword;

    private String password;

    private String repeatPassword;
}
