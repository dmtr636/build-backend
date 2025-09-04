package com.kydas.build.core.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.net.HttpCookie;
import java.util.Arrays;
import java.util.Optional;

@Component
public class RequestUtils {
    private static final String SET_COOKIE_HEADER_NAME = "Set-Cookie";

    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress.split(":")[0];
    }

    public static Optional<HttpCookie> getCookie(ResponseEntity<?> responseEntity, String cookieName) {
        var cookieHeaders = responseEntity.getHeaders().getOrEmpty(SET_COOKIE_HEADER_NAME);

        var sessionCookieHeader = cookieHeaders.stream().filter(
            header -> header.contains(cookieName)
        ).findFirst();

        return sessionCookieHeader.map(header ->
            HttpCookie.parse(header).get(0)
        );
    }

    public static Optional<Cookie> getCookie(HttpServletRequest request, String cookieName) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }
        return Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals(cookieName)).findFirst();
    }
}