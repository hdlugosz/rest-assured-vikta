package entityAPI;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import test.ConfigLoader;

import static io.restassured.RestAssured.given;

public class UserAPI {
    private static final ConfigLoader properties = ConfigLoader.getInstance();
    private static final String baseURI = properties.getPropertyValue("baseURI");
    private static final String USER_ENDPOINT = properties.getPropertyValue("USER_ENDPOINT");
    private static final String USER_SURNAME_ENDPOINT = properties.getPropertyValue("USER_SURNAME_ENDPOINT");

    public static Response getUser(RequestSpecification request) {
        return given()
                .spec(request)
                .when()
                .get(baseURI + USER_ENDPOINT)
                .then()
                .extract()
                .response();
    }

    public static Response postUser(RequestSpecification request) {
        return given()
                .spec(request)
                .when()
                .post(baseURI + USER_ENDPOINT)
                .then()
                .extract()
                .response();
    }

    public static Response deleteUser(RequestSpecification request) {
        return given()
                .spec(request)
                .when()
                .delete(baseURI + USER_ENDPOINT)
                .then()
                .extract()
                .response();
    }

    public static Response getUserListUsingSurname(RequestSpecification request) {
        return given()
                .spec(request)
                .when()
                .get(baseURI + USER_SURNAME_ENDPOINT);
    }
}
