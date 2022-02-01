package bootcamp.com.yankims.business;

import bootcamp.com.yankims.model.dto.CoinPurseDto;
import bootcamp.com.yankims.model.dto.TransactionDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICoinPurseService {

  Flux<CoinPurseDto> findAllCoinPurse();

  Flux<TransactionDto> findTransactionPending(String id);

  Mono<TransactionDto> confirmBuyCoinsTransaction(TransactionDto transactionDto);

  Mono<CoinPurseDto> findOneCoinPurse(String id);

  Mono<CoinPurseDto> createCoinPurse(CoinPurseDto coinPurseDto);

  Mono<CoinPurseDto> updateCoinPurse(CoinPurseDto coinPurseDto, String id);

  Mono<CoinPurseDto> deleteCoinPurse(String id);
}
