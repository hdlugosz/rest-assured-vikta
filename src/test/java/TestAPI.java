import org.json.simple.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestAPI {
    private String baseURI;
    private String ADDRESS_ENDPOINT;
    private String USER_ENDPOINT;
    private String PAYMENT_CARD_ENDPOINT;
    private String userTable;
    private String addressTable;
    private String paymentCardTable;

    private DatabaseController db;

    @BeforeAll
    public void initialize() throws IOException {
        ConfigLoader properties = new ConfigLoader();
        baseURI = properties.getPropertyValue("baseURI");
        ADDRESS_ENDPOINT = properties.getPropertyValue("ADDRESS_ENDPOINT");
        USER_ENDPOINT = properties.getPropertyValue("USER_ENDPOINT");
        PAYMENT_CARD_ENDPOINT = properties.getPropertyValue("PAYMENT_CARD_ENDPOINT");
        userTable = properties.getPropertyValue("userTable");
        addressTable = properties.getPropertyValue("addressTable");
        paymentCardTable = properties.getPropertyValue("paymentCardTable");
        String dbURL = properties.getPropertyValue("dbURL");
        String dbUsername = properties.getPropertyValue("dbUsername");
        String dbPassword = properties.getPropertyValue("dbPassword");

        db = new DatabaseController(dbURL, dbUsername, dbPassword);
    }

    @Test
    public void validateAddressExistence() {
        int id = 1000;
        String addressNicknameField = "addressNickname";
        String addressNickname = "testAddress";

        db.addNewAddress(id);

        given().param("id", id).
                when().get(baseURI + ADDRESS_ENDPOINT).
                then().statusCode(200).and().body(addressNicknameField, equalTo(addressNickname));

        db.deleteById(addressTable, id);
    }

    @Test
    public void validateUserExistence() {
        int id = 2000;
        String emailField = "email";
        String email = "testMail@gmail.com";

        db.addNewUser(id);

        given().param("id", id).
                when().get(baseURI + USER_ENDPOINT).
                then().statusCode(200).and().body(emailField, equalTo(email));

        db.deleteById(userTable, id);
    }

    @Test
    public void validateAddingUser() {
        String login = "testLogin";

        JSONObject request = new JSONObject();
        request.put("addressIds", null);
        request.put("email", "testMail@gmail.com");
        request.put("firstName", "testName");
        request.put("id", null);
        request.put("loginName", "testLogin");
        request.put("middleName", "testMiddlename");
        request.put("password", "test1234");
        request.put("pathToAvatarImage", "testPath.com");
        request.put("paymentCardIds", null);
        request.put("surname", "testSurname");

        given().header("Content-Type", "application/json").body(request.toJSONString()).
                when().post(baseURI + USER_ENDPOINT).
                then().statusCode(201);

        Assertions.assertTrue(db.exists(userTable, db.selectUserIdByLogin(login)));

        db.deleteById(userTable, db.selectUserIdByLogin(login));
    }

    @Test
    public void validateDeletingUser() {
        int id = 3000;

        db.addNewUser(id);

        given().param("id", id).
                when().delete(baseURI + USER_ENDPOINT).
                then().statusCode(200);

        Assertions.assertFalse(db.exists(userTable, id));
    }

    @Test
    public void validateDeletingUserThatDoesntExist() {
        int id = 4000;

        given().param("id", id).
                when().delete(baseURI + USER_ENDPOINT).
                then().statusCode(404);

        Assertions.assertFalse(db.exists(userTable, id));
    }

    @Test
    public void validateUpdatingPaymentCard() {
        int id = 5000;
        String expirationDateField = "expirationDate";
        String expirationDate = "2024-12-20";

        db.addNewPaymentCard(id);

        JSONObject request = new JSONObject();
        request.put("cardCode", "312");
        request.put("cardNickName", "testCardNickname");
        request.put("cardNumber", "343914624684393");
        request.put("expirationDate", "2024-12-20");
        request.put("id", 5000);
        request.put("ownerName", "testOwner");
        request.put("userId", null);

        given().header("Content-Type", "application/json").body(request.toJSONString()).
                when().put(baseURI + PAYMENT_CARD_ENDPOINT).
                then().statusCode(200).and().body(expirationDateField, equalTo(expirationDate));

        db.deleteById(paymentCardTable, id);
    }

    @Test
    public void validateDeletingPaymentCardThatDoesntExist() {
        int id = 6000;

        given().param("id", id).
                when().delete(baseURI + PAYMENT_CARD_ENDPOINT).
                then().statusCode(404);

        Assertions.assertFalse(db.exists(paymentCardTable, id));
    }
}
