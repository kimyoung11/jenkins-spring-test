# 1. 자바 21실행 환경을 베이스 이미지로 지정
FROM eclipse-temurin:21-jdk-alpine

# 2. 컨테이너 내부 작업 디렉토리 설정
WORKDIR /app

# 3. 빌드된 jar 파일을 컨테이너 내부로 복사
# (build/libs/ 내부의 -plain.jar가 아닌 진짜 실행 가능한 jar 하나만 집어넣습니다)
COPY build/libs/*-SNAPSHOT.jar app.jar

# 4. 외부와 연결할 포트 지정 (우리 스프링은 8081)
EXPOSE 8081

# 5. 컨테이너가 켜질 때 스프링 부트 실행 명령어
# (2교시 때 세팅한 환경변수 DB_PASSWORD를 스프링에 주입하면서 켭니다)
ENTRYPOINT ["java", "-Dspring.datasource.password=${DB_PASSWORD}", "-jar", "app.jar"]