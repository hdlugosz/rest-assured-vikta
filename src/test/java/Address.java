import com.github.javafaker.Faker;

public class Address {
    private String addressNickname;
    private String cityName;
    private int id;
    private String postalCode;
    private String regionName;
    private String street;
    private String streetAdditional;
    private int userId;

    public Address(String addressNickname, String cityName, int id, String postalCode, String regionName,
                   String street, String streetAdditional, int userId) {
        this.addressNickname = addressNickname;
        this.cityName = cityName;
        this.id = id;
        this.postalCode = postalCode;
        this.regionName = regionName;
        this.street = street;
        this.streetAdditional = streetAdditional;
        this.userId = userId;
    }

    public static void main(String[] args) {
        System.out.println(generateRandomAddress().toString());
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

    public String getAddressNickname() {
        return addressNickname;
    }

    public String getCityName() {
        return cityName;
    }

    public int getId() {
        return id;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getRegionName() {
        return regionName;
    }

    public String getStreet() {
        return street;
    }

    public String getStreetAdditional() {
        return streetAdditional;
    }

    public int getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "Address{" +
                "addressNickname='" + addressNickname + '\'' +
                ", cityName='" + cityName + '\'' +
                ", id=" + id +
                ", postalCode='" + postalCode + '\'' +
                ", regionName='" + regionName + '\'' +
                ", street='" + street + '\'' +
                ", streetAdditional='" + streetAdditional + '\'' +
                ", userId=" + userId +
                '}';
    }
}
