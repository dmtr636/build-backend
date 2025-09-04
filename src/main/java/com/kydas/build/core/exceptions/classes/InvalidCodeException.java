package com.kydas.build.core.exceptions.classes;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Map;

@NoArgsConstructor
public class InvalidCodeException extends ApiException {
    public InvalidCodeException(Integer remainingAttempts) {
        this.setData(Map.of("enterAttemptsLeft", remainingAttempts));
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
