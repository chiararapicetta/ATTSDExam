package attsd.exam.spring.project.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.naming.ConfigurationException;

import org.apache.thrift.transport.TTransportException;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

import attsd.exam.spring.project.CassandraConnector;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = RestaurantRepository.class)
public class RestaurantRepositoryIT {

	private KeyspaceRepository schemaRepository;

	@Autowired
	private RestaurantRepository restaurantRepository;

	private Session session;

	final String KEYSPACE_NAME = "testRestaurant";
	final String RESTAURANTS = "restaurants";
	final String RESTAURANTS_BY_NAME = "restaurantsByName";

	@BeforeClass
	public static void init() throws ConfigurationException, TTransportException, IOException, InterruptedException {
		// Start an embedded Cassandra Server
		EmbeddedCassandraServerHelper.startEmbeddedCassandra(20000L);
	}

	@Before
	public void connect() {
		CassandraConnector client = new CassandraConnector();
		client.connect("127.0.0.1", 9142);
		this.session = client.getSession();
		schemaRepository = new KeyspaceRepository(session);
		schemaRepository.createKeyspace(KEYSPACE_NAME, "SimpleStrategy", 1);
		schemaRepository.useKeyspace(KEYSPACE_NAME);
		restaurantRepository = new RestaurantRepository(session);
	}

	@Test
	public void whenCreatingATable_thenCreatedCorrectly() {
		restaurantRepository.createTable();
		ResultSet result = session.execute("SELECT * FROM " + KEYSPACE_NAME + "." + RESTAURANTS + ";");
		// Collect all the column names in one list.
		List<String> columnNames = result.getColumnDefinitions().asList().stream().map(cl -> cl.getName())
				.collect(Collectors.toList());
		assertEquals(columnNames.size(), 2);
		assertTrue(columnNames.contains("id"));
		assertTrue(columnNames.contains("Name"));
	}
}
