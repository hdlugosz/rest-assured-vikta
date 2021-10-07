import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Address {
    private String addressNickname;
    private String cityName;
    private int id;
    private String postalCode;
    private String regionName;
    private String street;
    private String streetAdditional;
    private int userId;

    public static void main(String[] args) {
        System.out.println(generateRandomAddress());
    }

    public static Address generateRandomAddress() {
        Faker faker = new Faker();
        return new Address(
                faker.pokemon().name(),
                faker.address().cityName(),
                Integer.parseInt(faker.code().gtin8().substring(0, 4)),
                faker.address().zipCode(),
                faker.address().state(),
                faker.address().streetAddress(),
                faker.address().latitude().concat(" " + faker.address().longitude()),
                Integer.parseInt(faker.code().gtin8().substring(0, 3)));
    }
}
