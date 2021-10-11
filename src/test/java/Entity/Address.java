package Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

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

    public static class AddressBuilder {
        public Address.AddressBuilder withTestValues() {
            this.addressNickname = "testAddressNickname";
            this.cityName = "testCityName";
            this.id = 8888;
            this.postalCode = "20200";
            this.regionName = "testRegionName";
            this.street = "testStreet";
            this.streetAdditional = "testStreetAdditional";
            this.userId = 555;
            return this;
        }

        public Address.AddressBuilder randomId() {
            this.id = (int) ((Math.random() * (9999 - 1000)) + 1000);
            return this;
        }
    }
}
