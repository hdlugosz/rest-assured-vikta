import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentCard {
    private String cardCode;
    private String cardNickName;
    private String cardNumber;
    private String expirationDate;
    private int id;
    private String ownerName;
    private int userId;

    public static void main(String[] args) {
        PaymentCard paymentCard = generateRandomPaymentCard();
        System.out.println(paymentCard);
    }

    public static PaymentCard generateRandomPaymentCard() {
        Faker faker = new Faker();
        return new PaymentCard(
                faker.code().gtin8().substring(0, 3),
                faker.name().username(),
                faker.code().imei().concat("1"),
                faker.business().creditCardExpiry(),
                Integer.parseInt(faker.code().gtin8().substring(0, 4)),
                faker.name().fullName(),
                Integer.parseInt(faker.code().gtin8().substring(0, 3)));
    }

    public static String generateExpirationDate() {
        Faker faker = new Faker();
        return faker.business().creditCardExpiry();
    }
}
