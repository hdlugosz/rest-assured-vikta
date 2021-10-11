package Entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

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

    public static class UserBuilder {
        public UserBuilder withTestValues() {
            this.addressIds = new int[]{0};
            this.email = "testEmail@mail.com";
            this.firstName = "testFirstName";
            this.id = 9999;
            this.loginName = "testLogin";
            this.middleName = "testMiddleName";
            this.password = "testPassword";
            this.pathToAvatarImage = "path.com";
            this.paymentCardIds = new int[]{0};
            this.surname = "testSurname";
            return this;
        }

        public UserBuilder randomId() {
            this.id = (int) ((Math.random() * (9999 - 1000)) + 1000);
            return this;
        }
    }

    public String toJsonString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }
}
