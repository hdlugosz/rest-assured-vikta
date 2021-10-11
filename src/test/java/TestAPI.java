import Entity.Address;
import Entity.PaymentCard;
import Entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import org.apache.logging.log4j.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestAPI {
    private static final Logger logger = LogManager.getLogger(TestAPI.class);

    private final ConfigLoader properties = ConfigLoader.getInstance();
    private final String baseURI = properties.getPropertyValue("baseURI");
    private final String ADDRESS_ENDPOINT = properties.getPropertyValue("ADDRESS_ENDPOINT");
    private final String USER_ENDPOINT = properties.getPropertyValue("USER_ENDPOINT");
    private final String PAYMENT_CARD_ENDPOINT = properties.getPropertyValue("PAYMENT_CARD_ENDPOINT");

    private final DatabaseController db = new DatabaseController(
            properties.getPropertyValue("dbURL"),
            properties.getPropertyValue("dbUsername"),
            properties.getPropertyValue("dbPassword")
    );

    private final ArrayList<Integer> usersToCleanUp = new ArrayList<>();
    private final ArrayList<Integer> addressesToCleanUp = new ArrayList<>();
    private final ArrayList<Integer> paymentCardsToCleanUp = new ArrayList<>();

    public int generateID() {
        return (int) ((Math.random() * (9999 - 1000)) + 1000);
    }

    @AfterAll
    public void cleanUpDB() {
        for (int id : usersToCleanUp) {
            db.deleteUserById(id);
        }
        for (int id : paymentCardsToCleanUp) {
            db.deletePaymentCardById(id);
        }
        for (int id : addressesToCleanUp) {
            db.deleteAddressById(id);
        }
    }

    @Test
    public void validateAddressExistence() {
        String addressNicknameField = "addressNickname";
        Address address = Address.builder().withTestValues().randomId().build();

        db.addNewAddress(address);
        addressesToCleanUp.add(address.getId());

        given().param("id", address.getId()).
                when().get(baseURI + ADDRESS_ENDPOINT).
                then().statusCode(200).and().body(addressNicknameField, equalTo(address.getAddressNickname()));

        Assertions.assertTrue(db.existsAddress(address.getId()));
    }

    @Test
    public void validateUserExistence() {
        String emailField = "email";
        User user = User.builder().withTestValues().randomId().build();

        db.addNewUser(user);
        usersToCleanUp.add(user.getId());

        given().param("id", user.getId()).
                when().get(baseURI + USER_ENDPOINT).
                then().statusCode(200).and().body(emailField, equalTo(user.getEmail()));

        Assertions.assertTrue(db.existsUser(user.getId()));
    }

    @Test
    public void validateAddingUser() throws JsonProcessingException {
        User user = User.builder().withTestValues().build();

        given().header("Content-Type", "application/json").body(user.toJsonString()).
                when().post(baseURI + USER_ENDPOINT).
                then().statusCode(201);

        Assertions.assertTrue(db.existsUser(db.selectUserIdByLogin(user.getLoginName())));

        logger.debug("created user: " + db.selectUserIdByLogin(user.getLoginName()));
        usersToCleanUp.add(db.selectUserIdByLogin(user.getLoginName()));
    }

    @Test
    public void validateDeletingUser() {
        User user = User.builder().withTestValues().randomId().build();

        db.addNewUser(user);
        usersToCleanUp.add(user.getId());

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
    public void validateUpdatingPaymentCard() throws JsonProcessingException {
        String expirationDateField = "expirationDate";
        String newExpirationDate = "2023-10-31";
        PaymentCard paymentCard = PaymentCard.builder().withTestValues().randomId().build();

        db.addNewPaymentCard(paymentCard);
        paymentCardsToCleanUp.add(paymentCard.getId());

        paymentCard.setExpirationDate(newExpirationDate);

        given().header("Content-Type", "application/json").body(paymentCard.toJsonString()).
                when().put(baseURI + PAYMENT_CARD_ENDPOINT).
                then().statusCode(200).and().body(expirationDateField, equalTo(newExpirationDate));
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
