package com.kydas.build.core.exceptions.classes;

import org.springframework.http.HttpStatus;

public class InternalServerError extends ApiException {
    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
