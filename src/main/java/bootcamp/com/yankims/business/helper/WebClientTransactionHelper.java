package bootcamp.com.yankims.business.helper;

import bootcamp.com.yankims.model.dto.TransactionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class WebClientTransactionHelper {
  @Autowired
  private WebClient webClient;

  /**
   * Method to search Transaction for id.
   *
   * @param productId -> Transaction identifier.
   * @return a list of Transactions.
   */
  public Flux<TransactionDto> findTransactionIdPending(String productId) {
    return webClient.get()
      .uri("/api/v1/transactions/status/pending/" + productId)
      .retrieve()
      .bodyToFlux(TransactionDto.class);
  }

  /**
   * Method to update Transaction.
   *
   * @param transactionDto -> Object Transaction .
   * @return object of Transactions.
   */
  public Mono<TransactionDto> updateTransaction(TransactionDto transactionDto) {
    return webClient.put()
      .uri("/api/v1/transactions/coinpurse/" + transactionDto.getId())
      .body(Mono.just(transactionDto), TransactionDto.class)
      .retrieve()
      .bodyToMono(TransactionDto.class);
  }

}
