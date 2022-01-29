package bootcamp.com.yankims.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableWebFlux
public class WebClientConfig implements WebFluxConfigurer {

  @Value("${api.gateway.uri}")
  private String uri;

  /**
   * Method to connect with other microservice.
   *
   * @return connection microservice.
   */
  @Bean
  public WebClient getWebClient() {
    return WebClient.builder()
      .baseUrl(uri)
      .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .build();
  }
}
