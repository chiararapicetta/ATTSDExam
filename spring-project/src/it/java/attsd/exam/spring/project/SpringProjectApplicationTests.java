package attsd.exam.spring.project;

import org.junit.Test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import attsd.exam.spring.project.config.CassandraConfig;



@Import({CassandraConfig.class})
@SpringBootTest
public class SpringProjectApplicationTests {

	@Test
	public void contextLoads() {
	}

}