package bootcamp.com.yankims.expose;

import bootcamp.com.yankims.business.impl.CoinPurseService;
import bootcamp.com.yankims.model.CoinPurse;
import bootcamp.com.yankims.model.dto.CoinPurseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "20000")
class CoinPurseControllerTest {

  @Autowired
  private CoinPurseController coinPurseController;

  @Autowired
  private WebTestClient webTestClient;

  @MockBean
  private CoinPurseService coinPurseService;

  private static final CoinPurse coinPurse = new CoinPurse();
  private static final CoinPurseDto coinPurseDto = new CoinPurseDto();
  private static final String id = "61f1b369af0cb800363bb885";
  private static final String documentType = "dni";
  private static final String documentNumber = "71528107312";
  private static final String coinPurseType = "COIN_PURSE_DNI";
  private static final String firstName = "Pedro";
  private static final String lastName = "Ramirez";
  private static final String phoneNumber = "87695245";
  private static final String phoneImei = "MXHFYT856";
  private static final String email = "tafur232321@gmail.com";
  private static final String accountNumber = "45fca7df-9c56-46aa-a333-3a272df1be8b";
  private static final double amount = 2400;
  private static final String status = "ACTIVE";

  @BeforeAll
  static void setUp() {
    coinPurseDto.setId(id);
    coinPurseDto.setCoinPurseType(coinPurseType);
    coinPurseDto.setDocumentType(documentType);
    coinPurseDto.setDocumentNumber(documentNumber);
    coinPurseDto.setFirstName(firstName);
    coinPurseDto.setLastName(lastName);
    coinPurseDto.setPhoneImei(phoneImei);
    coinPurseDto.setPhoneNumber(phoneNumber);
    coinPurseDto.setEmail(email);
    coinPurseDto.setAccountNumber(accountNumber);
    coinPurseDto.setAmount(amount);
    coinPurseDto.setStatus(status);
    BeanUtils.copyProperties(coinPurseDto, coinPurse);
  }

  @Test
  @DisplayName("GET -> /api/v1/yankis")
  void findAllCoinPurse() {
    when(coinPurseService.findAllCoinPurse()).thenReturn(Flux.just(coinPurseDto));

    WebTestClient.ResponseSpec responseSpec = webTestClient
      .get()
      .uri("/api/v1/yankis")
      .accept(MediaType.APPLICATION_JSON)
      .exchange();

    responseSpec.expectStatus().isOk()
      .expectHeader().contentType(MediaType.APPLICATION_JSON);
  }

  @Test
  @DisplayName("GET -> /api/v1/yankis/{id}")
  void findOneCoinPurse() {
    when(coinPurseService.findOneCoinPurse(id)).thenReturn(Mono.just(coinPurseDto));

    WebTestClient.ResponseSpec responseSpec = webTestClient
      .get()
      .uri("/api/v1/yankis/"+id)
      .accept(MediaType.APPLICATION_JSON)
      .exchange();

    responseSpec.expectStatus().isOk()
      .expectHeader().contentType(MediaType.APPLICATION_JSON);
    responseSpec.expectBody()
      .jsonPath("$.id").isEqualTo(coinPurseDto.getId());
  }

  @Test
  @DisplayName("POST -> /api/v1/yankis")
  void createCoinPurse() {
    when(coinPurseService.createCoinPurse(coinPurseDto)).thenReturn(Mono.just(coinPurseDto));
    Assertions.assertNotNull(coinPurseController.createCoinPurse(coinPurseDto));
  }

  @Test
  @DisplayName("PUT -> /api/v1/yankis/{id}")
  void updateCoinPurse() {
    when(coinPurseService.updateCoinPurse(coinPurseDto,id)).thenReturn(Mono.just(coinPurseDto));
    Assertions.assertNotNull(coinPurseController.updateCoinPurse(coinPurseDto, id));
  }

  @Test
  @DisplayName("DELETE -> /api/v1/yankis/{id}")
  void deleteCoinPurse() {
    when(coinPurseService.deleteCoinPurse(id)).thenReturn(Mono.just(coinPurseDto));

    WebTestClient.ResponseSpec responseSpec = webTestClient
      .delete()
      .uri("/api/v1/yankis/"+id)
      .accept(MediaType.APPLICATION_JSON)
      .exchange();

    responseSpec.expectStatus().isOk()
      .expectHeader().contentType(MediaType.APPLICATION_JSON);
    responseSpec.expectBody()
      .jsonPath("$.id").isEqualTo(coinPurseDto.getId());
  }

  @Test
  void fallBackPostCoinPurse() {
    Assertions.assertNotNull(coinPurseController.fallBackPostCoinPurse(coinPurseDto, new RuntimeException("")));
  }
}