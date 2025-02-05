# 1. JDK 17을 기반으로 하는 이미지 사용
FROM openjdk:17-jdk-slim

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. Gradle 빌드된 JAR 파일을 컨테이너로 복사
COPY build/libs/*.jar app.jar

# 4. Jasypt 비밀번호 환경 변수 설정 (필요 시)
ENV PASSWORD=sakncksjallkasdkl#$@^#*asdsiajodias2737

# 5. 애플리케이션 실행
ENTRYPOINT ["java", "-Djasypt.password=${PASSWORD}", "-jar", "/app.jar"]
