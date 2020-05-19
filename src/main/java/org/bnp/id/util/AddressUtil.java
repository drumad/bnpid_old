package org.bnp.id.util;

import com.mysql.cj.util.StringUtils;
import lombok.AllArgsConstructor;
import org.bnp.id.constants.StringConstants;
import org.bnp.id.controller.CountryController;
import org.bnp.id.model.field.Address;
import org.bnp.id.model.info.Country;

import java.util.Arrays;
import java.util.stream.Collectors;

@AllArgsConstructor
public class AddressUtil {

    private CountryController countryController;

    public Address convert(String value) {

        if (StringUtils.isNullOrEmpty(value)) {
            return new Address();
        }

        String[] array = Arrays.stream(value.split(",")).map(String::trim).toArray(String[]::new);

        if (array.length < 4) {
            // Force an array of size 4.
            array = Arrays.copyOf(array, 4);
        }

        int i = array.length - 1;

        // Last element is the country
        Country country = countryController.getCountries().get(array[i--]);

        String[] stateZip = array[i--].split(" ");
        String state;
        Integer zip;
        try {
            state = Arrays.stream(Arrays.copyOf(stateZip, stateZip.length - 1)).collect(Collectors.joining(" "));
            zip = Integer.valueOf(stateZip[stateZip.length - 1]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            state = Arrays.stream(stateZip).collect(Collectors.joining(" "));
            zip = null;
        }

        String city = array[i];
        String street = Arrays.stream(Arrays.copyOf(array, i)).collect(Collectors.joining(", "));

        return new Address(street, city, state, zip, country);
    }

    public String convert(Address address) {

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

        if (address.getCountry() != null && !StringUtils.isNullOrEmpty(address.getCountry().getNiceName())) {
            result.append(address.getCountry().getNiceName());
        }

        return result.toString().trim();
    }

    /**
     * This should be used for display purposes only. Removes leading and trailing commas.
     *
     * @param stringAddress The address to clear commas from.
     * @return The cleaned up string.
     */
    public String removeCommas(String stringAddress) {

        stringAddress = stringAddress.replace(", , ,", ",").replace(", ,", ",");

        if (stringAddress.startsWith(",")) {
            stringAddress = stringAddress.replaceFirst(",", "");
        }

        if (stringAddress.endsWith(",")) {
            StringBuffer buffer = new StringBuffer(stringAddress);
            buffer.reverse().replace(0, 1, "");
            stringAddress = buffer.reverse().toString();
        }

        return stringAddress.trim();
    }
}
