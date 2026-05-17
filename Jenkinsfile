pipeline {
    agent any

    tools {
        jdk 'JDK21'
    }

    // 💡 [핵심 추가] 빌드가 시작되자마자 꼬인 찌꺼기 폴더를 싹 밀어버리는 옵션입니다.
    options {
        skipDefaultCheckout(true) // 기본 체크아웃을 잠시 미루고
    }

    environment {
        DB_PASSWORD = credentials('my-db-password-token')
        IMAGE_NAME  = 'my-spring-app'
    }

    stages {
        stage('0. 작업 공간 청소 및 최신 코드 다운로드') {
            steps {
                echo '꼬인 내부 디렉토리를 완전히 청소합니다...'
                cleanWs() // 👈 이 명령어가 내부의 꼬인 Git 디렉토리를 박살 냅니다.
                
                echo '깃허브에서 코드를 깨끗하게 새로 받아옵니다...'
                checkout scm // 새로 클론을 받아옵니다.
            }
        }

        stage('1. 환경 확인') {
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