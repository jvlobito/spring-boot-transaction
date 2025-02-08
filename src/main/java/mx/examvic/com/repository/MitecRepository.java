package mx.examvic.com.repository;

import mx.examvic.com.entities.MitecEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MitecRepository extends MongoRepository<MitecEntity, String> {
    MitecEntity findByEmail(String email);
}