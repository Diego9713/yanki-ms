package bootcamp.com.yankims.repository;

import bootcamp.com.yankims.model.CoinPurse;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ICoinPurseRepository extends ReactiveMongoRepository<CoinPurse, String> {

  Mono<CoinPurse> findByPhoneNumberOrPhoneImeiOrDocumentNumber(String phone,String phoneImei,String document);

}
