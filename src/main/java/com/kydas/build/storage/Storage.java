package com.kydas.build.storage;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Storage {
    public Map<UUID, Map<String, String>> userStatus = new ConcurrentHashMap<>();
}
