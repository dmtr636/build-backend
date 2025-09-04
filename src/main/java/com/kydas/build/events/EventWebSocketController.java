package com.kydas.build.events;

import com.kydas.build.users.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@Tag(name = "Сервис взаимодействия по WebSocket")
public class EventWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public EventWebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notifyObjectChange(EventWebSocketDTO eventDTO) {
        messagingTemplate.convertAndSend("/topic/events", eventDTO);
    }

    public void notifyObjectChange(User user, EventWebSocketDTO eventDTO) {
        if (user.getLogin() != null) {
            messagingTemplate.convertAndSendToUser(user.getLogin(), "/queue/changes", eventDTO);
        }
    }
}
