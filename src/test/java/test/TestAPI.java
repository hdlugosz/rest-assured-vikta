package test;

import entity.Address;
import entity.PaymentCard;
import entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import org.apache.logging.log4j.*;

import static entityAPI.AddressAPI.*;
import static entityAPI.PaymentCardAPI.*;
import static entityAPI.UserAPI.*;
import static io.restassured.RestAssured.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestAPI {
    private static final Logger logger = LogManager.getLogger(TestAPI.class);
    private final ConfigLoader properties = ConfigLoader.getInstance();

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

        RequestSpecification request = given()
                .param("id", address.getId());

        Response response = getAddress(request);

        Assertions.assertEquals(address.getAddressNickname(), response.jsonPath().getString(addressNicknameField));
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertTrue(db.existsAddress(address.getId()));
    }

    @Test
    public void validateUserExistence() {
        String emailField = "email";
        User user = User.builder().withTestValues().randomId().build();
        db.addNewUser(user);
        usersToCleanUp.add(user.getId());

        RequestSpecification request = given()
                .param("id", user.getId());

        Response response = getUser(request);

        Assertions.assertEquals(user.getEmail(), response.jsonPath().get(emailField));
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertTrue(db.existsUser(user.getId()));
    }

    @Test
    public void validateAddingUser() throws JsonProcessingException {
        User user = User.builder().withTestValues().build();

        RequestSpecification request = given()
                .header("Content-Type", "application/json")
                .body(user.toJsonString());

        Response response = postUser(request);

        Assertions.assertEquals(201, response.statusCode());
        Assertions.assertTrue(db.existsUser(db.selectUserIdByLogin(user.getLoginName())));
        logger.debug("created user: " + db.selectUserIdByLogin(user.getLoginName()));
        usersToCleanUp.add(db.selectUserIdByLogin(user.getLoginName()));
    }

    @Test
    public void validateDeletingUser() {
        User user = User.builder().withTestValues().randomId().build();
        db.addNewUser(user);
        usersToCleanUp.add(user.getId());

        RequestSpecification request = given()
                .param("id", user.getId());

        Response response = deleteUser(request);

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertFalse(db.existsUser(user.getId()));
    }

    @Test
    public void validateDeletingUserThatDoesntExist() {
        int id = generateID();

        RequestSpecification request = given()
                .param("id", id);

        Response response = deleteUser(request);

        Assertions.assertEquals(404, response.statusCode());
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

        RequestSpecification request = given()
                .header("Content-Type", "application/json")
                .body(paymentCard.toJsonString());

        Response response = putPaymentCard(request);

        Assertions.assertEquals(newExpirationDate, response.jsonPath().getString(expirationDateField));
        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    public void validateDeletingPaymentCardThatDoesntExist() {
        int id = generateID();

        RequestSpecification request = given().param("id", id);

        Response response = getPaymentCard(request);

        Assertions.assertEquals(404, response.statusCode());
        Assertions.assertFalse(db.existsPaymentCard(id));
    }

    @Test
    public void validateFindingExistingUserBySurname() {
        User user = User.builder().withTestValues().randomId().build();
        db.addNewUser(user);
        usersToCleanUp.add(user.getId());

        RequestSpecification request = given()
                .param("surname", user.getSurname());

        Response response = getUserListUsingSurname(request);

        Assertions.assertTrue(response.body().asString().contains(user.getSurname()));
        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    public void validateFindingUserBySurnameThatDoesntExist() {
        String invalidSurname = "xyz";

        RequestSpecification request = given()
                .param("surname", invalidSurname);

        Response response = getUserListUsingSurname(request);

        Assertions.assertFalse(response.body().asString().contains(invalidSurname));
        Assertions.assertEquals(200, response.statusCode());
    }
}
