package org.bnp.id.model.field;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bnp.id.constants.StringConstants;
import org.bnp.id.util.StringUtil;

@Getter
@Setter
@AllArgsConstructor
public class Address {

    private String city;

    private String country;

    private String state;

    private String street;

    private Integer zip;

    public boolean equals(Address address) {
        return street.equals(address.getStreet()) &&
                city.equals(address.getCity()) &&
                state.equals(address.getState()) &&
                country.equals(address.getCountry()) &&
                zip == address.getZip();
    }

    @Override
    public String toString() {
        StringBuilder address = new StringBuilder();

        if (!StringUtil.isNullOrEmpty(street)) {
            address.append(street);
        }

        if (!StringUtil.isNullOrEmpty(city)) {
            if (!StringUtil.isNullOrEmpty(address.toString()) && !address.toString()
                    .endsWith(StringConstants.COMMA_SPACE)) {
                address.append(StringConstants.COMMA_SPACE);
            }

            address.append(city);
        }

        if (!StringUtil.isNullOrEmpty(state)) {
            if (!StringUtil.isNullOrEmpty(address.toString()) && !address.toString()
                    .endsWith(StringConstants.COMMA_SPACE)) {
                address.append(StringConstants.COMMA_SPACE);
            }

            address.append(state);
        }

        if (!StringUtil.isNullOrEmpty(country)) {
            if (!StringUtil.isNullOrEmpty(address.toString()) && !address.toString()
                    .endsWith(StringConstants.COMMA_SPACE)) {
                address.append(StringConstants.COMMA_SPACE);
            }

            address.append(country);
        }

        if (zip != null && zip > 0) {
            address.append(" ");
            address.append(zip);
        }

        return address.toString().trim();
    }
}
