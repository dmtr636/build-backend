package com.kydas.build.core.exceptions.classes;

import org.springframework.http.HttpStatus;

public class UnknownDeviceException extends ApiException{
    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
