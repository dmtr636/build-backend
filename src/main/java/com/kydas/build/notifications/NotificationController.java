package com.kydas.build.notifications;

import com.kydas.build.core.endpoints.Endpoints;
import com.kydas.build.core.exceptions.classes.ApiException;
import com.kydas.build.core.response.OkResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(Endpoints.NOTIFICATIONS)
@RequiredArgsConstructor
@Tag(name = "Сервис уведомлений")
public class NotificationController {

    private final NotificationService service;

    @GetMapping
    public List<NotificationDTO> getUserNotifications() throws ApiException {
        return service.getUserNotificationsForCurrentUser();
    }

    @GetMapping("/unread")
    public List<NotificationDTO> getUnread() throws ApiException {
        return service.getUnreadNotificationsForCurrentUser();
    }

    @GetMapping("/unread/{projectId}")
    public List<NotificationDTO> getNotificationsByType(@PathVariable UUID projectId,
                                                        @RequestParam NotificationType type) throws ApiException {
        return service.getUnreadNotificationsByTypeForCurrentUser(projectId, type);
    }

    @PatchMapping("/{id}/read")
    public OkResponse markAsRead(@PathVariable UUID id) throws ApiException {
        service.markAsRead(id);
        return new OkResponse();
    }

    @DeleteMapping("/{id}")
    public OkResponse delete(@PathVariable UUID id) {
        service.delete(id);
        return new OkResponse();
    }
}
