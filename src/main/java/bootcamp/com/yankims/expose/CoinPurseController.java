package bootcamp.com.yankims.expose;

import bootcamp.com.yankims.business.ICoinPurseService;
import bootcamp.com.yankims.model.dto.CoinPurseDto;
import bootcamp.com.yankims.model.dto.TransactionDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/yankis")
public class CoinPurseController {
  @Autowired
  @Qualifier("CoinPurseService")
  private ICoinPurseService coinPurseService;

  /**
   * Method to list all coiN purse.
   *
   * @return list of all active coin purse.
   */
  @GetMapping("")
  public Flux<CoinPurseDto> findAllCoinPurse() {
    return coinPurseService.findAllCoinPurse();
  }

  /**
   * Method to search one coin purse for id.
   *
   * @param id -> identify unique of coin purse.
   * @return object type coin purse.
   */
  @GetMapping("/{id}")
  public Mono<ResponseEntity<CoinPurseDto>> findOneCoinPurse(@PathVariable String id) {
    return coinPurseService.findOneCoinPurse(id)
      .flatMap(c -> Mono.just(ResponseEntity.ok().body(c)))
      .switchIfEmpty(Mono.just(ResponseEntity.badRequest().build()));
  }

  /**
   * Method to search one Transaction for id.
   *
   * @param id -> identify unique of Transaction.
   * @return object type Transaction.
   */
  @CircuitBreaker(name = "getCoinTransactionCB", fallbackMethod = "fallBackGetCoinTransaction")
  @GetMapping("/status/transactions/{TransactionId}")
  public Flux<TransactionDto> findTransactionPending(@PathVariable("TransactionId") String id) {
    return coinPurseService.findTransactionPending(id);
  }

  /**
   * Method to Confirm buy bootCoins transaction.
   *
   * @param transactionDto -> Transaction pending.
   * @return message type string odf confirmed.
   */
  @CircuitBreaker(name = "postCoinTransactionCB", fallbackMethod = "fallBackPostCoinTransaction")
  @PostMapping("/confirm/transactions")
  public Mono<ResponseEntity<TransactionDto>> findTransactionPending(@RequestBody TransactionDto transactionDto) {
    return coinPurseService.confirmBuyCoinsTransaction(transactionDto)
      .flatMap(c -> Mono.just(ResponseEntity.ok().body(c)))
      .switchIfEmpty(Mono.just(ResponseEntity.badRequest().build()));
  }

  /**
   * Method to save coin purse.
   *
   * @param coinPurseDto -> object to save coin purse.
   * @return object saved type coin purse.
   */
  @CircuitBreaker(name = "postCoinPurseCB", fallbackMethod = "fallBackPostCoinPurse")
  @PostMapping("")
  public Mono<ResponseEntity<CoinPurseDto>> createCoinPurse(@RequestBody CoinPurseDto coinPurseDto) {
    return coinPurseService.createCoinPurse(coinPurseDto)
      .flatMap(c -> Mono.just(ResponseEntity.ok().body(c)))
      .switchIfEmpty(Mono.just(ResponseEntity.badRequest().build()));
  }

  /**
   * Method to update coin purse.
   *
   * @param coinPurseDto -> object to save coin purse.
   * @param id           -> identify unique of coin purse.
   * @return object update coin purse.
   */
  @CircuitBreaker(name = "putCoinPurseCB", fallbackMethod = "fallBackPutCoinPurse")
  @PutMapping("/{id}")
  public Mono<ResponseEntity<CoinPurseDto>> updateCoinPurse(@RequestBody CoinPurseDto coinPurseDto,
                                                            @PathVariable String id) {
    return coinPurseService.updateCoinPurse(coinPurseDto, id)
      .flatMap(c -> Mono.just(ResponseEntity.ok().body(c)))
      .switchIfEmpty(Mono.just(ResponseEntity.badRequest().build()));
  }

  /**
   * Method to delete coin purse.
   *
   * @param id -> identify unique of coin purse.
   * @return object modified type coin purse.
   */
  @DeleteMapping("/{id}")
  public Mono<ResponseEntity<CoinPurseDto>> deleteCoinPurse(@PathVariable String id) {
    return coinPurseService.deleteCoinPurse(id)
      .flatMap(c -> Mono.just(ResponseEntity.ok().body(c)))
      .switchIfEmpty(Mono.just(ResponseEntity.badRequest().build()));
  }

  /**
   * Method CircuitBreaker to save coin purse.
   *
   * @param id -> Identified Transaction.
   * @param ex           -> this is exception error.
   * @return exception error.
   */
  public Flux<String> fallBackGetCoinTransaction(@PathVariable("TransactionId") String id,
                                                            RuntimeException ex) {
    return Flux.just("Microservice Transaction not found with: " + id);
  }
  /**
   * Method CircuitBreaker to update Transaction.
   *
   * @param transactionDto -> object to update transaction.
   * @param ex           -> this is exception error.
   * @return exception error.
   */

  public Mono<ResponseEntity<String>> fallBackPostCoinTransaction(@RequestBody TransactionDto transactionDto,
                                                            RuntimeException ex) {
    return Mono.just(ResponseEntity.ok().body("Microservice Transaction not found. " + transactionDto.getProductId()));
  }

  /**
   * Method CircuitBreaker to save coin purse.
   *
   * @param coinPurseDto -> object to save coin purse.
   * @param ex           -> this is exception error.
   * @return exception error.
   */
  public Mono<ResponseEntity<String>> fallBackPostCoinPurse(@RequestBody CoinPurseDto coinPurseDto,
                                                            RuntimeException ex) {
    return Mono.just(ResponseEntity.ok().body("Microservice product not found.Coin Purse for number "
      + coinPurseDto.getPhoneNumber() + " error save."));
  }

  /**
   * Method CircuitBreaker to update coin purse.
   *
   * @param coinPurseDto -> object to update coin purse.
   * @param ex           -> this is exception error.
   * @return exception error.
   */
  public Mono<ResponseEntity<String>> fallBackPutCoinPurse(@RequestBody CoinPurseDto coinPurseDto,
                                                           @PathVariable String id,
                                                           RuntimeException ex) {
    return Mono.just(ResponseEntity.ok().body("Microservice product not found for id: " + id
      + " and Coin Purse for number: "
      + coinPurseDto.getPhoneNumber() + " error update."));
  }
}
