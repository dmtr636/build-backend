package com.kydas.build.auth;

import com.kydas.build.core.endpoints.Endpoints;
import com.kydas.build.core.exceptions.classes.ApiException;
import com.kydas.build.core.response.OkResponse;
import com.kydas.build.users.UserDTO;
import com.kydas.build.users.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping(Endpoints.AUTH_ENDPOINT)
@Tag(name = "Сервис аутентификации")
public class AuthController {
    private final AuthService authService;
    private final UserMapper userMapper;

    @PostMapping("/login")
    @Operation(
        summary = "Вход в систему"
    )
    public UserDTO login(
        @Valid @RequestBody LoginRequest data, HttpServletRequest request, HttpServletResponse response
    ) throws ApiException {
        var user = authService.login(data, request, response);
        return userMapper.toDTO(user);
    }

    @PostMapping("/logout")
    @Operation(
        summary = "Выход из системы"
    )
    public OkResponse logout(HttpServletRequest request) throws ServletException {
        authService.logout(request);
        return new OkResponse();
    }
}
