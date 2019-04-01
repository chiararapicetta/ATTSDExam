package attsd.exam.spring.project;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.datastax.driver.core.Session;

import attsd.exam.spring.project.repositories.KeyspaceRepository;

@SpringBootApplication
public class SpringProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringProjectApplication.class, args);
		
		CassandraConnector connector = new CassandraConnector();
		connector.connect("127.0.0.1", null);
		Session session = connector.getSession();
		
		connector.close();
		
		
	}

}
