package attsd.exam.spring.project.repositories;

import com.datastax.driver.core.Session;

public class KeyspaceRepository {

	private Session session;

	public KeyspaceRepository(Session session) {
		// TODO Auto-generated constructor stub
		this.session = session;
	}

	public void createKeyspace(String kEYSPACE_NAME, String string, int i) {
		// TODO Auto-generated method stub
		
	}

	public void useKeyspace(String kEYSPACE_NAME) {
		// TODO Auto-generated method stub
		
	}

}
