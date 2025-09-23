package com.kydas.build;

import kong.unirest.Unirest;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;
import java.util.TimeZone;

@EnableAsync
@SpringBootApplication
@EnableScheduling
public class BuildApplication {
	public static void main(String[] args) throws TesseractException {
		Unirest.config().cookieSpec("standard");
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Moscow"));
		SpringApplication.run(com.kydas.build.BuildApplication.class, args);

		System.out.println("Start");

		File image = new File("src/main/resources/pdf/test.pdf");
		Tesseract tesseract = new Tesseract();
		tesseract.setDatapath("src/main/resources/tessdatafast");
		tesseract.setLanguage("rus");
		tesseract.setPageSegMode(1);
		tesseract.setOcrEngineMode(1);
		String result = tesseract.doOCR(image);
		System.out.println(result);

		System.out.println("End");

	}
}
