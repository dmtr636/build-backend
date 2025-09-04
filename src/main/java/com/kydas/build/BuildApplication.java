package com.kydas.build;

import kong.unirest.Unirest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@EnableAsync
@SpringBootApplication
@EnableScheduling
public class BuildApplication {
	public static void main(String[] args) {
		Unirest.config().cookieSpec("standard");
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Moscow"));
		SpringApplication.run(com.kydas.build.BuildApplication.class, args);
	}
}
