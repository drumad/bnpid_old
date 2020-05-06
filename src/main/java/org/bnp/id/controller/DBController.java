package org.bnp.id.controller;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.bnp.id.config.DBConfig;
import org.bnp.id.model.info.Country;
import org.bnp.id.model.info.Parish;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Log4j
public class DBController {

    private DBConfig config;

    private Map<Integer, Country> countries;

    private Map<Integer, Parish> parishes;

    public DBController() {

        //loadCountries();
    }

    /**
     * Loads all the countries from the database.
     */
    public void loadCountries() {

        HashMap<Integer, Country> map = null;

        log.debug("Loading countries..");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
        }

        try (Connection con = config.getConnection()) {

            Statement stmt = con.createStatement();
            ResultSet result = stmt.executeQuery("select * from country");

            map = new HashMap<>();

            while (result.next()) {
                Country country = new Country();
                country.setId(result.getInt("id"));
                country.setIso(result.getString("iso"));
                country.setName(result.getString("name"));
                country.setNiceName(result.getString("nicename"));
                country.setIso3(result.getString("iso3"));
                country.setNumCode(result.getInt("numcode"));
                country.setPhoneCode(result.getInt("phonecode"));

                map.put(country.getId(), country);
            }

            result.close();
            stmt.close();

        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }

        countries = map;
    }
}
