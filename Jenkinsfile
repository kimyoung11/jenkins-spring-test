pipeline {
    agent any

    // 2교시에서 등록한 젠킨스 금고(Credentials)를 변수로 가져옵니다.
    environment {
        DB_PASSWORD = credentials('my-db-password-token')
    }

    stages {
        stage('1. 환경 확인 (Check Environment)') {
            steps {
                echo '배포를 시작합니다. 현재 자바 버전을 확인합니다.'
                sh 'java -version'
            }
        }

        stage('2. 소스코드 빌드 (Gradle Build)') {
            steps {
                echo '스프링 부트 프로젝트를 빌드합니다...'
                // 1교시에서 바꾼 자바 17 환경에서 빌드를 수행합니다.
                sh './gradlew clean build -x test'
            }
        }

        stage('3. 보안 변수 검증 (Verify Security)') {
            steps {
                echo '2교시 환경 변수가 정상적으로 로드되었는지 확인합니다.'
                // 이 빌드 과정에서 스프링을 빌드할 때 환경 변수가 주입됩니다.
                echo "현재 주입된 DB_PASSWORD 변수 상태: ${DB_PASSWORD}"
            }
        }
    }
}