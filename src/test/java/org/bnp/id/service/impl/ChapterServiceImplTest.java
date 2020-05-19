package org.bnp.id.service.impl;

import org.assertj.core.api.Assertions;
import org.bnp.id.config.DBConfig;
import org.bnp.id.controller.CountryController;
import org.bnp.id.exception.ChapterNotFoundException;
import org.bnp.id.model.field.Address;
import org.bnp.id.model.info.Chapter;
import org.bnp.id.model.info.Country;
import org.bnp.id.util.AddressUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ChapterServiceImplTest {

    private ChapterServiceImpl service;

    private AddressUtil addressUtil;

    @Mock
    private DBConfig config;

    @Mock
    private Connection con;

    @Mock
    private ResultSet rs;

    @Mock
    private PreparedStatement stmt;

    @Mock
    private CountryController countryController;

    private Country philippines;

    private Country usa;

    private Date yesterday;

    private Date today;

    @Before
    public void setUp() throws SQLException {

        addressUtil = new AddressUtil(countryController);
        philippines = new Country();
        philippines.setNiceName("Philippines");
        usa = new Country();
        usa.setNiceName("United States");

        today = new Date(Instant.now().toEpochMilli());
        yesterday = new Date(Instant.now().minus(1, ChronoUnit.DAYS).toEpochMilli());

        Map<String, Country> map = new HashMap<>();
        map.put("Philippines", philippines);
        map.put("United States", usa);

        doReturn(map).when(countryController).getCountries();

        doReturn(con).when(config).getConnection();
        doReturn(stmt).when(con).createStatement();
        doReturn(stmt).when(con).prepareStatement(anyString());
        doReturn(stmt).when(con).prepareStatement(anyString(), anyInt(), anyInt());
        doReturn(stmt).when(con).createStatement(anyInt(), anyInt());
        doReturn(rs).when(stmt).executeQuery(anyString());
        doReturn(rs).when(stmt).executeQuery();
        doReturn(1).when(stmt).executeUpdate();
        doReturn(rs).when(stmt).getGeneratedKeys();
        when(rs.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(rs.getInt(anyInt())).thenReturn(7);
        when(rs.getInt("id")).thenReturn(1).thenReturn(2);
        when(rs.getString("name")).thenReturn("Buhangin Chapter").thenReturn("Area Satellite 10");
        when(rs.getString("location")).thenReturn("Buhangin, Davao City, Davao del Sur 8021, Philippines")
                                      .thenReturn("San Pedro St., Poblacion District, Davao City, Davao del Sur, Philippines");
        when(rs.getDate("started")).thenReturn(today).thenReturn(yesterday);
        doReturn(new Date(Instant.now().toEpochMilli())).when(rs).getDate("date_created");
        doReturn(new Date(Instant.now().toEpochMilli())).when(rs).getDate("date_updated");

        service = new ChapterServiceImpl(config, addressUtil);
    }

    @Test
    public void test_findById_success() throws SQLException {

        Chapter chapter = service.findById(1);

        assertEquals(1, chapter.getId().intValue());
        assertEquals("Buhangin Chapter", chapter.getName());
        assertEquals(new Address("Buhangin", "Davao City", "Davao del Sur", 8021, philippines), chapter.getLocation());
        assertEquals(today, chapter.getStarted());

        verify(rs, times(1)).close();
        verify(stmt, times(1)).close();
    }

    @Test
    public void test_findById_noneFound() throws SQLException {

        doReturn(false).when(rs).next();

        Assertions.assertThatThrownBy(() -> service.findById(1)).isInstanceOf(ChapterNotFoundException.class);

        verify(stmt, times(1)).executeQuery();
        verify(rs, times(1)).next();
        verify(rs, times(1)).close();
        verify(stmt, times(1)).close();
    }

    @Test
    public void test_findById_exception() throws SQLException {

        doThrow(SQLException.class).when(con).prepareStatement(anyString());

        Assertions.assertThatThrownBy(() -> service.findById(1)).isInstanceOf(SQLException.class);

        verify(stmt, never()).executeQuery();
    }

    @Test
    public void test_findByName_success() throws SQLException {

        List<Chapter> chapters = service.findByName("Name").stream().collect(Collectors.toList());

        assertEquals(2, chapters.size());
        assertEquals("Buhangin Chapter", chapters.get(0).getName());
        assertEquals(new Address("Buhangin", "Davao City", "Davao del Sur", 8021, philippines), chapters.get(0).getLocation());
        assertEquals(today, chapters.get(0).getStarted());
        assertEquals("Area Satellite 10", chapters.get(1).getName());
        assertEquals(new Address("San Pedro St., Poblacion District", "Davao City", "Davao del Sur", null, philippines),
            chapters.get(1).getLocation());
        assertEquals(yesterday, chapters.get(1).getStarted());

        verify(rs, times(1)).close();
        verify(stmt, times(1)).close();
    }

    @Test
    public void test_findByName_noneFound() throws SQLException {

        doReturn(false).when(rs).next();

        Assertions.assertThatThrownBy(() -> service.findByName("Name")).isInstanceOf(ChapterNotFoundException.class);

        verify(stmt, times(1)).executeQuery();
        verify(rs, times(1)).next();
        verify(rs, times(1)).close();
        verify(stmt, times(1)).close();
    }

    @Test
    public void test_findByName_exception() throws SQLException {

        doThrow(SQLException.class).when(con).prepareStatement(anyString(), anyInt(), anyInt());

        Assertions.assertThatThrownBy(() -> service.findByName("Name")).isInstanceOf(SQLException.class);

        verify(stmt, never()).executeQuery();
    }

    @Test
    public void test_findAll_success() throws SQLException {

        List<Chapter> chapters = service.findAll().stream().collect(Collectors.toList());

        assertEquals(2, chapters.size());
        assertEquals("Buhangin Chapter", chapters.get(0).getName());
        assertEquals(new Address("Buhangin", "Davao City", "Davao del Sur", 8021, philippines), chapters.get(0).getLocation());
        assertEquals(today, chapters.get(0).getStarted());
        assertEquals("Area Satellite 10", chapters.get(1).getName());
        assertEquals(new Address("San Pedro St., Poblacion District", "Davao City", "Davao del Sur", null, philippines),
            chapters.get(1).getLocation());
        assertEquals(yesterday, chapters.get(1).getStarted());

        verify(rs, times(1)).close();
        verify(stmt, times(1)).close();
    }

    @Test
    public void test_findAll_noneFound() throws SQLException {

        doReturn(false).when(rs).next();

        Assertions.assertThatThrownBy(() -> service.findAll()).isInstanceOf(ChapterNotFoundException.class);

        verify(stmt, times(1)).executeQuery(anyString());
        verify(rs, times(1)).next();
        verify(rs, times(1)).close();
        verify(stmt, times(1)).close();
    }

    @Test
    public void test_findAll_exception() throws SQLException {

        doThrow(SQLException.class).when(con).createStatement(anyInt(), anyInt());

        Assertions.assertThatThrownBy(() -> service.findAll()).isInstanceOf(SQLException.class);

        verify(stmt, never()).executeQuery();

        verify(rs, never()).close();
        verify(stmt, never()).close();
    }

    @Test
    public void test_save_success() throws SQLException {

        Chapter chapter = new Chapter();
        chapter.setName("Inland Empire Chapter");
        chapter.setLocation(new Address("", "Ontario", "California", 91761, usa));
        chapter.setStarted(today);
        int id = service.save(chapter);

        assertEquals(7, id);

        verify(rs, times(1)).close();
        verify(stmt, times(1)).close();
    }

    @Test
    public void test_save_exception() throws SQLException {

        doThrow(SQLException.class).when(con).prepareStatement(anyString());
        Assertions.assertThatThrownBy(() -> service.save(new Chapter())).isInstanceOf(SQLException.class);

        verify(rs, never()).close();
        verify(stmt, never()).close();
    }

    @Test
    public void test_update_success() throws SQLException {

        Chapter chapter = new Chapter();
        chapter.setId(1);
        chapter.setName("Inland Empire Chapter");
        chapter.setLocation(new Address("", "Ontario", "California", 91761, usa));
        chapter.setStarted(yesterday);
        int rows = service.update(chapter);

        assertEquals(1, rows);

        verify(stmt, times(1)).close();
    }

    @Test
    public void test_update_exception() throws SQLException {

        doThrow(SQLException.class).when(con).prepareStatement(anyString());

        Assertions.assertThatThrownBy(() -> service.update(new Chapter())).isInstanceOf(SQLException.class);

        verify(stmt, never()).close();
    }

    @Test
    public void test_deleteById_success() throws SQLException {

        int rows = service.deleteById(1);

        assertEquals(1, rows);

        verify(stmt, times(1)).close();
    }

    @Test
    public void test_deleteById_noneFound() throws SQLException {

        doReturn(0).when(stmt).executeUpdate();

        int rows = service.deleteById(1);

        assertEquals(0, rows);

        verify(stmt, times(1)).close();
    }

    @Test
    public void test_deleteById_exception() throws SQLException {

        doThrow(SQLException.class).when(con).prepareStatement(anyString());

        Assertions.assertThatThrownBy(() -> service.deleteById(1)).isInstanceOf(SQLException.class);

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
    public void test_isExists_success() throws SQLException {

        Chapter chapter = new Chapter();
        chapter.setId(1);
        chapter.setName("Inland Empire Chapter");
        chapter.setLocation(new Address("", "Ontario", "California", 91761, usa));
        chapter.setStarted(yesterday);

        assertTrue(service.isExists(chapter));
    }

    @Test
    public void test_isExists_noneFound() throws SQLException {

        doReturn(false).when(rs).next();

        Chapter chapter = new Chapter();
        chapter.setId(1);
        chapter.setName("Inland Empire Chapter");
        chapter.setLocation(new Address("", "Ontario", "California", 91761, usa));
        chapter.setStarted(yesterday);

        assertFalse(service.isExists(chapter));
    }

    @Test
    public void test_isExists_exception() throws SQLException {

        doThrow(SQLException.class).when(con).createStatement();

        Chapter chapter = new Chapter();
        chapter.setId(1);
        chapter.setName("Inland Empire Chapter");
        chapter.setLocation(new Address("", "Ontario", "California", 91761, usa));
        chapter.setStarted(yesterday);

        Assertions.assertThatThrownBy(() -> service.isExists(chapter)).isInstanceOf(SQLException.class);

        verify(rs, never()).close();
        verify(stmt, never()).close();
    }

    @Test
    public void test_findByCountry_success() throws SQLException {

        List<Chapter> chapters = service.findByCountry(philippines).stream().collect(Collectors.toList());

        assertEquals(2, chapters.size());
        assertEquals("Buhangin Chapter", chapters.get(0).getName());
        assertEquals(new Address("Buhangin", "Davao City", "Davao del Sur", 8021, philippines), chapters.get(0).getLocation());
        assertEquals(today, chapters.get(0).getStarted());
        assertEquals("Area Satellite 10", chapters.get(1).getName());
        assertEquals(new Address("San Pedro St., Poblacion District", "Davao City", "Davao del Sur", null, philippines),
            chapters.get(1).getLocation());
        assertEquals(yesterday, chapters.get(1).getStarted());

        verify(rs, times(1)).close();
        verify(stmt, times(1)).close();
    }

    @Test
    public void test_findByCountry_noneFound() throws SQLException {

        doReturn(false).when(rs).next();

        Assertions.assertThatThrownBy(() -> service.findByCountry(usa)).isInstanceOf(ChapterNotFoundException.class);

        verify(stmt, times(1)).executeQuery();
        verify(rs, times(1)).next();
        verify(rs, times(1)).close();
        verify(stmt, times(1)).close();
    }

    @Test
    public void test_findByCountry_exception() throws SQLException {

        doThrow(SQLException.class).when(con).prepareStatement(anyString(), anyInt(), anyInt());

        Assertions.assertThatThrownBy(() -> service.findByCountry(usa)).isInstanceOf(SQLException.class);

        verify(stmt, never()).executeQuery();

        verify(rs, never()).close();
        verify(stmt, never()).close();
    }
}