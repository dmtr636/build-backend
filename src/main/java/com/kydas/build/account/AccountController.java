package com.kydas.build.account;

import com.kydas.build.core.exceptions.classes.AlreadyExistsException;
import com.kydas.build.core.exceptions.classes.ApiException;
import com.kydas.build.core.exceptions.classes.ForbiddenException;
import com.kydas.build.core.response.OkResponse;
import com.kydas.build.core.security.SecurityContext;
import com.kydas.build.events.EventWebSocketController;
import com.kydas.build.events.EventWebSocketDTO;
import com.kydas.build.users.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Objects;

import static com.kydas.build.core.endpoints.Endpoints.ACCOUNT_ENDPOINT;

@RestController
@RequiredArgsConstructor
@RequestMapping(ACCOUNT_ENDPOINT)
@Tag(name = "Сервис аккаунта пользователя")
public class AccountController {
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    private final UserMapper userMapper;
    private final SecurityContext securityContext;
    private final UserService userService;
    private final EventWebSocketController eventWebSocketController;
    private final UserRepository userRepository;
    private final RememberMeServices rememberMeServices;
    private final PasswordEncoder passwordEncoder;

    @GetMapping()
    @Operation(
        summary = "Получение информации о текущем пользователе"
    )
    public UserDTO currentUser(HttpServletRequest request, HttpServletResponse response) throws ApiException {
        String REMEMBER_ME_COOKIE_NAME = AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (REMEMBER_ME_COOKIE_NAME.equals(cookie.getName())) {
                    rememberMeServices.loginSuccess(request, response, securityContext.getAuthentication());
                    break;
                }
            }
        }
        var currentUser = securityContext.getCurrentUser();
        return userMapper.toDTO(currentUser);
    }

    @PutMapping
    @Operation(
        summary = "Обновление информации о текущем пользователе"
    )
    public UserDTO update(@RequestBody @Valid UserDTO userDTO) throws ApiException {
        var user = securityContext.getCurrentUser();
        if (!user.getLogin().equals(userDTO.getLogin()) && userRepository.existsByLogin(userDTO.getLogin())) {
            throw new AlreadyExistsException().setMessage("This login is already in use");
        }
        if (!Objects.equals(user.getRole(), userDTO.getRole().name())) {
            throw new ForbiddenException().setMessage("Changing role is forbidden");
        }
        user = userMapper.update(user, userDTO);
        user = userService.save(user);
        eventWebSocketController.notifyObjectChange(user, new EventWebSocketDTO()
            .setType(EventWebSocketDTO.Type.UPDATE)
            .setObjectName("user")
            .setData(userMapper.toDTO(user))
        );
        return userMapper.toDTO(user);
    }

    @PostMapping("/changePassword")
    @Operation(
        summary = "Смена пароля для текущего пользователя"
    )
    public OkResponse changePassword(@RequestBody @Valid ChangePasswordDTO dto) throws ApiException {
        var user = securityContext.getCurrentUser();
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userService.save(user);
        return new OkResponse();
    }

    @DeleteMapping
    @Transactional
    @Operation(
        summary = "Удаление текущего пользователя"
    )
    public OkResponse delete() throws ApiException {
        var user = securityContext.getCurrentUser();
        if (Objects.equals(user.getRole(), User.Role.ROOT.name())) {
            throw new ForbiddenException().setMessage("Root user cannot be deleted");
        }
        userService.delete(user.getId());
        return new OkResponse();
    }

    @Data
    public static class ChangePasswordDTO {
        @NotBlank
        private String password;
    }
}
