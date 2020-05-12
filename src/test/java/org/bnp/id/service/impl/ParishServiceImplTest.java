package org.bnp.id.service.impl;

import org.assertj.core.api.Assertions;
import org.bnp.id.config.DBConfig;
import org.bnp.id.controller.CountryController;
import org.bnp.id.exception.ParishNotFoundException;
import org.bnp.id.model.field.Address;
import org.bnp.id.model.info.Country;
import org.bnp.id.model.info.Parish;
import org.bnp.id.util.AddressUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ParishServiceImplTest {

    private ParishServiceImpl service;

    private AddressUtil addressUtil;

    @Mock
    private DBConfig config;

    @Mock
    private Connection con;

    @Mock
    private ResultSet rs;

    @Mock
    private Statement stmt;

    @Mock
    private CountryController countryController;

    private Country philippines;

    private Country usa;

    @Before
    public void setUp() throws Exception {

        addressUtil = new AddressUtil(countryController);
        philippines = new Country();
        philippines.setNiceName("Philippines");

        usa = new Country();
        usa.setNiceName("United States");

        Map<String, Country> map = new HashMap<>();
        map.put("Philippines", philippines);
        map.put("United States", usa);

        doReturn(map).when(countryController).getCountries();
        doReturn(con).when(config).getConnection();
        doReturn(stmt).when(con).createStatement();
        doReturn(stmt).when(con).createStatement(anyInt(), anyInt());
        doReturn(rs).when(stmt).executeQuery(anyString());
        doReturn(1).when(stmt).executeUpdate(anyString());
        doReturn(rs).when(stmt).getGeneratedKeys();
        when(rs.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(rs.getInt(anyInt())).thenReturn(7);
        when(rs.getInt("id")).thenReturn(1).thenReturn(2);
        when(rs.getString("name")).thenReturn("San Pablo Parish").thenReturn("San Pedro Parish");
        when(rs.getString("address")).thenReturn("Matina, Davao City, Davao del Sur 8021, Philippines")
                                     .thenReturn("San Pedro St., Poblacion District, Davao City, Davao del Sur, Philippines");
        doReturn(new Date(Instant.now().toEpochMilli())).when(rs).getDate("date_created");
        doReturn(new Date(Instant.now().toEpochMilli())).when(rs).getDate("date_updated");

        service = new ParishServiceImpl(config, addressUtil);
    }

    @Test
    public void test_findById_success() throws SQLException {

        Parish parish = service.findById(1);

        assertEquals(1, parish.getId().intValue());
        assertEquals("San Pablo Parish", parish.getName());
        assertEquals(new Address("Matina", "Davao City", "Davao del Sur", 8021, philippines), parish.getAddress());

        verify(rs, times(1)).getInt(anyString());
        verify(rs, times(2)).getString(anyString());
        verify(rs, times(2)).getDate(anyString());
        verify(countryController, times(1)).getCountries();
    }

    @Test
    public void test_findById_notFound() throws SQLException {

        doReturn(false).when(rs).next();

        Parish parish = service.findById(1);

        assertNull(parish);

        verify(rs, never()).getInt(anyString());
        verify(rs, never()).getString(anyString());
        verify(rs, never()).getDate(anyString());
        verify(countryController, never()).getCountries();
    }

    @Test
    public void test_findById_exception() throws SQLException {

        doThrow(new SQLException("Unit test reason", "Unit test sqlState", 1000)).when(con).createStatement();

        Assertions.assertThatThrownBy(() -> service.findById(1)).isInstanceOf(ParishNotFoundException.class)
                  .hasMessage("Parish with id [1] was not found.");

        verify(rs, never()).getInt(anyString());
        verify(rs, never()).getString(anyString());
        verify(rs, never()).getDate(anyString());
        verify(countryController, never()).getCountries();
    }

    @Test
    public void test_findByName_success() {

        List<Parish> parishes = service.findByName("San").stream().collect(Collectors.toList());

        assertEquals(2, parishes.size());
        assertEquals("San Pablo Parish", parishes.get(0).getName());
        assertEquals(new Address("Matina", "Davao City", "Davao del Sur", 8021, philippines), parishes.get(0).getAddress());
        assertEquals("San Pedro Parish", parishes.get(1).getName());
        assertEquals(new Address("San Pedro St., Poblacion District", "Davao City", "Davao del Sur", null, philippines),
            parishes.get(1).getAddress());
    }

    @Test
    public void test_findByName_exception() throws SQLException {

        doThrow(new SQLException("Unit test reason", "Unit test sqlState", 1000)).when(con).createStatement(anyInt(), anyInt());

        Assertions.assertThatThrownBy(() -> service.findByName("San")).isInstanceOf(ParishNotFoundException.class)
                  .hasMessage("Parish containing [San] in name was not found.");

        verify(rs, never()).getInt(anyString());
        verify(rs, never()).getString(anyString());
        verify(rs, never()).getDate(anyString());
        verify(countryController, never()).getCountries();
    }

    @Test
    public void test_findAll_success() {

        List<Parish> parishes = service.findAll().stream().collect(Collectors.toList());

        assertEquals(2, parishes.size());
        assertEquals("San Pablo Parish", parishes.get(0).getName());
        assertEquals(new Address("Matina", "Davao City", "Davao del Sur", 8021, philippines), parishes.get(0).getAddress());
        assertEquals("San Pedro Parish", parishes.get(1).getName());
        assertEquals(new Address("San Pedro St., Poblacion District", "Davao City", "Davao del Sur", null, philippines),
            parishes.get(1).getAddress());

    }

    @Test
    public void test_findAll_exception() throws SQLException {

        doThrow(new SQLException("Unit test reason", "Unit test sqlState", 1000)).when(con).createStatement(anyInt(), anyInt());

        Assertions.assertThatThrownBy(() -> service.findAll()).isInstanceOf(ParishNotFoundException.class).hasMessage("No parish found!");

        verify(rs, never()).getInt(anyString());
        verify(rs, never()).getString(anyString());
        verify(rs, never()).getDate(anyString());
        verify(countryController, never()).getCountries();
    }

    @Test
    public void test_findByAddress_address_success() throws SQLException {

        List<Parish> parishes = service.findByAddress(new Address()).stream().collect(Collectors.toList());

        assertEquals(2, parishes.size());
        assertEquals("San Pablo Parish", parishes.get(0).getName());
        assertEquals(new Address("Matina", "Davao City", "Davao del Sur", 8021, philippines), parishes.get(0).getAddress());
        assertEquals("San Pedro Parish", parishes.get(1).getName());
        assertEquals(new Address("San Pedro St., Poblacion District", "Davao City", "Davao del Sur", null, philippines),
            parishes.get(1).getAddress());

        verify(rs, times(2)).getInt(anyString());
        verify(rs, times(4)).getString(anyString());
        verify(rs, times(4)).getDate(anyString());
        verify(countryController, times(2)).getCountries();
    }

    @Test
    public void test_findByAddress_address_exception() throws SQLException {

        doThrow(new SQLException("Unit test reason", "Unit test sqlState", 1000)).when(con).createStatement(anyInt(), anyInt());

        Assertions.assertThatThrownBy(() -> service.findByAddress(new Address())).isInstanceOf(ParishNotFoundException.class)
                  .hasMessage("No parish found!");

        verify(rs, never()).getInt(anyString());
        verify(rs, never()).getString(anyString());
        verify(rs, never()).getDate(anyString());
        verify(countryController, never()).getCountries();
    }

    @Test
    public void test_findByAddress_string_success() throws SQLException {

        List<Parish> parishes = service.findByAddress("any string").stream().collect(Collectors.toList());

        assertEquals(2, parishes.size());
        assertEquals("San Pablo Parish", parishes.get(0).getName());
        assertEquals(new Address("Matina", "Davao City", "Davao del Sur", 8021, philippines), parishes.get(0).getAddress());
        assertEquals("San Pedro Parish", parishes.get(1).getName());
        assertEquals(new Address("San Pedro St., Poblacion District", "Davao City", "Davao del Sur", null, philippines),
            parishes.get(1).getAddress());

        verify(rs, times(2)).getInt(anyString());
        verify(rs, times(4)).getString(anyString());
        verify(rs, times(4)).getDate(anyString());
        verify(countryController, times(2)).getCountries();
    }

    @Test
    public void test_findByAddress_string_exception() throws SQLException {

        doThrow(new SQLException("Unit test reason", "Unit test sqlState", 1000)).when(con).createStatement(anyInt(), anyInt());

        Assertions.assertThatThrownBy(() -> service.findByAddress("any string")).isInstanceOf(ParishNotFoundException.class)
                  .hasMessage("No parish found!");

        verify(rs, never()).getInt(anyString());
        verify(rs, never()).getString(anyString());
        verify(rs, never()).getDate(anyString());
        verify(countryController, never()).getCountries();
    }

    @Test
    public void test_save_success() throws SQLException {

        Parish parish = new Parish();
        parish.setName("St. Jude Thaddeus Parish");
        parish.setAddress(addressUtil.convert(""));
        int id = service.save(parish);

        assertEquals(7, id);
    }

    @Test
    public void test_save_exception() throws SQLException {

        doThrow(new SQLException("Unit test reason", "Unit test sqlState", 1000)).when(con).createStatement();

        Assertions.assertThatThrownBy(() -> service.save(new Parish())).isInstanceOf(SQLException.class);
    }

    @Test
    public void test_update_success() throws SQLException {

        Parish parish = new Parish();
        parish.setName("St. Jude Thaddeus Parish");
        parish.setAddress(addressUtil.convert(""));
        int updated = service.update(parish);

        assertEquals(1, updated);

        verify(config, times(1)).getConnection();
        verify(stmt, times(1)).executeUpdate(anyString());
        verify(stmt, times(1)).close();
    }

    @Test
    public void test_update_exception() throws SQLException {

        doThrow(new SQLException("Unit test reason", "Unit test sqlState", 1000)).when(con).createStatement();

        Assertions.assertThatThrownBy(() -> service.update(new Parish())).isInstanceOf(SQLException.class);

        verify(config, times(1)).getConnection();
        verify(stmt, never()).executeUpdate(anyString());
        verify(stmt, never()).close();
    }

    @Test
    public void test_deleteById_success() throws SQLException {

        Parish parish = new Parish();
        parish.setName("St. Jude Thaddeus Parish");
        parish.setAddress(addressUtil.convert(""));
        int updated = service.deleteById(1);

        assertEquals(1, updated);

        verify(config, times(1)).getConnection();
        verify(stmt, times(1)).executeUpdate(anyString());
        verify(stmt, times(1)).close();
    }

    @Test
    public void test_deleteById_exception() throws SQLException {

        doThrow(new SQLException("Unit test reason", "Unit test sqlState", 1000)).when(con).createStatement();

        Assertions.assertThatThrownBy(() -> service.deleteById(1)).isInstanceOf(SQLException.class);

        verify(config, times(1)).getConnection();
        verify(stmt, never()).executeUpdate(anyString());
        verify(stmt, never()).close();
    }

    @Test
    public void test_deleteAll_doesNothing() throws SQLException {

        service.deleteAll();

        verify(config, never()).getConnection();
        verify(con, never()).createStatement();
        verify(stmt, never()).executeUpdate(anyString());
        verify(stmt, never()).close();
        verify(rs, never()).next();
        verify(rs, never()).close();
    }

    @Test
    public void test_isExists_success_exists() {


        Parish parish = new Parish();

        parish.setName("BNP Center");
        parish.setAddress(addressUtil.convert("7877 Riverside Dr., Ontario, CA 91761, United States"));

        assertTrue(service.isExists(parish));
    }

    @Test
    public void test_isExists_success_notExists() throws SQLException {

        doReturn(false).when(rs).next();
        Parish parish = new Parish();

        parish.setId(1);
        parish.setName("Unknown Parish");
        parish.setAddress(addressUtil.convert("Whatever street, City, State Zip, Country"));

        assertFalse(service.isExists(parish));
    }

    @Test
    public void test_isExists_exception() throws SQLException {

        doThrow(new SQLException("Unit test reason", "Unit test sqlState", 1000)).when(con).createStatement();

        assertFalse(service.isExists(new Parish()));

        verify(config, times(1)).getConnection();
        verify(stmt, never()).executeUpdate(anyString());
        verify(stmt, never()).close();
    }
}