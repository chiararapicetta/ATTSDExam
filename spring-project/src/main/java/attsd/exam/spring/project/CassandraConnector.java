package attsd.exam.spring.project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.jmx.JmxReporter;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;

public class CassandraConnector {
	
	private static final Logger log = LoggerFactory.getLogger(CassandraConnector.class);
	
	private Cluster cluster;
	private Session session;
	
	public void connect(final String node, final Integer port) {
		/*
		 * Builder builder = Cluster.builder().addContactPoint(node); if (port != null)
		 * { builder.withPort(port); } cluster = builder.build();
		 * 
		 * Metadata metadata = cluster.getMetadata(); log.info("Cluster name: " +
		 * metadata.getClusterName());
		 * 
		 * for(Host host : metadata.getAllHosts()) { log.info("Datacenter: " +
		 * host.getDatacenter() + "Host: " + host.getAddress() + "Rack: " +
		 * host.getRack()); }
		 * 
		 * session = cluster.connect();
		 */
		Cluster cluster = Cluster.builder().withoutJMXReporting().build();
		JmxReporter reporter = JmxReporter.forRegistry(cluster.getMetrics().getRegistry()).inDomain(cluster.getClusterName() + "metrics").build();
		reporter.start();
		session = cluster.connect();
	}

	public Session getSession() {
		return session;
	}
	
	public void close() {
		session.close();
		cluster.close();
	}
	
	
}
