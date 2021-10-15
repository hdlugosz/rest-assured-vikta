package entityAPI;

import entity.Address;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import config.ConfigLoader;

public class AddressAPI extends BaseAPI {
    private static final ConfigLoader properties = ConfigLoader.getInstance();
    private static final String ADDRESS_ENDPOINT = properties.getPropertyValue("ADDRESS_ENDPOINT");

    public static Response getAddress(Address address) {
        RequestSpecification request = setBaseUri()
                .param("id", address.getId());

        return request.get(ADDRESS_ENDPOINT)
                .then()
                .extract()
                .response();
    }
}
