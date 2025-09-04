package com.kydas.build.events;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class EventWebSocketDTO {
    private Type type;
    private String objectName;
    private Object data;

    public enum Type {CREATE, UPDATE, DELETE}
}
