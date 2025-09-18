package com.kydas.build.core.utils;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.stream.Collectors;

public final class PasswordUtils {

    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#^&*?";

    private static final String ALL = UPPER + LOWER + DIGITS + SPECIAL;

    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordUtils() {}

    public static String generate(int length) {
        if (length < 8) {
            throw new IllegalArgumentException("Password length must be at least 8 characters");
        }

        StringBuilder password = new StringBuilder();

        password.append(randomChar(UPPER));
        password.append(randomChar(LOWER));
        password.append(randomChar(DIGITS));
        password.append(randomChar(SPECIAL));

        for (int i = 4; i < length; i++) {
            password.append(randomChar(ALL));
        }

        var chars = password.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());
        Collections.shuffle(chars, RANDOM);

        return chars.stream()
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

    private static char randomChar(String chars) {
        return chars.charAt(RANDOM.nextInt(chars.length()));
    }
}
