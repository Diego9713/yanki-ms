package bootcamp.com.yankims.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "coin_purse")
public class CoinPurse {
  @Id
  private String id;
  @Field(name = "document_type")
  private String documentType;
  @Field(name = "document_number")
  private String documentNumber;
  @Field(name = "coin-purse_type")
  private String coinPurseType;
  @Field(name = "first_name")
  private String firstName;
  @Field(name = "last_name")
  private String lastName;
  @Field(name = "phone_number")
  private String phoneNumber;
  @Field(name = "phone_imei")
  private String phoneImei;
  @Field(name = "email")
  private String email;
  @Field(name = "account_number")
  private String accountNumber;
  @Field(name = "amount")
  private double amount;
  @Field(name = "status")
  private String status;
}
