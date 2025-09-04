package com.kydas.build.core.security;

import com.kydas.build.core.exceptions.classes.ApiException;
import com.kydas.build.core.exceptions.classes.AuthenticationException;
import com.kydas.build.users.User;
import com.kydas.build.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityContext {
    private final UserRepository userRepository;
    
    public User getCurrentUser() throws ApiException {
        try {
            var userDetails = (UserDetailsImpl) getAuthentication().getPrincipal();
            return userRepository.findById(userDetails.getUser().getId()).orElseThrow(AuthenticationException::new);
        } catch (ClassCastException e) {
            throw new AuthenticationException();
        }
    }

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public Boolean isAuthenticated() {
        if (getAuthentication() == null) {
            return false;
        }
        try {
            getCurrentUser();
            return true;
        } catch (ApiException e) {
            return false;
        }
    }

    public void authenticated() throws ApiException {
        if (!isAuthenticated()) {
            throw new AuthenticationException();
        }
    }
}
