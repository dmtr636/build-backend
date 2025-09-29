package com.kydas.build.notifications.entities;

import com.kydas.build.users.User;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Table(name = "notification_recipients")
@Getter
@Setter
@Accessors(chain = true)
public class NotificationRecipient {
    @EmbeddedId
    private NotificationRecipientKey id;

    @ManyToOne(optional = false)
    @MapsId("notificationId")
    @JoinColumn(name = "notification_id", nullable = false)
    private Notification notification;

    @ManyToOne(optional = false)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private boolean read = false;
}
