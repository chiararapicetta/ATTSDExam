package attsd.exam.spring.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.datastax.driver.core.Session;

import attsd.exam.spring.project.CassandraConnector;

@Configuration
public class Config {
	
	private CassandraConnector cassandraConnector;
	
	@Bean
	public Session getSession() {
		return cassandraConnector.getSession();
	}

}
