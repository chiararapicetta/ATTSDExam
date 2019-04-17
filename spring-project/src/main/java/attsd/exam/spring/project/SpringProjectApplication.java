package attsd.exam.spring.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;



@SpringBootApplication
public class SpringProjectApplication {

	

	public static void main(String[] args) {
		SpringApplication.run(SpringProjectApplication.class, args);

	}

	

}
