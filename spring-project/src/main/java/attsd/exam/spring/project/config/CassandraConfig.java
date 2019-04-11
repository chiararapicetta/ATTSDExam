package attsd.exam.spring.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.CassandraAdminTemplate;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.convert.CassandraConverter;
import org.springframework.data.cassandra.core.convert.MappingCassandraConverter;
import org.springframework.data.cassandra.core.mapping.BasicCassandraMappingContext;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.util.Assert;

import com.datastax.driver.core.Cluster;

@EnableCassandraRepositories(basePackages = { "attsd.exam.spring.project.repositories"})
public class CassandraConfig extends AbstractCassandraConfiguration {

	@Autowired
	private Environment environment;

	@Override
	protected String getKeyspaceName() {
		return environment.getProperty("spring.data.cassandra.keyspace-name");
	}

	@Bean
	public CassandraClusterFactoryBean cluster() {
		CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
		cluster.setContactPoints("127.0.0.1");
		cluster.setPort(9142);
		return cluster;
	}
	
	protected Cluster getRequiredCluster() {
		CassandraClusterFactoryBean factoryBean = cluster();
		Cluster cluster = factoryBean.getObject();
		Assert.state(cluster != null, "Cluster not initialized");
		return cluster;
	}

	@Override
	protected String getContactPoints() {
		return "127.0.0.1";
	}

	@Override
	protected int getPort() {
		return 9142;
	}

	@Override
	public SchemaAction getSchemaAction() {
		return SchemaAction.NONE;
	}

	/*
	 * @Bean public CassandraMappingContext cassandraMapping() throws
	 * ClassNotFoundException { return new BasicCassandraMappingContext(); }
	 */

	/*
	 * @Bean public CassandraOperations operations() throws Exception { return new
	 * CassandraTemplate(session().getObject(), new MappingCassandraConverter(new
	 * BasicCassandraMappingContext())); }
	 */

	@Bean
	public CassandraAdminTemplate cassandraTemplate() throws Exception {
		return new CassandraAdminTemplate(getRequiredSession(), cassandraConverter());
	}

}
