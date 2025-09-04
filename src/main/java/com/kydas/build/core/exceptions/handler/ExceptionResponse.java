package com.kydas.build.core.exceptions.handler;

import com.kydas.build.core.exceptions.classes.ApiException;
import com.kydas.build.core.exceptions.classes.ForbiddenException;
import com.kydas.build.core.exceptions.classes.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse {

    private ErrorDetails error;

    public ExceptionResponse(ApiException e) {
        this.error = new ErrorDetails()
            .setCode(e.getCode())
            .setData(e.getData())
            .setMessage(e.getMessage());
    }

    public ExceptionResponse(MethodArgumentNotValidException e) {
        Map<String, Object> data = new HashMap<>();
        e.getFieldErrors().forEach(fieldError -> data.put(fieldError.getField(), fieldError.getCode()));
        this.error = new ErrorDetails()
            .setCode("ValidationError")
            .setData(data)
            .setMessage(e.getMessage());
    }

    public ExceptionResponse(NoSuchElementException e) {
        this.error = new ErrorDetails().setCode("NotFound");
    }

    public ExceptionResponse(AuthenticationException e) {
        this.error = new ErrorDetails()
            .setCode(AuthenticationException.class.getSimpleName())
            .setMessage(e.getMessage());
    }

    public ExceptionResponse(NoHandlerFoundException e) {
        this.error = new ErrorDetails()
            .setCode(NotFoundException.class.getSimpleName())
            .setMessage(e.getMessage());
    }

    public ExceptionResponse(AccessDeniedException e) {
        this.error = new ErrorDetails()
            .setCode(ForbiddenException.class.getSimpleName())
            .setMessage(e.getMessage());
    }

    public ExceptionResponse(Exception e) {
        var sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        this.error = new ErrorDetails()
            .setCode("UnknownError")
            .setMessage(e.getMessage())
            .setStackTrace(StringUtils.abbreviate(sw.toString(), 500));
    }
}
