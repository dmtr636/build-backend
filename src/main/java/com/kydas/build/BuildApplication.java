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
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.nio.file.*;

@EnableAsync
@SpringBootApplication
@EnableScheduling
public class BuildApplication {
	public static void main(String[] args) throws Exception {
		Unirest.config().cookieSpec("standard");
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Moscow"));
		SpringApplication.run(com.kydas.build.BuildApplication.class, args);

		System.out.println("Start");

		// 1) Достаём PDF из classpath -> во временный файл
		File pdfTemp = extractClasspathFileToTemp("pdf/test3.pdf", "test3", ".pdf");

		// 2) Достаём tessdatafast (всю папку) из classpath -> во временную директорию
		Path tessdataDir = extractClasspathDirToTemp("tessdatafast"); // внутри должен быть rus.traineddata

		Tesseract tesseract = new Tesseract();
		tesseract.setDatapath(tessdataDir.toAbsolutePath().toString());
		tesseract.setLanguage("rus");
		tesseract.setPageSegMode(1);
		tesseract.setOcrEngineMode(1);

		String result = tesseract.doOCR(pdfTemp);
		System.out.println(result);

		System.out.println("End");
	}

	private static File extractClasspathFileToTemp(String classpathLocation, String prefix, String suffix) throws IOException {
		ClassPathResource resource = new ClassPathResource(classpathLocation);
		if (!resource.exists()) {
			throw new FileNotFoundException("Resource not found on classpath: " + classpathLocation);
		}
		Path temp = Files.createTempFile(prefix, suffix);
		try (InputStream in = resource.getInputStream()) {
			Files.copy(in, temp, StandardCopyOption.REPLACE_EXISTING);
		}
		temp.toFile().deleteOnExit();
		return temp.toFile();
	}

	private static Path extractClasspathDirToTemp(String classpathDir) throws IOException {
		// Копирует папку из classpath во временную директорию рекурсивно
		Path target = Files.createTempDirectory("tessdata");
		copyClasspathDir(classpathDir, target);
		return target;
	}

	private static void copyClasspathDir(String classpathDir, Path targetBase) throws IOException {
		String[] files = new String[] { "rus.traineddata", "osd.traineddata" };
		for (String name : files) {
			ClassPathResource res = new ClassPathResource(classpathDir + "/" + name);
			if (!res.exists()) continue;
			Path out = targetBase.resolve(name);
			try (InputStream in = res.getInputStream()) {
				Files.copy(in, out, StandardCopyOption.REPLACE_EXISTING);
			}
		}
	}
}
