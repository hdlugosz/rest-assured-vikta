package Entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import static test.Utils.generateID;

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
            this.addressIds(new int[]{0})
                    .email("testEmail@mail.com")
                    .firstName("testFirstName")
                    .id(9999)
                    .loginName("testLogin")
                    .middleName("testMiddleName")
                    .password("testPassword")
                    .pathToAvatarImage("path.com")
                    .paymentCardIds(new int[]{0})
                    .surname("testSurname");
            return this;
        }

        public UserBuilder randomId() {
            return this.id(generateID());
        }
    }

    public String toJsonString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }
}