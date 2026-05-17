pipeline {
    agent any

    environment {
        DB_PASSWORD = credentials('my-db-password-token')
        // 내 도커 이미지 이름을 변수로 관리합니다.
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
                echo 'Jar 파일 생성 중...'
                sh './gradlew clean build -x test'
            }
        }

        stage('3. 도커 이미지 빌드 (Docker Build)') {
            steps {
                echo '스프링 부트 도커 이미지를 굽습니다...'
                // 기존 구버전 이미지가 있다면 찌꺼기 방지를 위해 새로 빌드합니다.
                sh "docker build -t ${IMAGE_NAME}:latest ."
            }
        }

        stage('4. 구버전 컨테이너 삭제 및 신버전 배포 (Deploy)') {
            steps {
                echo '운영 중인 기존 컨테이너가 있다면 중지하고 삭제합니다...'
                /* * [💡 꿀팁] 처음 배포할 때는 기존 컨테이너가 없어서 에러가 나므로, 
                 * 앞에 || true를 붙여서 에러가 나도 파이프라인이 멈추지 않고 계속 진행되게 합니다.
                 */
                sh "docker stop ${IMAGE_NAME}-container || true"
                sh "docker rm ${IMAGE_NAME}-container || true"

                echo '새로운 스프링 부트 컨테이너를 8081 포트로 띄웁니다...'
                // 젠킨스 금고에서 가져온 DB_PASSWORD를 도커 실행 시점에 주입합니다.
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