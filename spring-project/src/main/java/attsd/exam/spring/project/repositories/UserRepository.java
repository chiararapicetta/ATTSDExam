package attsd.exam.spring.project.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import attsd.exam.spring.project.model.User;


public interface UserRepository extends MongoRepository<User, String> {
    
    User findByEmail(String email);
    
}