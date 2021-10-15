package entityAPI;

import entity.PaymentCard;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import test.ConfigLoader;

public class PaymentCardAPI extends BaseAPI {
    private static final ConfigLoader properties = ConfigLoader.getInstance();
    private static final String PAYMENT_CARD_ENDPOINT = properties.getPropertyValue("PAYMENT_CARD_ENDPOINT");

    public static Response getPaymentCard(PaymentCard paymentCard) {
        RequestSpecification request = setBaseUri()
                .param("id", paymentCard.getId());

        return request.delete(PAYMENT_CARD_ENDPOINT)
                .then()
                .extract()
                .response();
    }

    public static Response putPaymentCard(PaymentCard paymentCard) {
        RequestSpecification request = setBaseUri()
                .header("Content-Type", "application/json")
                .body(paymentCard);

        return request.put(PAYMENT_CARD_ENDPOINT)
                .then()
                .extract()
                .response();
    }
}
