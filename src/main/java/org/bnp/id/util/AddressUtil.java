package org.bnp.id.util;

import org.bnp.id.constants.StringConstants;
import org.bnp.id.model.field.Address;

public class AddressUtil {

    public static Address convert(String value) {

        String[] array = value.split(",");
        String[] countryZip = array[3].split(" ");
        Integer zip;
        try {
            zip = Integer.valueOf(countryZip[1]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            zip = 0;
        }

        return new Address(array[0], array[1], array[2], array[3], zip);
    }

    public static String convert(Address address) {

        StringBuilder result = new StringBuilder();

        if (!StringUtil.isNullOrEmpty(address.getStreet())) {
            result.append(address.getStreet());
        }
        result.append(StringConstants.COMMA_SPACE);

        if (!StringUtil.isNullOrEmpty(address.getCity())) {
            result.append(address.getCity());
        }
        result.append(StringConstants.COMMA_SPACE);

        if (!StringUtil.isNullOrEmpty(address.getState())) {
            result.append(address.getState());
        }
        result.append(StringConstants.COMMA_SPACE);

        if (!StringUtil.isNullOrEmpty(address.getCountry())) {
            result.append(address.getCountry());
        }
        result.append(StringConstants.SPACE);

        if (address.getZip() != null && address.getZip() > 0) {
            result.append(address.getZip());
        }

        return result.toString().trim();
    }

}
