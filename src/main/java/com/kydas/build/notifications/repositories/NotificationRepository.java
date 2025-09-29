package com.kydas.build.notifications.repositories;

import com.kydas.build.core.crud.BaseRepository;
import com.kydas.build.notifications.entities.Notification;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends BaseRepository<Notification> {
}
