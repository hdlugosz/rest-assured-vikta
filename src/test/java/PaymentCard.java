import com.github.javafaker.Faker;

public class PaymentCard {
    private String cardCode;
    private String cardNickName;
    private String cardNumber;
    private String expirationDate;
    private int id;
    private String ownerName;
    private int userId;

    public PaymentCard(String cardCode, String cardNickName, String cardNumber, String expirationDate, int id,
                       String ownerName, Integer userId) {
        this.cardCode = cardCode;
        this.cardNickName = cardNickName;
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.id = id;
        this.ownerName = ownerName;
        this.userId = userId;
    }

    public static void main(String[] args) {
        PaymentCard paymentCard = generateRandomPaymentCard();
        System.out.println(paymentCard.toString());
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

    public String getCardCode() {
        return cardCode;
    }

    public String getCardNickName() {
        return cardNickName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public int getId() {
        return id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public int getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "PaymentCard{" +
                "cardCode='" + cardCode + '\'' +
                ", cardNickName='" + cardNickName + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                ", expirationDate='" + expirationDate + '\'' +
                ", id=" + id +
                ", ownerName='" + ownerName + '\'' +
                ", userId=" + userId +
                '}';
    }
}
