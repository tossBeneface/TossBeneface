# 1. JDK 17을 기반으로 하는 이미지 사용
FROM openjdk:17-jdk-slim AS builder

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. Gradle 빌드 수행
COPY . .
RUN ./gradlew clean build --no-daemon

# 4. 실제 실행용 이미지 생성
FROM openjdk:17-jdk-slim
WORKDIR /app

# 5. Gradle 빌드된 JAR 파일을 컨테이너로 복사
COPY --from=builder /app/build/libs/toss-beneface-0.0.1-SNAPSHOT.jar app.jar

# 4. 애플리케이션 실행
ENV PASSWORD=${PASSWORD}
ENTRYPOINT ["sh", "-c", "java -Djasypt.password=$PASSWORD -jar app.jar"]