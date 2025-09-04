package com.kydas.build.security;

import com.kydas.build.core.endpoints.Endpoints;
import com.kydas.build.core.utils.RequestUtils;
import com.kydas.build.users.User;
import com.kydas.build.users.UserMapper;
import com.kydas.build.users.UserRepository;
import com.kydas.build.users.UserDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Component;

import java.net.HttpCookie;
import java.util.Collections;
import java.util.Optional;

import static com.kydas.build.auth.AuthTestUnits.getLoginRequest;

@Getter
@Component
@RequiredArgsConstructor
public class SecurityTestUtils {
    private final UserMapper userMapper;
    private static final String SESSION_COOKIE_NAME = "JSESSIONID";
    private static final String SET_COOKIE_HEADER_NAME = "Set-Cookie";
    private static final String COOKIE_HEADER_NAME = "Cookie";

    @Autowired
    private UserRepository userRepository;

    @Value("${root-user-name}")
    private String rootUserLogin;

    @Value("${root-user-password}")
    private String rootUserPassword;

    public void authenticateRestTemplateAsRootUser(TestRestTemplate restTemplate) {
        authenticateRestTemplate(restTemplate, rootUserLogin, rootUserPassword);
    }

    public void authenticateRestTemplate(TestRestTemplate restTemplate, String email, String password) {
        var loginResponse = restTemplate.postForEntity(
            Endpoints.AUTH_ENDPOINT + "/login",
            getLoginRequest(email, password),
            UserDTO.class
        );
        var httpCookie = getSessionCookie(loginResponse).orElseThrow(() ->
            new AssertionError("No session cookie in response")
        );
        setCookieToRestTemplate(restTemplate, httpCookie);
    }

    public Optional<HttpCookie> getSessionCookie(ResponseEntity<?> responseEntity) {
        return RequestUtils.getCookie(responseEntity, SESSION_COOKIE_NAME);
    }

    public void setCookieToRestTemplate(TestRestTemplate restTemplate, HttpCookie httpCookie) {
        ClientHttpRequestInterceptor interceptor = (request, body, execution) -> {
            request.getHeaders().add(COOKIE_HEADER_NAME, httpCookie.toString());
            return execution.execute(request, body);
        };
        restTemplate.getRestTemplate().setInterceptors(Collections.singletonList(interceptor));
    }

    public void removeCookiesFromRestTemplate(TestRestTemplate restTemplate) {
        restTemplate.getRestTemplate().setInterceptors(Collections.emptyList());
    }

    public UserDTO getRootUserDTO() {
        return getUserDTOByLogin(rootUserLogin);
    }

    public UserDTO getUserDTOByLogin(String email) {
        return userMapper.toDTO(getUserByLogin(email));
    }

    private User getUserByLogin(String login) {
        return userRepository.findByEmail(login).orElseThrow(() ->
            new AssertionError("User not found")
        );
    }
}
