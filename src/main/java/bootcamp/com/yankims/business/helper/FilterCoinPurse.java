package bootcamp.com.yankims.business.helper;

import bootcamp.com.yankims.model.CoinPurse;
import bootcamp.com.yankims.model.dto.CoinPurseDto;
import bootcamp.com.yankims.model.dto.ProductDto;
import bootcamp.com.yankims.utils.ConstantCoinType;
import bootcamp.com.yankims.utils.ConstantStatus;
import bootcamp.com.yankims.utils.ConstantsProducts;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class FilterCoinPurse {

  @Autowired
  private WebClientProductHelper webClientProductHelper;

  /**
   * Method to create object and setter attributes coin purse.
   *
   * @param coinPurseDto -> object sending for user.
   * @return object complete for save.
   */
  public Mono<CoinPurseDto> createObjectCoinPurse(CoinPurseDto coinPurseDto) {
    Mono<CoinPurseDto> newCoinPurse;
    if (coinPurseDto.getCoinPurseType().equalsIgnoreCase(ConstantCoinType.COIN_PURSE_CARD.name())
      && coinPurseDto.getAccountNumber() != null) {
      Flux<ProductDto> productDtoFlux = webClientProductHelper.findProductAccount(coinPurseDto.getAccountNumber());
      Mono<List<ProductDto>> listMono = productDtoFlux.collectList();
      newCoinPurse = listMono.flatMap(productDtos -> searchProductAccount(productDtos, coinPurseDto));
    } else {
      coinPurseDto.setAccountNumber(null);
      newCoinPurse = Mono.just(coinPurseDto);
    }
    return newCoinPurse;
  }

  /**
   * Method to update object coin purse.
   *
   * @param coinPurseDto  -> object sending for user.
   * @param findCoinPurse -> object fined for id.
   * @return object updating.
   */
  public Mono<CoinPurseDto> updateObjectCoinPurse(CoinPurseDto coinPurseDto, CoinPurse findCoinPurse) {
    coinPurseDto.setId(findCoinPurse.getId());
    coinPurseDto.setCoinPurseType(findCoinPurse.getCoinPurseType());
    coinPurseDto.setAccountNumber(findCoinPurse.getAccountNumber());
    coinPurseDto.setPhoneNumber(findCoinPurse.getPhoneNumber());
    coinPurseDto.setPhoneImei(findCoinPurse.getPhoneImei());
    return Mono.just(coinPurseDto);
  }

  /**
   * Method to change status coin purse.
   *
   * @param findCoinPurse -> object fined for id.
   * @return object with change status.
   */
  public Mono<CoinPurse> deleteObjectCoinPurse(CoinPurse findCoinPurse) {
    findCoinPurse.setStatus(ConstantStatus.INACTIVE.name());
    return Mono.just(findCoinPurse);
  }

  /**
   * Method to search product and assigned account number at coin purse.
   *
   * @param productDtos  -> list product search.
   * @param coinPurseDto -> object sending for user.
   * @return object coin purse setting with account number.
   */
  public Mono<CoinPurseDto> searchProductAccount(List<ProductDto> productDtos, CoinPurseDto coinPurseDto) {
    Mono<CoinPurseDto> coinPurseDtoMono = Mono.just(new CoinPurseDto());
    for (ProductDto productDto : productDtos) {
      if (productDto.getLevel() == 1
        && Arrays.stream(ConstantsProducts.values())
        .anyMatch(constantsProducts -> constantsProducts.toString()
          .equalsIgnoreCase(productDto.getAccountType()))) {
        coinPurseDto.setAccountNumber(productDto.getSubAccountNumber());
        coinPurseDto.setAmount(productDto.getAmount());
        coinPurseDtoMono = Mono.just(coinPurseDto);
        break;
      }
    }
    return coinPurseDtoMono;
  }

}
