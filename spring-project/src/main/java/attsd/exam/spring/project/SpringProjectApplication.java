package attsd.exam.spring.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class})
public class SpringProjectApplication {

	

	public static void main(String[] args) {
		SpringApplication.run(SpringProjectApplication.class, args);

	}

	

}
