plugins {
	java
	id("org.springframework.boot") version "3.3.3"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "com.kydas"
version = "1.0.0"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
	compileClasspath {
		resolutionStrategy.activateDependencyLocking()
	}
	runtimeClasspath {
		resolutionStrategy.activateDependencyLocking()
	}
	annotationProcessor {
		resolutionStrategy.activateDependencyLocking()
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jdbc:3.3.5")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.3.5")
	implementation("org.springframework.boot:spring-boot-starter-security:3.3.5")
	implementation("org.springframework.boot:spring-boot-starter-validation:3.3.5")
	implementation("org.springframework.boot:spring-boot-starter-web:3.3.5")
	implementation("org.springframework.boot:spring-boot-starter-websocket:3.3.5")
	implementation("org.springframework.boot:spring-boot-starter-mail:3.3.5")
	implementation("org.springframework.boot:spring-boot-starter-actuator:3.3.5")
	implementation("org.flywaydb:flyway-core")
	implementation("org.flywaydb:flyway-database-postgresql")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
	implementation("org.mapstruct:mapstruct:1.6.0")
	implementation("org.jsoup:jsoup:1.18.1")
	implementation("org.projectlombok:lombok")
	implementation("com.konghq:unirest-java:3.14.5")
	implementation("org.sejda.imageio:webp-imageio:0.1.6")
	implementation("com.bucket4j:bucket4j-core:8.10.1")
	implementation("commons-net:commons-net:3.11.1")
	implementation("net.sourceforge.tess4j:tess4j:4.6.1")
	runtimeOnly("org.postgresql:postgresql:42.7.4")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor:3.3.5")
	annotationProcessor("org.projectlombok:lombok")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.6.0")
	testAnnotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testRuntimeOnly("com.h2database:h2")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
