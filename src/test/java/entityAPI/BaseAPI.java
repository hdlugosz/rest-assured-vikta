package entityAPI;

import io.restassured.specification.RequestSpecification;
import config.ConfigLoader;

import static io.restassured.RestAssured.given;

public abstract class BaseAPI {
    private static final ConfigLoader properties = ConfigLoader.getInstance();
    private static final String baseURI = properties.getPropertyValue("baseURI");

    protected static RequestSpecification setBaseUri() {
        return given().baseUri(baseURI);
    }
}
