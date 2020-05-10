package org.bnp.id.util;

import org.bnp.id.model.field.Address;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AddressUtilTest {

    @Test
    public void test_convert_string_success() {

        Address expected = new Address("4089 Chaminade Ct.", "Claremont", "California", 91711, "USA");
        Address actual = AddressUtil.convert("4089 Chaminade Ct., Claremont, California 91711, USA");

        assertEquals(expected, actual);
    }

    @Test
    public void test_convert_address_success() {

        String expected = "4089 Chaminade Ct., Claremont, California 91711, USA";
        Address actual = new Address("4089 Chaminade Ct.", "Claremont", "California", 91711, "USA");

        assertEquals(expected, AddressUtil.convert(actual));
    }

    @Test
    public void test_convert_nullString() {

        Address actual = AddressUtil.convert((String) null);

        assertNull(actual.getStreet());
        assertNull(actual.getCity());
        assertNull(actual.getState());
        assertNull(actual.getZip());
        assertNull(actual.getCountry());
        assertEquals(new Address(), actual);
    }


    @Test
    public void test_convert_address_nullAddress() {

        String actual = AddressUtil.convert((Address) null);

        assertEquals(", , ,", actual);
    }

    @Test
    public void test_convert_string_blankString() {

        Address actual = AddressUtil.convert("");

        assertNull(actual.getStreet());
        assertNull(actual.getCity());
        assertNull(actual.getState());
        assertNull(actual.getZip());
        assertNull(actual.getCountry());
        assertEquals(new Address(), actual);
    }

    @Test
    public void test_convert_address_emptyAddress() {

        String actual = AddressUtil.convert(new Address());

        assertEquals(", , ,", actual);
    }

    @Test
    public void test_convert_string_nullStreet() {

        Address expected = new Address(null, "Claremont", "California", 91711, "USA");
        Address actual = AddressUtil.convert(", Claremont, California 91711, USA");

        assertEquals(expected, actual);
    }

    @Test
    public void test_convert_address_nullStreet() {

        String actual = AddressUtil.convert(new Address(null, "Claremont", "California", 91711, "USA"));

        assertEquals(", Claremont, California 91711, USA", actual);
    }

    @Test
    public void test_convert_string_emptyStreet() {

        Address expected = new Address("", "Claremont", "California", 91711, "USA");
        Address actual = AddressUtil.convert(", Claremont, California 91711, USA");

        assertEquals(expected, actual);
    }

    @Test
    public void test_convert_address_emptyStreet() {

        String actual = AddressUtil.convert(new Address("", "Claremont", "California", 91711, "USA"));

        assertEquals(", Claremont, California 91711, USA", actual);
    }

    @Test
    public void test_convert_string_streetOnly() {

        Address expected = new Address();
        expected.setStreet("4089 Chaminade Ct.");
        Address actual = AddressUtil.convert("4089 Chaminade Ct., , ,");

        assertEquals(expected, actual);
    }

    @Test
    public void test_convert_address_streetOnly() {

        String actual = AddressUtil.convert(new Address("4089 Chaminade Ct.", null, null, null, null));

        assertEquals("4089 Chaminade Ct., , ,", actual);
    }

    @Test
    public void test_convert_string_nullCity() {

        Address expected = new Address("4089 Chaminade Ct.", null, "California", 91711, "USA");
        Address actual = AddressUtil.convert("4089 Chaminade Ct., , California 91711, USA");

        assertEquals(expected, actual);
    }

    @Test
    public void test_convert_address_nullCity() {

        String actual = AddressUtil.convert(new Address("4089 Chaminade Ct.", null, "California", 91711, "USA"));

        assertEquals("4089 Chaminade Ct., , California 91711, USA", actual);
    }

    @Test
    public void test_convert_string_emptyCity() {

        Address expected = new Address("4089 Chaminade Ct.", "", "California", 91711, "USA");
        Address actual = AddressUtil.convert("4089 Chaminade Ct., , California 91711, USA");

        assertEquals(expected, actual);
    }

    @Test
    public void test_convert_address_emptyCity() {

        String actual = AddressUtil.convert(new Address("4089 Chaminade Ct.", "", "California", 91711, "USA"));

        assertEquals("4089 Chaminade Ct., , California 91711, USA", actual);
    }

    @Test
    public void test_convert_string_cityOnly() {

        Address expected = new Address();
        expected.setCity("Claremont");
        Address actual = AddressUtil.convert(", Claremont, ,");

        assertEquals(expected, actual);
    }

    @Test
    public void test_convert_address_cityOnly() {

        String actual = AddressUtil.convert(new Address(null, "Claremont", null, null, null));

        assertEquals(", Claremont, ,", actual);
    }

    @Test
    public void test_convert_string_nullState() {

        Address expected = new Address("4089 Chaminade Ct.", "Claremont", null, 91711, "USA");
        Address actual = AddressUtil.convert("4089 Chaminade Ct., Claremont,  91711, USA");

        assertEquals(expected, actual);
    }

    @Test
    public void test_convert_address_nullState() {

        String actual = AddressUtil.convert(new Address("4089 Chaminade Ct.", "Claremont", null, 91711, "USA"));

        assertEquals("4089 Chaminade Ct., Claremont,  91711, USA", actual);
    }

    @Test
    public void test_convert_string_emptyState() {

        Address expected = new Address("4089 Chaminade Ct.", "Claremont", "", 91711, "USA");
        Address actual = AddressUtil.convert("4089 Chaminade Ct., Claremont,  91711, USA");

        assertEquals(expected, actual);
    }

    @Test
    public void test_convert_address_emptyState() {

        String actual = AddressUtil.convert(new Address("4089 Chaminade Ct.", "Claremont", "", 91711, "USA"));

        assertEquals("4089 Chaminade Ct., Claremont,  91711, USA", actual);
    }

    @Test
    public void test_convert_string_stateOnly() {

        Address expected = new Address();
        expected.setState("California");
        Address actual = AddressUtil.convert(", , California,");

        assertEquals(expected, actual);
    }

    @Test
    public void test_convert_address_stateOnly() {

        String actual = AddressUtil.convert(new Address(null, null, "California", null, null));

        assertEquals(", , California,", actual);
    }

    @Test
    public void test_convert_string_zipOnly() {

        Address expected = new Address();
        expected.setZip(91711);
        Address actual = AddressUtil.convert(", ,  91711,");

        assertEquals(expected, actual);
    }

    @Test
    public void test_convert_address_zipOnly() {

        String actual = AddressUtil.convert(new Address(null, null, null, 91711, null));

        assertEquals(", ,  91711,", actual);
    }

    @Test
    public void test_convert_string_nullCountry() {

        Address expected = new Address("4089 Chaminade Ct.", "Claremont", "California", 91711, null);
        Address actual = AddressUtil.convert("4089 Chaminade Ct., Claremont, California 91711,");

        assertEquals(expected, actual);
    }

    @Test
    public void test_convert_address_nullCountry() {

        String expected = "4089 Chaminade Ct., Claremont, California 91711,";
        Address actual = new Address("4089 Chaminade Ct.", "Claremont", "California", 91711, null);

        assertEquals(expected, AddressUtil.convert(actual));
    }

    @Test
    public void test_convert_string_emptyCountry() {

        Address expected = new Address("4089 Chaminade Ct.", "Claremont", "California", 91711, "");
        Address actual = AddressUtil.convert("4089 Chaminade Ct., Claremont, California 91711,");

        assertEquals(expected, actual);
    }

    @Test
    public void test_convert_address_emptyCountry() {

        String expected = "4089 Chaminade Ct., Claremont, California 91711,";
        Address actual = new Address("4089 Chaminade Ct.", "Claremont", "California", 91711, "");

        assertEquals(expected, AddressUtil.convert(actual));
    }
}