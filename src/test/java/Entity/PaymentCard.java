package Entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

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

    public static class PaymentCardBuilder {
        public PaymentCard.PaymentCardBuilder withTestValues() {
            this.cardCode = "375";
            this.cardNickName = "testCardNickname";
            this.cardNumber = "testCardNumber";
            this.expirationDate = "2021-12-20";
            this.id = 7777;
            this.ownerName = "testOwnerName";
            this.userId = 0;
            return this;
        }

        public PaymentCard.PaymentCardBuilder randomId() {
            this.id = (int) ((Math.random() * (9999 - 1000)) + 1000);
            return this;
        }
    }

    public String toJsonString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }
}
