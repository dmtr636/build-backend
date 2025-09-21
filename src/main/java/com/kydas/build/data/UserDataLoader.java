package com.kydas.build.data;

import com.kydas.build.core.exceptions.classes.ApiException;
import com.kydas.build.core.utils.JsonUtils;
import com.kydas.build.users.User;
import com.kydas.build.users.UserDTO;
import com.kydas.build.users.UserRepository;
import com.kydas.build.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class UserDataLoader {
    private final UserService userService;
    private final UserRepository userRepository;

    @Value("${root-user-name}")
    private String rootUserEmail;

    @Value("${root-user-password}")
    private String rootUserPassword;

    @Value("classpath:data/users.json")
    private Resource userDataFile;

    public boolean isRootUserExists() {
        return userRepository.existsByLogin(rootUserEmail);
    }

    public void createUsers() throws ApiException, IOException {
        createRootUser();
        var users = JsonUtils.readJson(userDataFile, UserDTO[].class);
        for (UserDTO user : users) {
            user.setPassword(user.getPassword().replace("{password}", rootUserPassword));
            user.setLogin(user.getLogin());
            userService.create(user);
        }
    }

    private void createRootUser() throws ApiException {
        userService.create(new UserDTO()
            .setLogin(rootUserEmail)
            .setEmail(rootUserEmail)
            .setPassword(rootUserPassword)
            .setRole(User.Role.ROOT)
        );
    }
}
