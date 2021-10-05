package test;

import io.restassured.response.Response;
import entity.Address;
import entity.PaymentCard;
import entity.User;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import org.apache.logging.log4j.*;

import static entityAPI.UserAPI.*;
import static entityAPI.AddressAPI.*;
import static entityAPI.PaymentCardAPI.*;

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
        Address address = Address.builder().withTestValues().randomId().build();
        db.addNewAddress(address);
        addressesToCleanUp.add(address.getId());

        Response response = getAddress(address);

        Address responseAddress = response.as(Address.class);
        Assertions.assertEquals(address.getAddressNickname(), responseAddress.getAddressNickname());
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertTrue(db.existsAddress(address.getId()));
    }

    @Test
    public void validateUserExistence() {
        User user = User.builder().withTestValues().randomId().build();
        db.addNewUser(user);
        usersToCleanUp.add(user.getId());

        Response response = getUser(user);

        User responseUser = response.as(User.class);
        Assertions.assertEquals(user.getEmail(), responseUser.getEmail());
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertTrue(db.existsUser(user.getId()));
    }

    @Test
    public void validateAddingUser() {
        User user = User.builder().withTestValues().build();

        Response response = postUser(user);

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

        Response response = deleteUser(user);

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertFalse(db.existsUser(user.getId()));
    }

    @Test
    public void validateDeletingUserThatDoesntExist() {
        User user = User.builder().withTestValues().randomId().build();

        Response response = deleteUser(user);

        Assertions.assertEquals(404, response.statusCode());
        Assertions.assertFalse(db.existsUser(user.getId()));
    }

    @Test
    public void validateUpdatingPaymentCard() {
        String newExpirationDate = "2023-10-31";
        PaymentCard paymentCard = PaymentCard.builder().withTestValues().randomId().build();
        db.addNewPaymentCard(paymentCard);
        paymentCardsToCleanUp.add(paymentCard.getId());
        paymentCard.setExpirationDate(newExpirationDate);

        Response response = putPaymentCard(paymentCard);

        PaymentCard responsePaymentCard = response.as(PaymentCard.class);
        Assertions.assertEquals(newExpirationDate, responsePaymentCard.getExpirationDate());
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
        usersToCleanUp.add(user.getId());

        Response response = getUserListUsingSurname(user);

        ArrayList responseUserList = response.as(ArrayList.class);
        Assertions.assertFalse(responseUserList.isEmpty());
        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    public void validateThatFindingUserThatDoesntExistReturnsEmptyBodyResponse() {
        User user = User.builder().withTestValues().randomId().surname("xyz").build();

        Response response = getUserListUsingSurname(user);

        ArrayList responseUserList = response.as(ArrayList.class);
        Assertions.assertTrue(responseUserList.isEmpty());
        Assertions.assertEquals(200, response.statusCode());
    }
}
