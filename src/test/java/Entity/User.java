package Entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.Data;

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

    public static void main(String[] args) {
        User user = generateRandomUser();
        System.out.println(user);
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

    public String toJsonString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }
}
