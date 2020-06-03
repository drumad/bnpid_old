package org.bnp.id.service.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.bnp.id.config.DBConfig;
import org.bnp.id.exception.CountryNotFoundException;
import org.bnp.id.model.info.Country;
import org.bnp.id.service.CountryService;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Service
@Log4j
public class CountryServiceImpl implements CountryService {

    private DBConfig config;

    @Override
    public Country findById(Integer id) {

        Country ret = null;

        try (Connection con = config.getConnection()) {

            Statement stmt = con.createStatement();
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT * FROM `country` WHERE `id` = ").append(id);

            ResultSet result = stmt.executeQuery(sql.toString());
            if (result.next()) {
                ret = getObject(result);
            } else {
                log.error("No country found with id " + id);
            }

            result.close();
            stmt.close();
        } catch (SQLException e) {
            log.error("Error " + e.getErrorCode() + " while attempting to get country with id " + id, e);
            throw new CountryNotFoundException(id);
        }

        return ret;
    }

    @Override
    public Collection<Country> findByName(String name) {

        List<Country> ret = new ArrayList<>();

        try (Connection con = config.getConnection()) {

            Statement stmt = con.createStatement();
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT * FROM `country` WHERE `name` LIKE %").append(name).append("%");

            ResultSet result = stmt.executeQuery(sql.toString());
            while (result.next()) {
                ret.add(getObject(result));
            }

            result.close();
            stmt.close();
        } catch (SQLException e) {
            log.error("Error " + e.getErrorCode() + " while attempting to get country with name " + name, e);
            throw new CountryNotFoundException(name);
        }

        return ret;
    }

    @Override
    public Collection<Country> findAll() {

        List<Country> ret = new ArrayList<>();

        try (Connection con = config.getConnection()) {

            Statement stmt = con.createStatement();
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT * FROM `country`");

            ResultSet result = stmt.executeQuery(sql.toString());
            while (result.next()) {
                ret.add(getObject(result));
            }

            result.close();
            stmt.close();
        } catch (SQLException e) {
            log.error("Error " + e.getErrorCode() + " while attempting to load country table.");
            throw new CountryNotFoundException();
        }

        return ret;
    }

    @Override
    public int save(Country country) {

        throw new UnsupportedOperationException("save function not allowed for Countries table");
    }

    @Override
    public int update(Country country) {

        throw new UnsupportedOperationException("update function not allowed for Countries table");
    }

    @Override
    public int deleteById(Integer id) {

        throw new UnsupportedOperationException("delete function not allowed for Countries table");
    }

    @Override
    public int count() throws SQLException {

        return 0;
    }

    @Override
    public void deleteAll() {

        throw new UnsupportedOperationException("delete function not allowed for Countries table");
    }

    @Override
    public boolean isExists(Country country) {

        return false;
    }

    @Override
    public Country getObject(ResultSet result) throws SQLException {

        Country ret;
        ret = new Country();
        ret.setId(result.getInt("id"));
        ret.setIso(result.getString("iso"));
        ret.setName(result.getString("name"));
        ret.setNiceName(result.getString("nicename"));
        ret.setIso3(result.getString("iso3"));
        ret.setNumCode(result.getInt("numcode"));
        ret.setPhoneCode(result.getInt("phonecode"));
        return ret;
    }
}
