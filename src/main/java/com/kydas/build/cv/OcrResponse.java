package com.kydas.build.cv;

public record OcrResponse(
        boolean success,
        String text,
        int chars,
        String languages
) {
}

