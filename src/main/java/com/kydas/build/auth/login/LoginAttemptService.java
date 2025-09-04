package com.kydas.build.auth.login;

import com.kydas.build.core.exceptions.classes.ApiException;
import com.kydas.build.core.exceptions.classes.LoginFailedException;
import com.kydas.build.core.exceptions.classes.ThrottledException;
import com.kydas.build.core.utils.RequestUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class LoginAttemptService {
    private final LoginAttemptRepository loginAttemptRepository;

    public final static int INITIAL_ATTEMPTS = 6;
    public final static int ATTEMPTS_AFTER_BAN = 3;
    public final static int SHORT_BAN_DURATION_SECONDS = 300;
    public final static int LONG_BAN_DURATION_SECONDS = 3600;

    public LoginAttempt getOrCreateLoginAttempt(HttpServletRequest request) {
        var ipAddress = RequestUtils.getIpAddress(request);
        var loginAttempt = loginAttemptRepository.findByIp(ipAddress).orElseGet(() ->
            new LoginAttempt(ipAddress)
        );
        checkLastLogin(loginAttempt);
        loginAttempt.setLastLoginAttempt(LocalDateTime.now());
        return loginAttemptRepository.save(loginAttempt);
    }

    public void handleSuccessfulAttempt(LoginAttempt loginAttempt) throws ServletException {
        loginAttemptRepository.delete(loginAttempt);
    }

    public void handleFailedAttempt(LoginAttempt loginAttempt) throws ApiException {
        incrementAttempts(loginAttempt);
        var remainingAttempts = getRemainingAttempts(loginAttempt);

        if (remainingAttempts == 0) {
            setBan(loginAttempt);
            throw new ThrottledException(getBanDurationSeconds(loginAttempt));
        }

        throw new LoginFailedException(remainingAttempts);
    }

    public void assertAttemptAvailable(LoginAttempt loginAttempt) throws ApiException {
        if (isBanned(loginAttempt)) {
            throw new ThrottledException(getBanDurationSeconds(loginAttempt));
        }
    }

    private void checkLastLogin(LoginAttempt loginAttempt) {
        var now = LocalDateTime.now();
        var secondsSinceLastLogin = ChronoUnit.SECONDS.between(loginAttempt.getLastLoginAttempt(), now);

        if (secondsSinceLastLogin >= SHORT_BAN_DURATION_SECONDS) {
            if (loginAttempt.getAttempts() < INITIAL_ATTEMPTS) {
                loginAttempt.setAttempts(0);
            } else if (loginAttempt.getAttempts() % ATTEMPTS_AFTER_BAN != 0) {
                loginAttempt.setAttempts(INITIAL_ATTEMPTS);
            }
        }
    }

    private boolean isBanned(LoginAttempt loginAttempt) {
        var banExpirationTime = loginAttempt.getBanExpirationTime();
        return banExpirationTime != null && banExpirationTime.isAfter(LocalDateTime.now());
    }

    private void incrementAttempts(LoginAttempt loginAttempt) {
        var attempts = loginAttempt.getAttempts();
        loginAttempt.setAttempts(++attempts);
        loginAttemptRepository.save(loginAttempt);
    }

    private void setBan(LoginAttempt loginAttempt) {
        var attempts = loginAttempt.getAttempts();
        if (attempts == INITIAL_ATTEMPTS) {
            loginAttempt.setBanExpirationTime(LocalDateTime.now().plusSeconds(SHORT_BAN_DURATION_SECONDS));
        } else if (attempts != ATTEMPTS_AFTER_BAN && attempts % ATTEMPTS_AFTER_BAN == 0) {
            loginAttempt.setBanExpirationTime(LocalDateTime.now().plusSeconds(LONG_BAN_DURATION_SECONDS));
        }
        loginAttemptRepository.save(loginAttempt);
    }

    private int getRemainingAttempts(LoginAttempt loginAttempt) {
        var attempts = loginAttempt.getAttempts();
        if (attempts <= INITIAL_ATTEMPTS) {
            return INITIAL_ATTEMPTS - attempts;
        } else if (attempts % ATTEMPTS_AFTER_BAN == 0) {
            return 0;
        } else {
            return ATTEMPTS_AFTER_BAN - (attempts % ATTEMPTS_AFTER_BAN);
        }
    }

    private int getBanDurationSeconds(LoginAttempt loginAttempt) {
        var banExpirationTime = loginAttempt.getBanExpirationTime();
        if (banExpirationTime != null) {
            var remainingSeconds = (ChronoUnit.MILLIS.between(LocalDateTime.now(), banExpirationTime) / 1000.0);
            return (int) Math.max(0, Math.round(remainingSeconds));
        }
        return 0;
    }
}