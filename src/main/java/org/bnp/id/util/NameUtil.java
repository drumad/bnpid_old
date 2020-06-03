package org.bnp.id.util;

import com.mysql.cj.util.StringUtils;
import lombok.Setter;
import org.bnp.id.constants.StringConstants;
import org.bnp.id.model.field.Name;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NameUtil {

    @Setter
    private List<String> namePrefix;

    /**
     * Convert a String value to its {@link Name} equivalent.
     *
     * @param value The string to convert.
     * @return The {@link Name} object of the string.
     */
    public Name convert(String value) {

        if (StringUtils.isNullOrEmpty(value)) {
            return new Name();
        }

        String[] array;
        String shortName = value.substring(value.indexOf("\""), value.lastIndexOf("\""));
        String first;
        String middle;
        String surname;
        String suffix = null;

        array = Arrays.stream(value.split(" ")).map(String::trim).toArray(String[]::new);

        int len = array.length;
        int i = len - 1;

        surname = array[i--];
        if (isRomanNumeral(surname)) {
            suffix = surname;
            surname = array[i--];
        }

        while (namePrefix.contains(array[i]) && i > 0) {
            surname = prepend(array[i--], surname);
        }

        middle = array[i--];
        while (namePrefix.contains(array[i]) && i > 0) {
            middle = prepend(array[i--], middle);
        }

        first = Arrays.stream(Arrays.copyOf(array, i)).collect(Collectors.joining(" "));

        return new Name(first, middle, surname, suffix, shortName);
    }

    /**
     * Converts the Name object into a string for DB saving.
     *
     * @param name The {@link Name} object.
     * @return The string equivalent of the {@link Name}.
     */
    public String convert(Name name) {

        StringBuffer buffer = new StringBuffer();

        append(buffer, name.getFirstName());
        append(buffer, "\"" + name.getShortName() + "\"");
        append(buffer, name.getMiddleName());
        append(buffer, name.getLastName());
        append(buffer, name.getSuffix());

        return buffer.toString().trim();
    }

    private String prepend(String prefix, String name) {

        return prefix + " " + name;
    }

    private void append(StringBuffer buffer, String name) {

        if (!StringUtils.isNullOrEmpty(name)) {
            buffer.append(name).append(StringConstants.SPACE);
        }
    }

    private boolean isRomanNumeral(String word) {

        return word.matches("^M{0,4}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$");
    }
}
