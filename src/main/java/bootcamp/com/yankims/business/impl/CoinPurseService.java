package bootcamp.com.yankims.business.impl;

import bootcamp.com.yankims.business.ICoinPurseService;
import bootcamp.com.yankims.business.helper.FilterCoinPurse;
import bootcamp.com.yankims.business.helper.WebClientTransactionHelper;
import bootcamp.com.yankims.model.CoinPurse;
import bootcamp.com.yankims.model.dto.CoinPurseDto;
import bootcamp.com.yankims.model.dto.TransactionDto;
import bootcamp.com.yankims.repository.ICoinPurseRepository;
import bootcamp.com.yankims.utils.AppUtil;
import bootcamp.com.yankims.utils.ConstantStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service("CoinPurseService")
@Slf4j
public class CoinPurseService implements ICoinPurseService {

  @Autowired
  private ICoinPurseRepository coinPurseRepository;

  @Autowired
  private FilterCoinPurse filterCoinPurse;

  @Autowired
  private WebClientTransactionHelper webClientTransactionHelper;

  /**
   * Method to search all coin purse.
   *
   * @return object list of type coin purse.
   */
  @Override
  @Transactional(readOnly = true)
  public Flux<CoinPurseDto> findAllCoinPurse() {
    log.info("findAll Coin purse >>>");
    return coinPurseRepository.findAll()
      .filter(coinPurse -> coinPurse.getStatus().equalsIgnoreCase(ConstantStatus.ACTIVE.name()))
      .map(AppUtil::entityToCoinPurse);
  }

  /**
   * Method to search one Transaction for id.
   *
   * @param id -> identify unique of Transaction.
   * @return object type Transaction.
   */
  @Override
  @Transactional(readOnly = true)
  public Flux<TransactionDto> findTransactionPending(String id) {
    log.info("findAll Transaction status pending>>>");
    return webClientTransactionHelper.findTransactionIdPending(id);
  }

  /**
   * Method to Confirm buy bootCoins transaction
   *
   * @param transactionDto -> Transaction pending.
   * @return message type string odf confirmed.
   */
  @Override
  @Transactional
  public Mono<TransactionDto> confirmBuyCoinsTransaction(TransactionDto transactionDto) {
    TransactionDto transaction = filterCoinPurse.confirmTransaction(transactionDto);
    return webClientTransactionHelper.updateTransaction(transaction);
  }

  /**
   * Method to search one coin purse for id.
   *
   * @param id -> identify unique of coin purse.
   * @return object type coin purse.
   */
  @Override
  @Transactional(readOnly = true)
  public Mono<CoinPurseDto> findOneCoinPurse(String id) {
    log.info("findOne Coin purse >>>");
    return coinPurseRepository.findById(id)
      .filter(coinPurse -> coinPurse.getStatus().equalsIgnoreCase(ConstantStatus.ACTIVE.name()))
      .map(AppUtil::entityToCoinPurse);
  }

  /**
   * Method to save coin purse.
   *
   * @param coinPurseDto -> object to save coin purse.
   * @return object saved type coin purse.
   */
  @Override
  @Transactional
  public Mono<CoinPurseDto> createCoinPurse(CoinPurseDto coinPurseDto) {
    log.info("Save Coin purse >>>");
    return coinPurseRepository
      .findByPhoneNumberOrPhoneImeiAndDocumentNumberOrAccountNumber(coinPurseDto.getPhoneNumber(),
        coinPurseDto.getPhoneImei(),
        coinPurseDto.getDocumentNumber(),
        coinPurseDto.getAccountNumber())
      .switchIfEmpty(Mono.just(new CoinPurse()))
      .filter(coinPurse -> coinPurse.getId() == null)
      .flatMap(coinPurse ->  filterCoinPurse.createObjectCoinPurse(coinPurseDto))
      .filter(c -> c.getDocumentNumber() != null)
      .map(AppUtil::coinPurseToEntity)
      .flatMap(coinPurseRepository::save)
      .map(AppUtil::entityToCoinPurse);
  }

  /**
   * Method to update coin purse.
   *
   * @param coinPurseDto -> object to save coin purse.
   * @param id           -> identify unique of coin purse.
   * @return object update coin purse.
   */
  @Override
  @Transactional
  public Mono<CoinPurseDto> updateCoinPurse(CoinPurseDto coinPurseDto, String id) {
    log.info("Update Coin purse >>>");
    return coinPurseRepository.findById(id)
      .switchIfEmpty(Mono.empty())
      .flatMap(coinPurse -> filterCoinPurse.updateObjectCoinPurse(coinPurseDto, coinPurse))
      .map(AppUtil::coinPurseToEntity)
      .flatMap(coinPurseRepository::save)
      .map(AppUtil::entityToCoinPurse);
  }

  /**
   * Method to delete coin purse.
   *
   * @param id -> identify unique of coin purse.
   * @return object modified type coin purse.
   */
  @Override
  @Transactional
  public Mono<CoinPurseDto> deleteCoinPurse(String id) {
    log.info("Delete Coin purse >>>");
    return coinPurseRepository.findById(id)
      .switchIfEmpty(Mono.empty())
      .flatMap(filterCoinPurse::deleteObjectCoinPurse)
      .flatMap(coinPurseRepository::save)
      .map(AppUtil::entityToCoinPurse);
  }
}
