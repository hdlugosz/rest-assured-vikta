package entityAPI;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import test.ConfigLoader;

import static io.restassured.RestAssured.given;

public class PaymentCardAPI {
    private static final ConfigLoader properties = ConfigLoader.getInstance();
    private static final String baseURI = properties.getPropertyValue("baseURI");
    private static final String PAYMENT_CARD_ENDPOINT = properties.getPropertyValue("PAYMENT_CARD_ENDPOINT");

    public static Response getPaymentCard(RequestSpecification request) {
        return given()
                .spec(request)
                .when()
                .delete(baseURI + PAYMENT_CARD_ENDPOINT);
    }

    public static Response putPaymentCard(RequestSpecification request) {
        return given()
                .spec(request)
                .when()
                .put(baseURI + PAYMENT_CARD_ENDPOINT)
                .then()
                .extract()
                .response();
    }
}
