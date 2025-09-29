package com.kydas.build.notifications;

import com.kydas.build.core.exceptions.classes.ApiException;
import com.kydas.build.core.exceptions.classes.NotFoundException;
import com.kydas.build.core.security.SecurityContext;
import com.kydas.build.events.ActionType;
import com.kydas.build.events.EventPublisher;
import com.kydas.build.events.EventWebSocketDTO;
import com.kydas.build.notifications.entities.Notification;
import com.kydas.build.notifications.entities.NotificationRecipient;
import com.kydas.build.notifications.entities.NotificationRecipientKey;
import com.kydas.build.notifications.repositories.NotificationRecipientRepository;
import com.kydas.build.notifications.repositories.NotificationRepository;
import com.kydas.build.projects.entities.Project;
import com.kydas.build.projects.entities.ProjectUser;
import com.kydas.build.users.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationRecipientRepository recipientRepository;
    private final SecurityContext securityContext;
    private final EventPublisher eventPublisher;
    private final NotificationMapper mapper;

    @Transactional
    public void create(Project project, NotificationType type, UUID objectId, String content, User author) throws ApiException {
        var notification = new Notification()
                .setProject(project)
                .setType(type)
                .setObjectId(objectId)
                .setContent(content)
                .setAuthor(author);
        notificationRepository.save(notification);

        var recipients = project.getProjectUsers().stream()
                .map(ProjectUser::getUser)
                .filter(u -> !u.getId().equals(author.getId()))
                .map(u -> new NotificationRecipient()
                        .setId(new NotificationRecipientKey(notification.getId(), u.getId()))
                        .setNotification(notification)
                        .setUser(u)
                        .setRead(false))
                .toList();
        recipientRepository.saveAll(recipients);

        eventPublisher.publish(
                "notification",
                EventWebSocketDTO.Type.CREATE,
                ActionType.WORK,
                Map.of(
                        "id", notification.getId(),
                        "projectId", project.getId(),
                        "type", type,
                        "content", content,
                        "objectId", objectId
                )
        );
    }

    @Transactional
    public void create(Project project, NotificationType type, UUID objectId, String content) throws ApiException {
        create(project, type, objectId, content, securityContext.getCurrentUser());
    }

    public List<NotificationDTO> getUserNotificationsForCurrentUser() throws ApiException {
        UUID userId = securityContext.getCurrentUser().getId();
        return recipientRepository.findByUserIdOrderByNotificationCreatedAtDesc(userId).stream()
                .map(this::toDTO)
                .toList();
    }

    public List<NotificationDTO> getUnreadNotificationsForCurrentUser() throws ApiException {
        UUID userId = securityContext.getCurrentUser().getId();
        return recipientRepository.findByUserIdAndReadFalseOrderByNotificationCreatedAtDesc(userId).stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional
    public void markAsRead(UUID notificationId) throws ApiException {
        UUID userId = securityContext.getCurrentUser().getId();
        var recipient = recipientRepository.findById(new NotificationRecipientKey(notificationId, userId))
                .orElseThrow(() -> new NotFoundException().setMessage("Notification not found for this user"));

        recipient.setRead(true);
        recipientRepository.save(recipient);
    }

    @Transactional(readOnly = true)
    public List<NotificationDTO> getUnreadNotificationsByTypeForCurrentUser(UUID projectId, NotificationType type) throws ApiException {
        UUID userId = securityContext.getCurrentUser().getId();
        return recipientRepository.findByUserIdAndReadFalseAndNotificationProjectIdAndNotificationTypeOrderByNotificationCreatedAtDesc(userId, projectId, type)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public void delete(UUID notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    private NotificationDTO toDTO(NotificationRecipient recipient) {
        var dto = mapper.toDTO(recipient.getNotification());
        dto.setRead(recipient.isRead());
        return dto;
    }
}
