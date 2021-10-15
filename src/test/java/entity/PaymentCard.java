package entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import static test.Utils.generateRandomFourDigitNumber;

@Builder
@Data
@AllArgsConstructor
public class PaymentCard {
    private String cardCode;
    private String cardNickName;
    private String cardNumber;
    private String expirationDate;
    private int id;
    private String ownerName;
    private int userId;

    public PaymentCard() {
    }

    public static class PaymentCardBuilder {
        public PaymentCardBuilder withTestValues() {
            this.cardCode("375")
                    .cardNickName("testCardNickname")
                    .cardNumber("testCardNumber")
                    .expirationDate("2021-12-20")
                    .id(7777)
                    .ownerName("testOwnerName")
                    .userId(0);
            return this;
        }

        public PaymentCardBuilder randomId() {
            return this.id(generateRandomFourDigitNumber());
        }
    }
}
