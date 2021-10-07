import org.json.simple.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestAPI {
    private String baseURI;
    private String ADDRESS_ENDPOINT;
    private String USER_ENDPOINT;
    private String PAYMENT_CARD_ENDPOINT;

    private DatabaseController db;

    public int generateID() {
        return ThreadLocalRandom.current().nextInt(999, 10000);
    }

    @BeforeAll
    public void initialize() throws IOException {
        ConfigLoader properties = new ConfigLoader();
        baseURI = properties.getPropertyValue("baseURI");
        ADDRESS_ENDPOINT = properties.getPropertyValue("ADDRESS_ENDPOINT");
        USER_ENDPOINT = properties.getPropertyValue("USER_ENDPOINT");
        PAYMENT_CARD_ENDPOINT = properties.getPropertyValue("PAYMENT_CARD_ENDPOINT");
        String dbURL = properties.getPropertyValue("dbURL");
        String dbUsername = properties.getPropertyValue("dbUsername");
        String dbPassword = properties.getPropertyValue("dbPassword");

        db = new DatabaseController(dbURL, dbUsername, dbPassword);
    }

    @Test
    public void validateAddressExistence() {
        String addressNicknameField = "addressNickname";
        Address address = Address.generateRandomAddress();

        db.addNewAddress(address);

        given().param("id", address.getId()).
                when().get(baseURI + ADDRESS_ENDPOINT).
                then().statusCode(200).and().body(addressNicknameField, equalTo(address.getAddressNickname()));

        Assertions.assertTrue(db.existsAddress(address.getId()));

        db.deleteAddressById(address.getId());
    }

    @Test
    public void validateUserExistence() {
        String emailField = "email";
        User user = User.generateRandomUser();

        db.addNewUser(user);

        given().param("id", user.getId()).
                when().get(baseURI + USER_ENDPOINT).
                then().statusCode(200).and().body(emailField, equalTo(user.getEmail()));

        Assertions.assertTrue(db.existsUser(user.getId()));

        db.deleteUserById(user.getId());
    }

    @Test
    public void validateAddingUser() {
        User user = User.generateRandomUser();

        JSONObject request = new JSONObject();
        request.put("addressIds", null);
        request.put("email", user.getEmail());
        request.put("firstName", user.getFirstName());
        request.put("id", null);
        request.put("loginName", user.getLoginName());
        request.put("middleName", user.getMiddleName());
        request.put("password", user.getPassword());
        request.put("pathToAvatarImage", user.getPathToAvatarImage());
        request.put("paymentCardIds", null);
        request.put("surname", user.getSurname());

        given().header("Content-Type", "application/json").body(request.toJSONString()).
                when().post(baseURI + USER_ENDPOINT).
                then().statusCode(201);

        Assertions.assertTrue(db.existsUser(db.selectUserIdByLogin(user.getLoginName())));

        db.deleteUserById(db.selectUserIdByLogin(user.getLoginName()));
    }

    @Test
    public void validateDeletingUser() {
        User user = User.generateRandomUser();

        db.addNewUser(user);

        given().param("id", user.getId()).
                when().delete(baseURI + USER_ENDPOINT).
                then().statusCode(200);

        Assertions.assertFalse(db.existsUser(user.getId()));
    }

    @Test
    public void validateDeletingUserThatDoesntExist() {
        int id = generateID();

        given().param("id", id).
                when().delete(baseURI + USER_ENDPOINT).
                then().statusCode(404);

        Assertions.assertFalse(db.existsUser(id));
    }

    @Test
    public void validateUpdatingPaymentCard() {
        String expirationDateField = "expirationDate";
        PaymentCard paymentCard = PaymentCard.generateRandomPaymentCard();
        String newExpirationDate = PaymentCard.generateExpirationDate();

        db.addNewPaymentCard(paymentCard);

        JSONObject request = new JSONObject();
        request.put("cardCode", paymentCard.getCardCode());
        request.put("cardNickName", paymentCard.getCardNickName());
        request.put("cardNumber", paymentCard.getCardNumber());
        request.put("expirationDate", newExpirationDate);
        request.put("id", paymentCard.getId());
        request.put("ownerName", paymentCard.getOwnerName());
        request.put("userId", null);

        given().header("Content-Type", "application/json").body(request.toJSONString()).
                when().put(baseURI + PAYMENT_CARD_ENDPOINT).
                then().statusCode(200).and().body(expirationDateField, equalTo(newExpirationDate));

        db.deletePaymentCardById(paymentCard.getId());
    }

    @Test
    public void validateDeletingPaymentCardThatDoesntExist() {
        int id = generateID();

        given().param("id", id).
                when().delete(baseURI + PAYMENT_CARD_ENDPOINT).
                then().statusCode(404);

        Assertions.assertFalse(db.existsPaymentCard(id));
    }
}
