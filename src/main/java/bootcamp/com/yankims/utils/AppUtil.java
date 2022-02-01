package bootcamp.com.yankims.utils;

import bootcamp.com.yankims.model.CoinPurse;
import bootcamp.com.yankims.model.dto.CoinPurseDto;
import org.springframework.beans.BeanUtils;

public class AppUtil {

  private AppUtil() {
  }

  /**
   * Method to modify the return of data.
   *
   * @param coinPurse -> is object with data complete.
   * @return object coin purse modified.
   */
  public static CoinPurseDto entityToCoinPurse(CoinPurse coinPurse) {
    CoinPurseDto coinPurseDto = new CoinPurseDto();
    BeanUtils.copyProperties(coinPurse, coinPurseDto);
    return coinPurseDto;
  }

  /**
   * Method to modify the return of data.
   *
   * @param coinPurseDto -> is object with data complete.
   * @return object coin purse modified.
   */
  public static CoinPurse coinPurseToEntity(CoinPurseDto coinPurseDto) {
    CoinPurse coinPurse = new CoinPurse();
    BeanUtils.copyProperties(coinPurseDto, coinPurse);
    return coinPurse;
  }

  /**
   * Method to convert String in double.
   *
   * @return a double object amount.
   */
  public static double convertStringToDouble(String amount) {
    return Double.parseDouble(amount);
  }
}
