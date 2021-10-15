package entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import static test.Utils.generateRandomFourDigitNumber;

@Builder
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

    public Address() {
    }

    public static class AddressBuilder {
        public AddressBuilder withTestValues() {
            this.addressNickname("testAddressNickname")
                    .cityName("testCityName")
                    .id(8888)
                    .postalCode("20200")
                    .regionName("testRegionName")
                    .street("testStreet")
                    .streetAdditional("testStreetAdditional")
                    .userId(555);
            return this;
        }

        public AddressBuilder randomId() {
            this.id(generateRandomFourDigitNumber());
            return this;
        }
    }
}
