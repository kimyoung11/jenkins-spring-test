package jenkins_spring_test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JenkinsSpringTestApplication {

	public static void main(String[] args) {
		System.out.println("---START---");
		SpringApplication.run(JenkinsSpringTestApplication.class, args);
		System.out.println("---END2---");
	}

}
