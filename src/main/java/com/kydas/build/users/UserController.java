package com.kydas.build.users;

import com.kydas.build.core.crud.BaseController;
import com.kydas.build.core.endpoints.Endpoints;
import com.kydas.build.core.response.OkResponse;
import com.kydas.build.core.utils.DateUtils;
import com.kydas.build.storage.Storage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(Endpoints.USERS_ENDPOINT)
@Tag(name = "Сервис пользователей")
public class UserController extends BaseController<User, UserDTO> {
    @Autowired
    private Storage storage;

    @GetMapping("/status")
    @Operation(summary = "Статусы пользователей")
    public Map<UUID, Map<String, String>> getStatus() {
        return storage.userStatus;
    }

    @PutMapping("/{id}/status/{status}")
    @Operation(summary = "Обновление статуса пользователя")
    public OkResponse updateStatus(@PathVariable UUID id, @PathVariable String status) {
        storage.userStatus.put(id, Map.of(
            "status", status,
            "date", DateTimeFormatter.ofPattern(DateUtils.ISO_DATE_TIME_FORMAT).withZone(ZoneOffset.UTC).format(Instant.now())
        ));
        return new OkResponse();
    }
}
