package attsd.exam.spring.project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@EnableCassandraRepositories(basePackages = "attsd.exam.spring.project.repositories")
public class CassandraConfig extends AbstractCassandraConfiguration {

	@Override
	protected String getKeyspaceName() {
		return "testKeySpace";
	}

}
