pipeline {
    agent any

    tools {
        jdk 'java17'
    }

    environment {
        DB_PASSWORD = credentials('my-db-password-token')
        IMAGE_NAME  = 'my-spring-app'
    }

    stages {
        stage('1. 환경 확인 및 소스 다운로드') { steps { sh 'java -version' } }
        stage('2. Gradle 빌드') { steps { sh './gradlew clean build -x test' } }
        stage('3. 도커 이미지 빌드 (Docker Build)') { steps { sh "docker build -t ${IMAGE_NAME}:latest ." } }

        stage('4. 구버전 컨테이너 삭제 및 신버전 배포 (Deploy)') {
            steps {
                echo '기존 컨테이너 스톱 및 삭제...'
                sh "docker stop ${IMAGE_NAME}-container || true"
                sh "docker rm ${IMAGE_NAME}-container || true"

                echo '새로운 스프링 부트 컨테이너를 8082 포트로 안전하게 띄웁니다...'
                // 💡 [핵심 변경] 맥북 호스트 포트를 8082로 바꿉니다! (8081 충돌 원천 차단)
                sh """
                    docker run -d \
                      --name ${IMAGE_NAME}-container \
                      -p 8082:8081 \
                      -e DB_PASSWORD=${DB_PASSWORD} \
                      ${IMAGE_NAME}:latest
                """
            }
        }
    }
}