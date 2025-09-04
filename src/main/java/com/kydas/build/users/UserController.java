package com.kydas.build.users;

import com.kydas.build.core.crud.BaseController;
import com.kydas.build.core.endpoints.Endpoints;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Endpoints.USERS_ENDPOINT)
@Tag(name = "Сервис пользователей")
public class UserController extends BaseController<User, UserDTO> {

}
