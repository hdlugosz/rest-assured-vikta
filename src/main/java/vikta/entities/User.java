package vikta.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Objects;

import static vikta.utils.Utils.generateRandomFourDigitNumber;

@Builder
@Data
@AllArgsConstructor
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

    public User() {
    }

    public static class UserBuilder {
        public UserBuilder withTestValues() {
            this.addressIds(new int[]{})
                    .email("testEmail@mail.com")
                    .firstName("testFirstName")
                    .id(9999)
                    .loginName("testLogin")
                    .middleName("testMiddleName")
                    .password("testPassword")
                    .pathToAvatarImage("path.com")
                    .paymentCardIds(new int[]{})
                    .surname("testSurname");
            return this;
        }

        public UserBuilder randomId() {
            return this.id(generateRandomFourDigitNumber());
        }
    }

    public boolean equalsNotOverriddenFields(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email)
                && Objects.equals(firstName, user.firstName)
                && Objects.equals(loginName, user.loginName)
                && Objects.equals(middleName, user.middleName)
                && Objects.equals(password, user.password)
                && Objects.equals(surname, user.surname);
    }
}
