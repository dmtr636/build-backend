package com.kydas.build.events;

import com.kydas.build.core.exceptions.classes.ApiException;
import com.kydas.build.core.security.SecurityContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class EventPublisher {

    private final EventService eventService;
    private final EventWebSocketController webSocketController;
    private final SecurityContext securityContext;

    public EventPublisher(EventService eventService,
                          EventWebSocketController webSocketController,
                          SecurityContext securityContext) {
        this.eventService = eventService;
        this.webSocketController = webSocketController;
        this.securityContext = securityContext;
    }

    public void publish(String objectName, EventWebSocketDTO.Type type, ActionType actionType, Object data) throws ApiException {
        if (securityContext.isAuthenticated()) {
            var userId = securityContext.getCurrentUser().getId();
            var event = eventService.create(new EventDTO()
                    .setUserId(userId)
                    .setAction(type.name().toLowerCase())
                    .setActionType(actionType.value())
                    .setObjectName(objectName)
                    .setObjectId(extractId(data))
            );
            webSocketController.notifyObjectChange(new EventWebSocketDTO()
                    .setType(EventWebSocketDTO.Type.CREATE)
                    .setObjectName("event")
                    .setData(event)
            );
        }
        webSocketController.notifyObjectChange(new EventWebSocketDTO()
                .setType(type)
                .setObjectName(objectName)
                .setData(data)
        );
    }

    public void publish(String objectName, EventWebSocketDTO.Type type, ActionType actionType, Object data,
                        Map<String, Object> info) throws ApiException {
        if (securityContext.isAuthenticated()) {
            var userId = securityContext.getCurrentUser().getId();
            var event = eventService.create(new EventDTO()
                    .setUserId(userId)
                    .setAction(type.name().toLowerCase())
                    .setActionType(actionType.value())
                    .setObjectName(objectName)
                    .setObjectId(extractId(data))
                    .setInfo(info)
            );
            webSocketController.notifyObjectChange(new EventWebSocketDTO()
                    .setType(EventWebSocketDTO.Type.CREATE)
                    .setObjectName("event")
                    .setData(event)
            );
        }
        webSocketController.notifyObjectChange(new EventWebSocketDTO()
                .setType(type)
                .setObjectName(objectName)
                .setData(data)
        );
    }

    private String extractId(Object data) {
        try {
            var method = data.getClass().getMethod("getId");
            Object id = method.invoke(data);
            return id != null ? id.toString() : null;
        } catch (Exception e) {
            return null;
        }
    }
}
