FROM gradle:8.10.1-jdk21-alpine AS builder
WORKDIR /app
COPY build.gradle.kts settings.gradle.kts gradle.lockfile ./
COPY gradle ./gradle
RUN gradle dependencies --no-daemon
COPY src ./src
RUN gradle clean build -x test --no-daemon

FROM eclipse-temurin:21.0.4_7-jre-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar /app/app.jar
RUN mkdir -p /logs
RUN mkdir -p /cdn
EXPOSE 8080
ENTRYPOINT ["java", "-XX:MaxJavaStackTraceDepth=10", "-Xmx1024m", "-jar", "/app/app.jar"]