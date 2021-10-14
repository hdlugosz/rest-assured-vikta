package Entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import static test.Utils.generateID;

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
            return this.id(generateID());
        }
    }

    public String toJsonString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }
}
