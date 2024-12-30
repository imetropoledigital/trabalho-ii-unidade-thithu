package com.imd.api.repository;

import com.imd.api.model.User; // Certifique-se de que esta importação é válida
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String>{
}