package org.bnp.id.service.impl;

import org.assertj.core.api.Assertions;
import org.bnp.id.exception.ParishNotFoundException;
import org.bnp.id.model.field.Address;
import org.bnp.id.model.info.Parish;
import org.bnp.id.util.AddressUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class ParishServiceImplIT {

    @Autowired
    private ParishServiceImpl parishService;

    @Autowired
    private AddressUtil addressUtil;

    private Parish parish;

    private int id;

    @Before
    public void setUp() throws SQLException {

        parish = new Parish();
        parish.setName("Our Lady of Assumption");
        parish.setAddress(addressUtil.convert("435 N Berkeley Ave, Claremont, CA 91711, United States"));

        id = parishService.save(parish);

        assertTrue(id > 0);
    }

    @Test
    public void test_findById_parishFound() throws SQLException {

        Parish parish = parishService.findById(id);

        assertEquals(this.parish.getName(), parish.getName());
        assertEquals(this.parish.getAddress(), parish.getAddress());
    }

    @Test
    public void test_findById_parishNotFound() {

        Assertions.assertThatThrownBy(() -> parishService.findById(id + 1)).isInstanceOf(ParishNotFoundException.class)
                  .hasMessage("Parish with id [" + (id + 1) + "] was not found.");
    }

    @Test
    public void test_findByName_oneFound() throws SQLException {

        List<Parish> parishList = parishService.findByName("Our Lady of Assumption").stream().collect(Collectors.toList());

        assertEquals(1, parishList.size());

        Parish parish = parishList.get(0);
        assertEquals(id, parish.getId().intValue());
        assertEquals(this.parish.getName(), parish.getName());
        assertEquals(this.parish.getAddress(), parish.getAddress());
    }

    @Test
    public void test_findByName_moreThanOneFound() throws SQLException {

        Address addr = addressUtil.convert("F. Torres St, Poblacion District, Davao City, Davao del Sur 8000, Philippines");
        Parish temp = new Parish();
        temp.setName("Our Lady of AssumptioN");
        temp.setAddress(addr);

        int id = parishService.save(temp);

        assertTrue(id > 0);

        List<Parish> parishList = parishService.findByName("Our Lady of Assumption").stream().collect(Collectors.toList());

        assertTrue(parishList.size() > 1);

        Parish parish = parishList.get(0);
        assertEquals(this.id, parish.getId().intValue());
        assertEquals(this.parish.getName(), parish.getName());
        assertEquals(this.parish.getAddress(), parish.getAddress());

        parish = parishList.get(1);
        assertEquals(id, parish.getId().intValue());
        assertEquals("Our Lady of AssumptioN", parish.getName());
        assertEquals(addr, parish.getAddress());

        int result = parishService.deleteById(id);

        assertEquals(1, result);
    }

    @Test
    public void test_findByName_noneFound() {

        Assertions.assertThatThrownBy(() -> parishService.findByName("The Enemy")).isInstanceOf(ParishNotFoundException.class)
                  .hasMessage("Parish containing [The Enemy] in name was not found.");
    }

    @Test
    public void test_findAll_success() {

        List<Parish> list = parishService.findAll().stream().collect(Collectors.toList());

        assertFalse(list.isEmpty());
    }

    @Test
    public void test_findByAddress_addressObject_success() throws SQLException {

        Address address = new Address();
        address.setStreet("Berkeley");

        List<Parish> list = parishService.findByAddress(address).stream().collect(Collectors.toList());

        assertEquals(1, list.size());
    }

    @Test
    public void test_findByAddress_addressObject_noneFound() throws SQLException {

        Address address = new Address();
        address.setStreet("asdf");

        List<Parish> list = parishService.findByAddress(address).stream().collect(Collectors.toList());

        assertEquals(0, list.size());
    }

    @Test
    public void test_findByAddress_addressString_success() throws SQLException {

        List<Parish> list = parishService.findByAddress("Claremont").stream().collect(Collectors.toList());

        assertEquals(1, list.size());
    }

    @Test
    public void test_findByAddress_addressString_noneFound() throws SQLException {

        List<Parish> list = parishService.findByAddress("qwer").stream().collect(Collectors.toList());

        assertEquals(0, list.size());
    }

    @Test
    public void test_update_success() throws SQLException {

        Parish parish = new Parish();
        parish.setId(id);
        parish.setName("Hello World!");
        parish.setAddress(addressUtil.convert("Hello United States of America"));

        int rows = parishService.update(parish);

        assertEquals(1, rows);

        Parish updatedParish = parishService.findById(id);

        assertEquals("Hello World!", updatedParish.getName());
        assertEquals(addressUtil.convert("Hello United States of America"), updatedParish.getAddress());
    }

    @Test
    public void test_update_fail() throws SQLException {

        Parish parish = new Parish();
        parish.setId(-1);
        parish.setName("Hello World!");
        parish.setAddress(addressUtil.convert("Hello United States of America"));

        int rows = parishService.update(parish);

        assertEquals(0, rows);
    }

    @Test
    public void test_deleteById_success() {

        // This will be performed in tearDown.. Which hasn't been a problem so far.
    }

    @Test
    public void test_deleteById_fail() throws SQLException {

        assertEquals(0, parishService.deleteById(-1));
    }

    @Test
    public void test_count_success() throws SQLException {

        int count = parishService.count();

        assertTrue(count > 0);
    }

    @After
    public void tearDown() throws SQLException {

        int result = parishService.deleteById(id);

        assertTrue(result > 0);
    }
}