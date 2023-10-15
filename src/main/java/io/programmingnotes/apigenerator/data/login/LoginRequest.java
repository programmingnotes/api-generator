package io.programmingnotes.apigenerator.data.login;

import lombok.Getter;
import lombok.Setter;

public class LoginRequest {
    @Setter @Getter
    private String username;

    @Getter @Setter
    private String password;


}