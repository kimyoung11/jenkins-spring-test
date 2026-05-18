pipeline {
    agent any

    tools {
        jdk 'java17'
    }

    environment {
        // 도커 컴포즈가 인식할 수 있도록 환경 변수를 선언합니다.
        // 젠킨스 금고에서 꺼낸 비밀번호를 컴포즈 안의 MySQL root 패스워드와 스프링 패스워드로 동시 주입합니다.
        DB_PASSWORD = credentials('my-db-password-token')
        IMAGE_NAME  = 'my-spring-app'
    }

    stages {
        stage('1. 환경 확인 및 소스 다운로드') { steps { sh 'java -version' } }
        stage('2. Gradle 빌드') { steps { sh './gradlew clean build -x test' } }
        stage('3. 도커 이미지 빌드 (Docker Build)') { steps { sh "docker build -t ${IMAGE_NAME}:latest ." } }

        // 💡 [2교시 핵심 변경 영역] 수동 명령어를 싹 걷어내고 컴포즈 명령어로 대체합니다.
        stage('4. 멀티 컨테이너 일괄 배포 (Docker Compose Deploy)') {
            steps {
                echo '도커 컴포즈를 사용해 구버전 서비스를 내리고, 스프링+MySQL 세트를 새롭게 띄웁니다...'
                
                // 1. 기존에 돌고 있던 컴포즈 세트(스프링, DB, 가상 네트워크)를 안전하게 중지하고 삭제합니다.
                sh "docker-compose down || true"
                
                // 2. 환경변수(DB_PASSWORD)를 컴포즈 파일에 실어서 백그라운드(-d)로 세트를 통째로 구동합니다.
                sh "DB_PASSWORD=${DB_PASSWORD} docker-compose up -d"
            }
        }
    }
}