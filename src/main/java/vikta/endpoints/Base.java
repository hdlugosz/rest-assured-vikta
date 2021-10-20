package vikta.endpoints;

import io.restassured.specification.RequestSpecification;
import vikta.utils.ConfigLoader;

import static io.restassured.RestAssured.given;

public abstract class Base {
    private static final ConfigLoader properties = ConfigLoader.getInstance();
    private static final String baseURI = properties.getPropertyValue("baseURI");

    protected static RequestSpecification setBaseUri() {
        return given().baseUri(baseURI);
    }
}
