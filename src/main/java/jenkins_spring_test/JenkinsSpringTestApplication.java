package jenkins_spring_test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JenkinsSpringTestApplication {

	private static String dbPassword;

	// Setter를 통해 static 변수에 값을 주입합니다.
	@Value("${DB_PASSWORD:NOT_FOUND}")
	public void setDbPassword(String value) {
		dbPassword = value;
	}

	public static void main(String[] args) {
		System.out.println("---START---");
		SpringApplication.run(JenkinsSpringTestApplication.class, args);
		System.out.println("비밀번호 테스트 : " + dbPassword);
		System.out.println("---END2---");
		System.out.println("---END4---");
	}

}
