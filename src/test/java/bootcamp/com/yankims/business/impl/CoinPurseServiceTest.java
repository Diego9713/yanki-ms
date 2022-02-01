package bootcamp.com.yankims.business.impl;

import bootcamp.com.yankims.business.helper.FilterCoinPurse;
import bootcamp.com.yankims.model.CoinPurse;
import bootcamp.com.yankims.model.dto.CoinPurseDto;
import bootcamp.com.yankims.repository.ICoinPurseRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class CoinPurseServiceTest {

  @Autowired
  private CoinPurseService coinPurseService;

  @MockBean
  private ICoinPurseRepository coinPurseRepository;

  @MockBean
  private FilterCoinPurse filterCoinPurse;

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
    coinPurse.getId();
    coinPurse.getCoinPurseType();
    coinPurse.getAmount();
    coinPurse.getStatus();
    coinPurse.getPhoneNumber();
    coinPurse.getPhoneImei();
    coinPurse.getDocumentNumber();
    coinPurse.getDocumentType();
    coinPurse.getEmail();
    coinPurse.getFirstName();
    coinPurse.getLastName();
    coinPurse.getAccountNumber();
    coinPurse.getAccountNumber();
  }

  @Test
  void findAllCoinPurse() {
    when(coinPurseRepository.findAll()).thenReturn(Flux.just(coinPurse));
    Flux<CoinPurseDto> allCoinPurse = coinPurseService.findAllCoinPurse();
    StepVerifier
      .create(allCoinPurse)
      .consumeNextWith(newCoin -> Assertions.assertEquals(status,newCoin.getStatus()))
      .expectComplete();
  }

  @Test
  void findOneCoinPurse() {
    when(coinPurseRepository.findById(id)).thenReturn(Mono.just(coinPurse));
    Mono<CoinPurseDto> allCoinPurse = coinPurseService.findOneCoinPurse(id);
    StepVerifier
      .create(allCoinPurse)
      .consumeNextWith(newCoin -> Assertions.assertEquals(status,newCoin.getStatus()))
      .expectComplete();
  }

  @Test
  void createCoinPurse() {
    when(coinPurseRepository.findByPhoneNumberOrPhoneImeiAndDocumentNumberOrAccountNumber(coinPurseDto.getPhoneNumber(),
      coinPurseDto.getPhoneImei(),
      coinPurseDto.getDocumentNumber(),
      coinPurseDto.getAccountNumber()))
      .thenReturn(Mono.just(new CoinPurse()));
    when(filterCoinPurse.createObjectCoinPurse(coinPurseDto)).thenReturn(Mono.just(coinPurseDto));
    when(coinPurseRepository.save(coinPurse)).thenReturn(Mono.just(coinPurse));

    Mono<CoinPurseDto> coinPurseDtoMono = coinPurseService.createCoinPurse(coinPurseDto);
    StepVerifier
      .create(coinPurseDtoMono)
      .expectNext(coinPurseDto)
      .expectComplete();
  }

  @Test
  void updateCoinPurse() {
    when(coinPurseRepository.findById(id)).thenReturn(Mono.just(coinPurse));
    when(filterCoinPurse.updateObjectCoinPurse(coinPurseDto,coinPurse)).thenReturn(Mono.just(coinPurseDto));
    when(coinPurseRepository.save(coinPurse)).thenReturn(Mono.just(coinPurse));

    Mono<CoinPurseDto> coinPurseDtoMono = coinPurseService.updateCoinPurse(coinPurseDto, id);
    StepVerifier
      .create(coinPurseDtoMono)
      .expectNext(coinPurseDto)
      .expectComplete();
  }

  @Test
  void deleteCoinPurse() {
    when(coinPurseRepository.findById(id)).thenReturn(Mono.just(coinPurse));
    when(filterCoinPurse.deleteObjectCoinPurse(coinPurse)).thenReturn(Mono.just(coinPurse));
    when(coinPurseRepository.save(coinPurse)).thenReturn(Mono.just(coinPurse));

    Mono<CoinPurseDto> coinPurseDtoMono = coinPurseService.deleteCoinPurse(id);
    StepVerifier
      .create(coinPurseDtoMono)
      .expectNext(coinPurseDto)
      .expectComplete();
  }

  @Test
  void findTransactionPending() {
  }

  @Test
  void confirmBuyCoinsTransaction() {
  }
}