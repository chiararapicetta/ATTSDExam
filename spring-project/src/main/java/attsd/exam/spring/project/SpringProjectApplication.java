package attsd.exam.spring.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration(exclude= {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class SpringProjectApplication {

	

	public static void main(String[] args) {
		SpringApplication.run(SpringProjectApplication.class, args);

	}

	

}
