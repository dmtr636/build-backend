# ===== build =====
FROM gradle:8.10.1-jdk21 AS builder
WORKDIR /app
COPY build.gradle.kts settings.gradle.kts gradle.lockfile ./
COPY gradle ./gradle
RUN gradle dependencies --no-daemon
COPY src ./src
RUN gradle clean build -x test --no-daemon

# ===== runtime (Ubuntu 24.04 / Noble) =====
FROM eclipse-temurin:21-jre-noble
WORKDIR /app

RUN apt-get update && apt-get install -y --no-install-recommends \
    tesseract-ocr tesseract-ocr-rus libleptonica-dev \
 && rm -rf /var/lib/apt/lists/*

# Only set this if you actually want to use system tessdata
ENV TESSDATA_PREFIX=/usr/share/tesseract-ocr/5/tessdata

COPY --from=builder /app/build/libs/*.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java","-XX:MaxJavaStackTraceDepth=10","-Xmx1024m","-jar","/app/app.jar"]