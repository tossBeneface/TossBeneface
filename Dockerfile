# 1. JDK 17을 기반으로 하는 이미지 사용
FROM openjdk:17-jdk-slim

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. Gradle 빌드된 JAR 파일을 컨테이너로 복사
COPY ./build/libs/toss-beneface-0.0.1-SNAPSHOT.jar app.jar

# 4. 애플리케이션 실행
#ENTRYPOINT ["java", "-Djasypt.password=${PASSWORD}", "-jar", "app.jar"]
ENTRYPOINT ["sh", "-c", "java -Djasypt.password=$PASSWORD -jar app.jar"]