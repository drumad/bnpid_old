package org.bnp.id.util;

import com.mysql.cj.util.StringUtils;
import org.bnp.id.constants.StringConstants;
import org.bnp.id.model.field.Address;

import java.util.Arrays;
import java.util.stream.Collectors;

public class AddressUtil {

    public static Address convert(String value) {

        if (StringUtils.isNullOrEmpty(value)) {
            return new Address();
        }

        String[] array = Arrays.stream(value.split(",")).map(String::trim).toArray(String[]::new);

        if (array.length < 4) {
            // Force an array of size 4.
            array = Arrays.copyOf(array, 4);
        }

        String[] cityZip = array[2].split(" ");
        String city;
        Integer zip;
        try {
            city = Arrays.stream(Arrays.copyOf(cityZip, cityZip.length - 1)).collect(Collectors.joining(" "));
            zip = Integer.valueOf(cityZip[cityZip.length - 1]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            city = Arrays.stream(cityZip).collect(Collectors.joining(" "));
            zip = 0;
        }

        return new Address(array[0], array[1], city, zip, array[3]);
    }

    public static String convert(Address address) {

        if (address == null) {
            address = new Address();
        }

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

        if (address.getZip() != null && address.getZip() > 0) {
            result.append(StringConstants.SPACE);
            result.append(address.getZip());
        }
        result.append(StringConstants.COMMA_SPACE);

        if (!StringUtil.isNullOrEmpty(address.getCountry())) {
            result.append(address.getCountry());
        }

        return result.toString().trim();
    }
}
