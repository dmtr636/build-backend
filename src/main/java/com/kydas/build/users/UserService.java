package com.kydas.build.users;

import com.kydas.build.core.crud.BaseService;
import com.kydas.build.core.exceptions.classes.AlreadyExistsException;
import com.kydas.build.core.exceptions.classes.ApiException;
import com.kydas.build.core.exceptions.classes.NotFoundException;
import com.kydas.build.core.security.SecurityContext;
import com.kydas.build.events.EventDTO;
import com.kydas.build.events.EventService;
import com.kydas.build.events.EventWebSocketController;
import com.kydas.build.events.EventWebSocketDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService extends BaseService<User, UserDTO> {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SecurityContext securityContext;
    @Autowired
    private EventService eventService;
    @Autowired
    private EventWebSocketController eventWebSocketController;

    public UserService() {
        super(User.class);
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
            password = UUID.randomUUID().toString();
        }
        user.setPassword(passwordEncoder.encode(password));
        return user;
    }

    @Override
    public User create(UserDTO userDTO) throws ApiException {
        var user = makeEntity(userDTO);
        var createdUser = userRepository.save(user);
        eventService.create(new EventDTO()
            .setUserId(securityContext.getCurrentUser().getId())
            .setAction("create")
            .setActionType("system")
            .setObjectName("user")
            .setObjectId(String.valueOf(createdUser.getId()))
        );
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
            .setAction("create")
            .setActionType("system")
            .setObjectName("user")
            .setObjectId(String.valueOf(updatedUser.getId()))
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
        );
        eventWebSocketController.notifyObjectChange(new EventWebSocketDTO()
            .setType(EventWebSocketDTO.Type.DELETE)
            .setObjectName("user")
            .setData(userMapper.toDTO(user))
        );
        userRepository.delete(user);
    }
}
