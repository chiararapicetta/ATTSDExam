package attsd.exam.spring.project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.stereotype.Component;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Host;

@Configuration
public class CassandraConnector {

	private static final Logger LOG = LoggerFactory.getLogger(CassandraConnector.class);

	private Cluster cluster;

	private Session session;

	public void connect(final String node, final Integer port) {

		Builder b = Cluster.builder().addContactPoint(node);

		if (port != null) {
			b.withPort(port);
		}
		cluster = b.build();

		Metadata metadata = cluster.getMetadata();
		LOG.info("Cluster name: " + metadata.getClusterName());

		for (Host host : metadata.getAllHosts()) {
			LOG.info(
					"Datacenter: " + host.getDatacenter() + " Host: " + host.getAddress() + " Rack: " + host.getRack());
		}

		session = cluster.connect();
	}

	@Bean
	public Session getSession() {
		return this.session;
	}

	public void close() {
		session.close();
		cluster.close();
	}

}
