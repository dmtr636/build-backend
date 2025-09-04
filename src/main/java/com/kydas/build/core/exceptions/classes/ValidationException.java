package com.kydas.build.core.exceptions.classes;

import org.springframework.http.HttpStatus;

public class ValidationException extends ApiException {
    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
