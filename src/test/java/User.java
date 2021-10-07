import com.github.javafaker.Faker;

import java.util.Arrays;

public class User {
    private int[] addressIds;
    private String email;
    private String firstName;
    private int id;
    private String loginName;
    private String middleName;
    private String password;
    private String pathToAvatarImage;
    private int[] paymentCardIds;
    private String surname;

    public User(int[] addressIds, String email, String firstName, int id, String loginName, String middleName,
                String password, String pathToAvatarImage, int[] paymentCardIds, String surname) {
        this.addressIds = addressIds;
        this.email = email;
        this.firstName = firstName;
        this.id = id;
        this.loginName = loginName;
        this.middleName = middleName;
        this.password = password;
        this.pathToAvatarImage = pathToAvatarImage;
        this.paymentCardIds = paymentCardIds;
        this.surname = surname;
    }

    public static void main(String[] args) {
        User user = generateRandomUser();
        System.out.println(user.toString());
    }

    public static User generateRandomUser() {
        Faker faker = new Faker();
        return new User(new int[]{0},
                faker.internet().emailAddress(),
                faker.name().firstName(),
                Integer.parseInt(faker.code().gtin8().substring(0, 4)),
                faker.name().username(),
                faker.name().firstName(),
                faker.internet().password(),
                faker.internet().image(),
                new int[]{0},
                faker.name().lastName());
    }

    public int[] getAddressIds() {
        return addressIds;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public int getId() {
        return id;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getPassword() {
        return password;
    }

    public String getPathToAvatarImage() {
        return pathToAvatarImage;
    }

    public int[] getPaymentCardIds() {
        return paymentCardIds;
    }

    public String getSurname() {
        return surname;
    }

    @Override
    public String toString() {
        return "User{" +
                "addressIds=" + Arrays.toString(addressIds) +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", id=" + id +
                ", loginName='" + loginName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", password='" + password + '\'' +
                ", pathToAvatarImage='" + pathToAvatarImage + '\'' +
                ", paymentCardIds=" + Arrays.toString(paymentCardIds) +
                ", surname='" + surname + '\'' +
                '}';
    }
}
