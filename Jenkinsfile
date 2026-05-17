pipeline {
    agent any

    tools {
        // 에러 나던 dockerTool은 깔끔하게 지우고 자바만 남겨둡니다.
        jdk 'java17'
    }

    environment {
        DB_PASSWORD = credentials('my-db-password-token')
        IMAGE_NAME  = 'my-spring-app'
    }

    stages {
        stage('1. 환경 확인 및 소스 다운로드') {
            steps {
                sh 'java -version'
            }
        }

        stage('2. Gradle 빌드') {
            steps {
                echo 'JDK 21 환경에서 Jar 파일 생성 중...'
                sh './gradlew clean build -x test'
            }
        }

        // 💡 [핵심 변경] 젠킨스 방 안의 docker 대신, 공유된 소켓(/var/run/docker.sock)을 통해 
        // 맥북 거실에 있는 도커 엔진을 직접 호출하여 이미지를 굽습니다.
        stage('3. 도커 이미지 빌드 (Docker Build)') {
            steps {
                echo '스프링 부트 JDK 21 도커 이미지를 굽습니다...'
                // 현재 작업 공간의 Dockerfile을 기반으로 이미지를 생성합니다.
                sh "docker build -t ${IMAGE_NAME}:latest ."
            }
        }

        stage('4. 구버전 컨테이너 삭제 및 신버전 배포 (Deploy)') {
            steps {
                echo '기존 컨테이너 스톱 및 삭제...'
                sh "docker stop ${IMAGE_NAME}-container || true"
                sh "docker rm ${IMAGE_NAME}-container || true"

                echo '새로운 스프링 부트(JDK21) 컨테이너 구동...'
                sh """
                    docker run -d \
                      --name ${IMAGE_NAME}-container \
                      -p 8081:8081 \
                      -e DB_PASSWORD=${DB_PASSWORD} \
                      ${IMAGE_NAME}:latest
                """
            }
        }
    }
}