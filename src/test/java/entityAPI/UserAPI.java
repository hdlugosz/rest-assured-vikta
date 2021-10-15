package entityAPI;

import entity.User;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import config.ConfigLoader;

public class UserAPI extends BaseAPI {
    private static final ConfigLoader properties = ConfigLoader.getInstance();
    private static final String USER_ENDPOINT = properties.getPropertyValue("USER_ENDPOINT");
    private static final String USER_SURNAME_ENDPOINT = properties.getPropertyValue("USER_SURNAME_ENDPOINT");

    public static Response getUser(User user) {
        RequestSpecification request = setBaseUri()
                .param("id", user.getId());

        return request.get(USER_ENDPOINT)
                .then()
                .extract()
                .response();
    }

    public static Response postUser(User user) {
        RequestSpecification request = setBaseUri()
                .header("Content-Type", "application/json")
                .body(user);

        return request.post(USER_ENDPOINT)
                .then()
                .extract()
                .response();
    }

    public static Response deleteUser(User user) {
        RequestSpecification request = setBaseUri()
                .param("id", user.getId());

        return request.delete(USER_ENDPOINT)
                .then()
                .extract()
                .response();
    }

    public static Response getUserListUsingSurname(User user) {
        RequestSpecification request = setBaseUri()
                .param("surname", user.getSurname());

        return request.get(USER_SURNAME_ENDPOINT)
                .then()
                .extract()
                .response();
    }
}
