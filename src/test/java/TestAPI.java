import Entity.Address;
import Entity.PaymentCard;
import Entity.User;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.ArrayList;
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
    private final ArrayList<Integer> usersToCleanUp = new ArrayList<>();
    private final ArrayList<Integer> addressesToCleanUp = new ArrayList<>();
    private final ArrayList<Integer> paymentCardsToCleanUp = new ArrayList<>();

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

    @AfterAll
    public void cleanUpDB() {
        for (int id : usersToCleanUp) {
            db.deleteUserById(id);
            System.out.println("deleted user: " + id);
        }
        for (int id : paymentCardsToCleanUp) {
            db.deletePaymentCardById(id);
            System.out.println("deleted payment card: " + id);
        }
        for (int id : addressesToCleanUp) {
            db.deleteAddressById(id);
            System.out.println("deleted address: " + id);
        }
    }

    @Test
    public void validateAddressExistence() {
        String addressNicknameField = "addressNickname";
        Address address = Address.generateRandomAddress();

        db.addNewAddress(address);
        System.out.println("created address: " + address.getId());
        addressesToCleanUp.add(address.getId());

        given().param("id", address.getId()).
                when().get(baseURI + ADDRESS_ENDPOINT).
                then().statusCode(200).and().body(addressNicknameField, equalTo(address.getAddressNickname()));

        Assertions.assertTrue(db.existsAddress(address.getId()));

        //db.deleteAddressById(address.getId());
    }

    @Test
    public void validateUserExistence() {
        String emailField = "email";
        User user = User.generateRandomUser();

        db.addNewUser(user);
        System.out.println("created user: " + user.getId());
        usersToCleanUp.add(user.getId());

        given().param("id", user.getId()).
                when().get(baseURI + USER_ENDPOINT).
                then().statusCode(200).and().body(emailField, equalTo(user.getEmail()));

        Assertions.assertTrue(db.existsUser(user.getId()));

        //db.deleteUserById(user.getId());
    }

    @Test
    @SuppressWarnings("unchecked")
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

        System.out.println("created user: " + db.selectUserIdByLogin(user.getLoginName()));
        usersToCleanUp.add(db.selectUserIdByLogin(user.getLoginName()));
        //db.deleteUserById(db.selectUserIdByLogin(user.getLoginName()));
    }

    @Test
    public void validateDeletingUser() {
        User user = User.generateRandomUser();

        db.addNewUser(user);
        System.out.println("created user: " + user.getId());
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
    @SuppressWarnings("unchecked")
    public void validateUpdatingPaymentCard() {
        String expirationDateField = "expirationDate";
        PaymentCard paymentCard = PaymentCard.generateRandomPaymentCard();
        String newExpirationDate = PaymentCard.generateExpirationDate();

        db.addNewPaymentCard(paymentCard);
        System.out.println("created payment card: " + paymentCard.getId());
        paymentCardsToCleanUp.add(paymentCard.getId());

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

        //db.deletePaymentCardById(paymentCard.getId());
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
