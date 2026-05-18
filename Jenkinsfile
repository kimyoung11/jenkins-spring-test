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

        stage('4. 멀티 컨테이너 일괄 배포 (Docker Compose Deploy)') {
            steps {
                echo '맥북 호스트 도커 엔진을 직접 제어하여 도커 컴포즈 배포를 수행합니다...'
                
                withEnv(["DB_PASSWORD=${DB_PASSWORD}"]) {
                    // 1. 기존에 포트 충돌을 일으킬 수 있는 구버전 컨테이너들을 도커 기본 명령어로 확실하게 밀어버립니다.
                    sh 'docker rm -f my-spring-app-container mysql-db-container || true'
                    
                    // 2. 💡 [최종 치트키] 젠킨스가 내부 도커를 쓰는 대신, 
                    // 공유된 소켓 너머에 있는 맥북 거실의 진짜 최신 도커 엔진에게 컴포즈 파일의 경로를 찔러 넣어 실행시킵니다.
                    // 이렇게 하면 맥북이 알아서 내부에 내장된 최신 docker compose 명령어로 완벽하게 세트를 구동합니다!
                    sh 'docker run --rm -v /var/run/docker.sock:/var/run/docker.sock -v jenkins_home:/var/jenkins_home docker:cli sh -c "cd /var/jenkins_home/workspace/spring-pipeline-job && DB_PASSWORD=$DB_PASSWORD docker compose down || true"'
                    sh 'docker run --rm -v /var/run/docker.sock:/var/run/docker.sock -v jenkins_home:/var/jenkins_home docker:cli sh -c "cd /var/jenkins_home/workspace/spring-pipeline-job && DB_PASSWORD=$DB_PASSWORD docker compose up -d"'
                }
            }
        }
    }
}