package bootcamp.com.yankims.business.helper;

import bootcamp.com.yankims.model.CoinPurse;
import bootcamp.com.yankims.model.dto.CoinPurseDto;
import bootcamp.com.yankims.model.dto.ProductDto;
import bootcamp.com.yankims.model.dto.TransactionDto;
import bootcamp.com.yankims.utils.AppUtil;
import bootcamp.com.yankims.utils.ConstantCoinType;
import bootcamp.com.yankims.utils.ConstantStatus;
import bootcamp.com.yankims.utils.ConstantsProducts;
import bootcamp.com.yankims.utils.ConstantsTransacStatus;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class FilterCoinPurse {

  @Value("${rate.buy}")
  private String rateBuy;

  @Value("${rate.sell}")
  private String rateSell;

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
      CoinPurseDto purseDto = assignBootCoins(rateBuy, coinPurseDto);
      newCoinPurse = Mono.just(purseDto);
    }
    return newCoinPurse;
  }

  private CoinPurseDto assignBootCoins(String rateBuy, CoinPurseDto coinPurseDto) {
    double buy = AppUtil.convertStringToDouble(rateBuy);
    if (coinPurseDto.getAmount() > 0) {
      coinPurseDto.setBootCoin(coinPurseDto.getAmount() / buy);
    } else {
      coinPurseDto.setBootCoin(0);
    }
    return coinPurseDto;
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
        CoinPurseDto purseDto = assignBootCoins(rateBuy, coinPurseDto);
        coinPurseDtoMono = Mono.just(purseDto);
        break;
      }
    }
    return coinPurseDtoMono;
  }

  /**
   * Method to confirm Transaction for buy bootCoins.
   *
   * @param transactionDto -> object transaction confirmed.
   * @return check complete transaction.
   */
  public TransactionDto confirmTransaction(TransactionDto transactionDto) {
    transactionDto.setStatus(ConstantsTransacStatus.COMPLETE.name());
    return transactionDto;
  }

}
