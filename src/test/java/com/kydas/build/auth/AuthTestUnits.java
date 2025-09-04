package com.kydas.build.auth;

import com.kydas.build.auth.LoginRequest;

public class AuthTestUnits {
    public static LoginRequest getLoginRequest(String email, String password) {
        return new LoginRequest()
            .setEmail(email)
            .setPassword(password);
    }
}
