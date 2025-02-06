# 1. JDK 17 이미지를 기반으로 설정
FROM openjdk:17-jdk-slim

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. Gradle 빌드된 JAR 파일을 컨테이너로 복사
COPY ./build/libs/toss-beneface-0.0.1-SNAPSHOT.jar app.jar

# 4. 환경 변수 설정
ENV PASSWORD='sakncksjallkasdkl#$@^#*asdsiajodias2737'

# 5. 애플리케이션 실행
ENTRYPOINT ["sh", "-c", "java -Djasypt.password=$PASSWORD -jar app.jar"]