package org.bnp.id.util;

import org.bnp.id.controller.CountryController;
import org.bnp.id.model.field.Address;
import org.bnp.id.model.info.Country;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AddressUtilTest {

    private Country country;

    private Map<String, Country> map;

    private AddressUtil addressUtil;

    @Mock
    private CountryController countryController;

    @Before
    public void setUp() {

        country = new Country();
        country.setId(227);
        country.setIso("US");
        country.setName("UNITED STATES");
        country.setNiceName("United States");
        country.setIso3("United States");
        country.setNumCode(840);
        country.setPhoneCode(1);

        map = new HashMap<>(1);
        map.put("United States", country);

        when(countryController.getCountries()).thenReturn(map);

        addressUtil = new AddressUtil(countryController);
    }

    @Test
    public void test_convert_string_success() {

        Address expected = new Address("4089 Chaminade Ct.", "Claremont", "California", 91711, country);
        Address actual = addressUtil.convert("4089 Chaminade Ct., Claremont, California 91711, United States");

        assertEquals(expected, actual);
    }

    @Test
    public void test_convert_address_success() {

        String expected = "4089 Chaminade Ct., Claremont, California 91711, United States";
        Address actual = new Address("4089 Chaminade Ct.", "Claremont", "California", 91711, country);

        assertEquals(expected, addressUtil.convert(actual));
    }

    @Test
    public void test_convert_nullString() {

        Address actual = addressUtil.convert((String) null);

        assertNull(actual.getStreet());
        assertNull(actual.getCity());
        assertNull(actual.getState());
        assertNull(actual.getZip());
        assertNull(actual.getCountry());
        assertEquals(new Address(), actual);
    }


    @Test
    public void test_convert_address_nullAddress() {

        String actual = addressUtil.convert((Address) null);

        assertEquals(", , ,", actual);
    }

    @Test
    public void test_convert_string_blankString() {

        Address actual = addressUtil.convert("");

        assertNull(actual.getStreet());
        assertNull(actual.getCity());
        assertNull(actual.getState());
        assertNull(actual.getZip());
        assertNull(actual.getCountry());
        assertEquals(new Address(), actual);
    }

    @Test
    public void test_convert_address_emptyAddress() {

        String actual = addressUtil.convert(new Address());

        assertEquals(", , ,", actual);
    }

    @Test
    public void test_convert_string_nullStreet() {

        Address expected = new Address(null, "Claremont", "California", 91711, country);
        Address actual = addressUtil.convert(", Claremont, California 91711, United States");

        assertEquals(expected, actual);
    }

    @Test
    public void test_convert_address_nullStreet() {

        String actual = addressUtil.convert(new Address(null, "Claremont", "California", 91711, country));

        assertEquals(", Claremont, California 91711, United States", actual);
    }

    @Test
    public void test_convert_string_emptyStreet() {

        Address expected = new Address("", "Claremont", "California", 91711, country);
        Address actual = addressUtil.convert(", Claremont, California 91711, United States");

        assertEquals(expected, actual);
    }

    @Test
    public void test_convert_address_emptyStreet() {

        String actual = addressUtil.convert(new Address("", "Claremont", "California", 91711, country));

        assertEquals(", Claremont, California 91711, United States", actual);
    }

    @Test
    public void test_convert_string_streetOnly() {

        Address expected = new Address();
        expected.setStreet("4089 Chaminade Ct.");
        Address actual = addressUtil.convert("4089 Chaminade Ct., , ,");

        assertEquals(expected, actual);
    }

    @Test
    public void test_convert_address_streetOnly() {

        String actual = addressUtil.convert(new Address("4089 Chaminade Ct.", null, null, null, null));

        assertEquals("4089 Chaminade Ct., , ,", actual);
    }

    @Test
    public void test_convert_string_nullCity() {

        Address expected = new Address("4089 Chaminade Ct.", null, "California", 91711, country);
        Address actual = addressUtil.convert("4089 Chaminade Ct., , California 91711, United States");

        assertEquals(expected, actual);
    }

    @Test
    public void test_convert_address_nullCity() {

        String actual = addressUtil.convert(new Address("4089 Chaminade Ct.", null, "California", 91711, country));

        assertEquals("4089 Chaminade Ct., , California 91711, United States", actual);
    }

    @Test
    public void test_convert_string_emptyCity() {

        Address expected = new Address("4089 Chaminade Ct.", "", "California", 91711, country);
        Address actual = addressUtil.convert("4089 Chaminade Ct., , California 91711, United States");

        assertEquals(expected, actual);
    }

    @Test
    public void test_convert_address_emptyCity() {

        String actual = addressUtil.convert(new Address("4089 Chaminade Ct.", "", "California", 91711, country));

        assertEquals("4089 Chaminade Ct., , California 91711, United States", actual);
    }

    @Test
    public void test_convert_string_cityOnly() {

        Address expected = new Address();
        expected.setCity("Claremont");
        Address actual = addressUtil.convert(", Claremont, ,");

        assertEquals(expected, actual);
    }

    @Test
    public void test_convert_address_cityOnly() {

        String actual = addressUtil.convert(new Address(null, "Claremont", null, null, null));

        assertEquals(", Claremont, ,", actual);
    }

    @Test
    public void test_convert_string_nullState() {

        Address expected = new Address("4089 Chaminade Ct.", "Claremont", null, 91711, country);
        Address actual = addressUtil.convert("4089 Chaminade Ct., Claremont,  91711, United States");

        assertEquals(expected, actual);
    }

    @Test
    public void test_convert_address_nullState() {

        String actual = addressUtil.convert(new Address("4089 Chaminade Ct.", "Claremont", null, 91711, country));

        assertEquals("4089 Chaminade Ct., Claremont,  91711, United States", actual);
    }

    @Test
    public void test_convert_string_emptyState() {

        Address expected = new Address("4089 Chaminade Ct.", "Claremont", "", 91711, country);
        Address actual = addressUtil.convert("4089 Chaminade Ct., Claremont,  91711, United States");

        assertEquals(expected, actual);
    }

    @Test
    public void test_convert_address_emptyState() {

        String actual = addressUtil.convert(new Address("4089 Chaminade Ct.", "Claremont", "", 91711, country));

        assertEquals("4089 Chaminade Ct., Claremont,  91711, United States", actual);
    }

    @Test
    public void test_convert_string_stateOnly() {

        Address expected = new Address();
        expected.setState("California");
        Address actual = addressUtil.convert(", , California,");

        assertEquals(expected, actual);
    }

    @Test
    public void test_convert_address_stateOnly() {

        String actual = addressUtil.convert(new Address(null, null, "California", null, null));

        assertEquals(", , California,", actual);
    }

    @Test
    public void test_convert_string_nullZip() {

        Address expected = new Address("4089 Chaminade Ct.", "Claremont", "California", null, country);
        Address actual = addressUtil.convert("4089 Chaminade Ct., Claremont,  91711, United States");

        assertEquals(expected, actual);
    }

    @Test
    public void test_convert_address_nullZip() {

        String actual = addressUtil.convert(new Address("4089 Chaminade Ct.", "Claremont", "California", null, country));

        assertEquals("4089 Chaminade Ct., Claremont, California, United States", actual);
    }

    @Test
    public void test_convert_string_emptyZip() {

        Address expected = new Address("4089 Chaminade Ct.", "Claremont", "California", 0, country);
        Address actual = addressUtil.convert("4089 Chaminade Ct., Claremont,  91711, United States");

        assertEquals(expected, actual);
    }

    @Test
    public void test_convert_address_emptyZip() {

        String actual = addressUtil.convert(new Address("4089 Chaminade Ct.", "Claremont", "California", 0, country));

        assertEquals("4089 Chaminade Ct., Claremont, California, United States", actual);
    }

    @Test
    public void test_convert_string_zipOnly() {

        Address expected = new Address();
        expected.setZip(91711);
        Address actual = addressUtil.convert(", ,  91711,");

        assertEquals(expected, actual);
    }

    @Test
    public void test_convert_address_zipOnly() {

        String actual = addressUtil.convert(new Address(null, null, null, 91711, null));

        assertEquals(", ,  91711,", actual);
    }

    @Test
    public void test_convert_string_nullCountry() {

        Address expected = new Address("4089 Chaminade Ct.", "Claremont", "California", 91711, null);
        Address actual = addressUtil.convert("4089 Chaminade Ct., Claremont, California 91711,");

        assertEquals(expected, actual);
    }

    @Test
    public void test_convert_address_nullCountry() {

        String expected = "4089 Chaminade Ct., Claremont, California 91711,";
        Address actual = new Address("4089 Chaminade Ct.", "Claremont", "California", 91711, null);

        assertEquals(expected, addressUtil.convert(actual));
    }

    @Test
    public void test_convert_string_emptyCountry() {

        Address expected = new Address("4089 Chaminade Ct.", "Claremont", "California", 91711, null);
        Address actual = addressUtil.convert("4089 Chaminade Ct., Claremont, California 91711,");

        assertEquals(expected, actual);
    }

    @Test
    public void test_convert_address_emptyCountry() {

        String expected = "4089 Chaminade Ct., Claremont, California 91711,";
        Address actual = new Address("4089 Chaminade Ct.", "Claremont", "California", 91711, new Country());

        assertEquals(expected, addressUtil.convert(actual));
    }
}