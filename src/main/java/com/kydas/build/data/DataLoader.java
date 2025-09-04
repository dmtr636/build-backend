package com.kydas.build.data;

import com.kydas.build.core.exceptions.classes.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;

@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {
    private final UserDataLoader userDataLoader;

    @Override
    public void run(ApplicationArguments args) throws IOException, ApiException, ParseException {
        if (!userDataLoader.isRootUserExists()) {
            userDataLoader.createUsers();
        }
    }
}
