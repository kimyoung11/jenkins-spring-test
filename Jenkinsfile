pipeline {
    agent any

    tools {
        // 젠킨스 관리 화면에 매핑된 자바 이름
        jdk 'java17'
        // 💡 [핵심 추가] 젠킨스가 내부적으로 도커 CLI 명령어를 쓸 수 있도록 도구를 다운로드합니다.
        dockerTool 'latest' 
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

        stage('3. 도커 이미지 빌드 (Docker Build)') {
            steps {
                echo '스프링 부트 JDK 21 도커 이미지를 굽습니다...'
                // 이제 내부 도커 툴 덕분에 이 명령어 구문이 정상 작동합니다!
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