package vikta.test;

import vikta.database.DatabaseController;
import vikta.utils.ConfigLoader;
import io.restassured.response.Response;
import vikta.entities.Address;
import vikta.entities.PaymentCard;
import vikta.entities.User;
import org.junit.jupiter.api.*;

import org.apache.logging.log4j.*;

import static vikta.endpoints.UserEndpoints.*;
import static vikta.endpoints.AddressEndpoints.*;
import static vikta.endpoints.PaymentCardEndpoints.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ViktaTest {
    private static final Logger logger = LogManager.getLogger(ViktaTest.class);
    private final ConfigLoader properties = ConfigLoader.getInstance();

    private final DatabaseController db = new DatabaseController(
            properties.getPropertyValue("dbURL"),
            properties.getPropertyValue("dbUsername"),
            properties.getPropertyValue("dbPassword")
    );

    @BeforeAll
    public void setUp() {
        db.prepareDatabase();
    }

    @AfterAll
    public void tearDown() {
        db.cleanUpDatabase();
    }

    @Test
    public void validateAddressExistence() {
        Address address = Address.builder().withTestValues().randomId().build();
        db.addNewAddress(address);

        Response response = getAddress(address);

        Address responseAddress = response.as(Address.class);
        Assertions.assertEquals(address, responseAddress);
        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    public void validateUserExistence() {
        User user = User.builder().withTestValues().randomId().build();
        db.addNewUser(user);

        Response response = getUser(user);

        User responseUser = response.as(User.class);
        Assertions.assertEquals(user, responseUser);
        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    public void validateAddingUser() {
        User user = User.builder().withTestValues().build();

        Response response = postUser(user);

        User responseUser = response.as(User.class);
        Assertions.assertTrue(user.equalsExceptIDField(responseUser));
        Assertions.assertEquals(201, response.statusCode());
        Assertions.assertTrue(db.existsUser(responseUser.getId()));
        logger.debug("created user: " + responseUser.getId());
    }

    @Test
    public void validateDeletingUser() {
        User user = User.builder().withTestValues().randomId().build();
        db.addNewUser(user);

        Response response = deleteUser(user);

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertFalse(db.existsUser(user.getId()));
        logger.debug("deleted user: " + user.getId());
    }

    @Test
    public void validateDeletingUserThatDoesntExist() {
        User user = User.builder().withTestValues().randomId().build();

        Response response = deleteUser(user);

        Assertions.assertEquals(404, response.statusCode());
        Assertions.assertFalse(db.existsUser(user.getId()));
    }

    @Test
    public void validateUpdatingPaymentCardExpirationDate() {
        String newExpirationDate = "2023-10-31";
        PaymentCard paymentCard = PaymentCard.builder().withTestValues().randomId().build();
        db.addNewPaymentCard(paymentCard);
        paymentCard.setExpirationDate(newExpirationDate);

        Response response = putPaymentCard(paymentCard);

        PaymentCard responsePaymentCard = response.as(PaymentCard.class);
        Assertions.assertEquals(paymentCard, responsePaymentCard);
        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    public void validateDeletingPaymentCardThatDoesntExist() {
        PaymentCard paymentCard = PaymentCard.builder().withTestValues().randomId().build();

        Response response = getPaymentCard(paymentCard);

        Assertions.assertEquals(404, response.statusCode());
        Assertions.assertFalse(db.existsPaymentCard(paymentCard.getId()));
    }

    @Test
    public void validateFindingExistingUserBySurname() {
        User user = User.builder().withTestValues().randomId().build();
        db.addNewUser(user);

        Response response = getUserListUsingSurname(user);

        User[] responseUserArray = response.as(User[].class);
        for (User responseUser : responseUserArray) {
            Assertions.assertEquals(user.getSurname(), responseUser.getSurname());
        }
        Assertions.assertNotEquals(0, responseUserArray.length);
        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    public void validateThatFindingUserThatDoesntExistReturnsEmptyBodyResponse() {
        User user = User.builder().withTestValues().randomId().surname("xyz").build();

        Response response = getUserListUsingSurname(user);

        User[] responseUserArray = response.as(User[].class);
        Assertions.assertEquals(0, responseUserArray.length);
        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    public void validateThatCreatingUserWithInvalidEmailIsForbidden() {
        User user = User.builder().withTestValues().email("emailWithoutAtSign.com").build();

        Response response = postUser(user);

        Assertions.assertEquals(422, response.statusCode());
        Assertions.assertEquals(404, getUser(user).statusCode());
    }

    @Test
    public void validateThatCreatingUserWithEmptyNameIsForbidden() {
        User user = User.builder().withTestValues().firstName("").build();

        Response response = postUser(user);

        Assertions.assertEquals(422, response.statusCode());
        Assertions.assertEquals(404, getUser(user).statusCode());
    }

    @Test
    public void validateThatCreatingUserWithTooShortPasswordIsForbidden() {
        User user = User.builder().withTestValues().password("x").build();

        Response response = postUser(user);

        Assertions.assertEquals(422, response.statusCode());
        Assertions.assertEquals(404, getUser(user).statusCode());
    }

    @Test
    public void validateThatCreatingUserWithLoginNameThatAlreadyExistsIsForbidden() {
        User user1 = User.builder().withTestValues().loginName("existingLogin").build();
        User user2 = User.builder().withTestValues().loginName("existingLogin").build();

        Response response1 = postUser(user1);
        Response response2 = postUser(user2);

        Assertions.assertEquals(201, response1.statusCode());
        Assertions.assertEquals(409, response2.statusCode());
    }
}
