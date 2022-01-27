package bootcamp.com.yankims.business.helper;

import bootcamp.com.yankims.model.dto.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
public class WebClientProductHelper {
  @Autowired
  private WebClient webClient;

  /**
   * Method to search product for account number.
   *
   * @param account -> product identifier.
   * @return a list of product.
   */
  public Flux<ProductDto> findProductAccount(String account) {
    return webClient.get()
      .uri("/api/v1/products/accountnumber/" + account)
      .retrieve()
      .bodyToFlux(ProductDto.class);
  }

}
