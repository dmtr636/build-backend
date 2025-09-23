package com.kydas.build.core.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class InstantFromStringDeserializer extends JsonDeserializer<Instant> {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String str = p.getText();
        if (str == null || str.isEmpty()) return null;
        LocalDateTime ldt = LocalDateTime.parse(str, FORMATTER);
        return ldt.toInstant(ZoneOffset.UTC);
    }
}
