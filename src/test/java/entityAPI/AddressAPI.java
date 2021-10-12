package entityAPI;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import test.ConfigLoader;

import static io.restassured.RestAssured.given;

public class AddressAPI {
    private static final ConfigLoader properties = ConfigLoader.getInstance();
    private static final String baseURI = properties.getPropertyValue("baseURI");
    private static final String ADDRESS_ENDPOINT = properties.getPropertyValue("ADDRESS_ENDPOINT");

    public static Response getAddress(RequestSpecification request) {
        return given()
                .spec(request)
                .when()
                .get(baseURI + ADDRESS_ENDPOINT)
                .then()
                .extract()
                .response();
    }
}
