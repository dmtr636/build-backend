package com.kydas.build.notifications.repositories;

import com.kydas.build.notifications.NotificationType;
import com.kydas.build.notifications.entities.NotificationRecipient;
import com.kydas.build.notifications.entities.NotificationRecipientKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationRecipientRepository extends JpaRepository<NotificationRecipient, NotificationRecipientKey> {
    List<NotificationRecipient> findByUserIdOrderByNotificationCreatedAtDesc(UUID userId);

    List<NotificationRecipient> findByUserIdAndReadFalseOrderByNotificationCreatedAtDesc(UUID userId);

    List<NotificationRecipient> findByUserIdAndReadFalseAndNotificationProjectIdAndNotificationTypeOrderByNotificationCreatedAtDesc(
            UUID userId, UUID projectId, NotificationType type
    );
}
