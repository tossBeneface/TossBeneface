# 1. JDK 17 이미지를 기반으로 설정
FROM openjdk:17-jdk-slim as builder

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. 필요한 파일 복사
COPY . .

# 4. gradlew 실행 권한 추가
RUN chmod +x gradlew

# 5. 환경 변수 설정
ARG PASSWORD
ENV PASSWORD=${PASSWORD}

# 6. Gradle 빌드 수행
RUN ./gradlew clean build --no-daemon

# 7. 실행 환경 설정 단계
FROM openjdk:17-jdk-slim

# 8. 작업 디렉토리 설정
WORKDIR /app

# 9. 빌드된 JAR 파일 복사
COPY --from=builder /app/build/libs/toss-beneface-0.0.1-SNAPSHOT.jar app.jar

# 10. 애플리케이션 실행
ENTRYPOINT ["sh", "-c", "java -Djasypt.password=$PASSWORD -jar app.jar"]