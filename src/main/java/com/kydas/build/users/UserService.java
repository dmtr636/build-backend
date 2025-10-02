package com.kydas.build.users;

import com.kydas.build.core.crud.BaseService;
import com.kydas.build.core.email.EmailService;
import com.kydas.build.core.exceptions.classes.AlreadyExistsException;
import com.kydas.build.core.exceptions.classes.ApiException;
import com.kydas.build.core.exceptions.classes.NotFoundException;
import com.kydas.build.core.security.SecurityContext;
import com.kydas.build.core.utils.PasswordUtils;
import com.kydas.build.events.EventDTO;
import com.kydas.build.events.EventService;
import com.kydas.build.events.EventWebSocketController;
import com.kydas.build.events.EventWebSocketDTO;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class UserService extends BaseService<User, UserDTO> {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final SecurityContext securityContext;
    private final EventService eventService;
    private final EventWebSocketController eventWebSocketController;
    private final EmailService emailService;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       UserMapper userMapper,
                       SecurityContext securityContext,
                       EventService eventService,
                       EventWebSocketController eventWebSocketController,
                       EmailService emailService) {
        super(User.class);
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.securityContext = securityContext;
        this.eventService = eventService;
        this.eventWebSocketController = eventWebSocketController;
        this.emailService = emailService;
    }

    @Override
    public User makeEntity(UserDTO userDTO) throws ApiException {
        if (userRepository.existsByLogin(userDTO.getLogin())) {
            throw new AlreadyExistsException();
        }
        var user = new User();
        user = userMapper.update(user, userDTO);
        var password = userDTO.getPassword();
        if (password == null) {
            password = PasswordUtils.generate(12);
            emailService.sendEmail(
                    userDTO.getEmail(),
                    "Ваш аккаунт на build.kydas.ru создан",
                    String.format("""
                            Здравствуйте!
                            
                            Ваш аккаунт был создан на платформе build.kydas.ru.
                            Ваш пароль: %s
                            
                            Ссылка для входа: https://build.kydas.ru/
                            
                            С уважением,
                            команда Kydas
                            """, password)
            );
        }
        user.setPassword(passwordEncoder.encode(password));
        return user;
    }

    @Override
    public User create(UserDTO userDTO) throws ApiException {
        var user = makeEntity(userDTO);
        var createdUser = userRepository.save(user);
        if (securityContext.isAuthenticated()) {
            eventService.create(new EventDTO()
                .setUserId(securityContext.getCurrentUser().getId())
                .setAction("create")
                .setActionType("system")
                .setObjectName("user")
                .setObjectId(String.valueOf(createdUser.getId()))
                .setInfo(Map.of("login", userDTO.getLogin()))
            );
        }
        eventWebSocketController.notifyObjectChange(new EventWebSocketDTO()
                .setType(EventWebSocketDTO.Type.CREATE)
                .setObjectName("user")
                .setData(userMapper.toDTO(createdUser))
        );
        return createdUser;
    }

    @Override
    public User update(UserDTO userDTO) throws ApiException {
        var user = userRepository.findById(userDTO.getId()).orElseThrow(NotFoundException::new);
        userMapper.update(user, userDTO);
        var updatedUser = userRepository.save(user);
        eventService.create(new EventDTO()
                .setUserId(securityContext.getCurrentUser().getId())
                .setAction("update")
                .setActionType("system")
                .setObjectName("user")
                .setObjectId(String.valueOf(updatedUser.getId()))
                .setInfo(Map.of("login", userDTO.getLogin()))
        );
        eventWebSocketController.notifyObjectChange(new EventWebSocketDTO()
                .setType(EventWebSocketDTO.Type.UPDATE)
                .setObjectName("user")
                .setData(userMapper.toDTO(updatedUser))
        );
        return updatedUser;
    }

    @Transactional
    @Override
    public void delete(UUID id) throws ApiException {
        var user = userRepository.findById(id).orElseThrow(NotFoundException::new);
        eventService.create(new EventDTO()
                .setUserId(securityContext.getCurrentUser().getId())
                .setAction("delete")
                .setActionType("system")
                .setObjectName("user")
                .setObjectId(String.valueOf(id))
                .setInfo(Map.of("login", user.getLogin()))
        );
        eventWebSocketController.notifyObjectChange(new EventWebSocketDTO()
                .setType(EventWebSocketDTO.Type.DELETE)
                .setObjectName("user")
                .setData(userMapper.toDTO(user))
        );
        userRepository.delete(user);
    }
}
