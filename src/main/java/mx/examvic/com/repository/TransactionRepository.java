package mx.examvic.com.repository;
//
//
import mx.examvic.com.entities.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

//
public interface TransactionRepository extends MongoRepository<Transaction, String> {

    Optional<Transaction> findByTrackId(String trackId);
    @Query("{' createAt' : {$gte: ?0, $lte: ?1 } }")
    List<Transaction> findByDateBetween(LocalDate startDate, LocalDate endDate);


}
