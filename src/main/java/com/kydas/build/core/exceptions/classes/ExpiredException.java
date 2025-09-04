package com.kydas.build.core.exceptions.classes;

import org.springframework.http.HttpStatus;

public class ExpiredException extends ApiException {
    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.GONE;
    }
}
