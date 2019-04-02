package attsd.exam.spring.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@EnableCassandraRepositories(basePackages = { "attsd.exam.spring.project.model" })
public class CassandraConfig extends AbstractCassandraConfiguration {
	
	@Autowired
	private Environment environment;
	
	@Bean
	public CassandraClusterFactoryBean cluster() {
		CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
		cluster.setContactPoints("127.0.0.1");
		cluster.setPort(9142);
		return cluster;
	}

	@Override
	protected String getKeyspaceName() {
		return environment.getProperty("spring.data.cassandra.keyspace-name");
	}
	

	@Override
	public SchemaAction getSchemaAction() {
		return SchemaAction.NONE;
	}

}
