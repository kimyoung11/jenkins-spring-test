pipeline {
    agent any

    tools {
        jdk 'java17'
    }

    environment {
        // 젠킨스 금고에서 토큰을 꺼내와 변수에 장착
        DB_PASSWORD = credentials('my-db-password-token')
        IMAGE_NAME  = 'my-spring-app'
    }

    stages {
        stage('1. 환경 확인 및 소스 다운로드') { steps { sh 'java -version' } }
        stage('2. Gradle 빌드') { steps { sh './gradlew clean build -x test' } }
        stage('3. 도커 이미지 빌드 (Docker Build)') { steps { sh "docker build -t ${IMAGE_NAME}:latest ." } }

        stage('4. 멀티 컨테이너 일괄 배포 (Docker Compose Deploy)') {
            steps {
                echo '보안 래핑을 적용하여 맥북 도커 엔진에서 컴포즈 배포를 수행합니다...'
                
                // 💡 [핵심 변경] 큰따옴표 내부에 Groovy 변수를 직접 찌르면 보안 경고가 뜹니다.
                // 아래처럼 싱글 쿼트('') 래핑 환경에서 주입하면 경고(Warning)가 완벽히 사라집니다!
                withEnv(["DB_PASSWORD=${DB_PASSWORD}"]) {
                    
                    // 1. 기존 구버전 서비스 세트를 청소합니다. (도커 기본 명령어 'rm -f' 활용)
                    sh 'docker rm -f my-spring-app-container mysql-db-container || true'
                    
                    // 2. [치트키] 젠킨스 방 안에서 컴포즈를 안 치고, 
                    // 공유된 소켓을 통해 맥북 거실 엔진에게 "야! 내 방에 있는 docker-compose.yml 파일 읽어서 컨테이너 세트 띄워!"라고 직접 명령을 하사합니다.
                    sh 'docker run --rm -v /var/run/docker.sock:/var/run/docker.sock -v "$PWD":/app -w /app -e DB_PASSWORD=$DB_PASSWORD docker/compose:1.29.2 down || true'
                    sh 'docker run --rm -v /var/run/docker.sock:/var/run/docker.sock -v "$PWD":/app -w /app -e DB_PASSWORD=$DB_PASSWORD docker/compose:1.29.2 up -d'
                }
            }
        }
    }
}