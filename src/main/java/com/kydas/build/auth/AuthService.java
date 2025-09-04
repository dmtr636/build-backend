package com.kydas.build.auth;

import com.kydas.build.auth.login.LoginAttemptService;
import com.kydas.build.core.exceptions.classes.ApiException;
import com.kydas.build.core.security.SecurityContext;
import com.kydas.build.users.User;
import com.kydas.build.users.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AuthService {
    private final RememberMeServices rememberMeServices;
    private final SecurityContext securityContext;
    private final LoginAttemptService loginAttemptService;
    private final UserService userService;

    public AuthService(RememberMeServices rememberMeServices, SecurityContext securityContext,
                       LoginAttemptService loginAttemptService, UserService userService) {
        this.rememberMeServices = rememberMeServices;
        this.securityContext = securityContext;
        this.loginAttemptService = loginAttemptService;
        this.userService = userService;
    }

    public User login(LoginRequest data, HttpServletRequest request, HttpServletResponse response) throws ApiException {
        authenticateUser(data.getLogin(), data.getPassword(), request);
        if (data.getRememberMe()) {
            rememberMeServices.loginSuccess(request, response, securityContext.getAuthentication());
        }
        return securityContext.getCurrentUser();
    }

    public void logout(HttpServletRequest request) throws ServletException {
        request.logout();
    }

    private void authenticateUser(String login, String password, HttpServletRequest request) throws ApiException {
        var loginAttempt = loginAttemptService.getOrCreateLoginAttempt(request);
        loginAttemptService.assertAttemptAvailable(loginAttempt);
        try {
            request.logout();
            request.login(login, password);
            loginAttemptService.handleSuccessfulAttempt(loginAttempt);
        } catch (ServletException e) {
            loginAttemptService.handleFailedAttempt(loginAttempt);
        }
    }
}
