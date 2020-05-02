package org.bnp.id.controller;

import org.bnp.id.config.DBConfig;
import org.bnp.id.constants.StringConstants;
import org.bnp.id.model.field.Address;
import org.bnp.id.model.info.Parish;
import org.springframework.context.annotation.Bean;

import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;


public class DBController {

    private static DBController instance = new DBController();

    private DBConfig config = DBConfig.getInstance();

    private DBController() {

    }

    @Bean
    public static DBController getInstance() {

        return instance;
    }

    public HashMap<Integer, String> getCountriesMap() {

        HashMap<Integer, String> map = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }

        try (Connection con = config.getConnection()) {

            Statement stmt = con.createStatement();
            ResultSet result = stmt.executeQuery("select * from country");

            map = new HashMap<>();

            while (result.next()) {
                map.put(result.getInt("org/bnp/id"), result.getString("nicename"));
            }

            System.out.println("Countries in map: " + map.size());

            result.close();
            stmt.close();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return map;
    }

    public HashSet<String> getLastNames() {

        HashSet<String> set = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bnpid", "root", "root123")) {

            Statement stmt = con.createStatement();
            ResultSet result = stmt.executeQuery("select lastname from members");

            String name;

            set = new HashSet<>();

            while (result.next()) {
                name = result.getString("lastname");

                set.add(name);
            }

            result.close();
            stmt.close();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return set;
    }

    public HashSet<String> getLastNames(String firstName, String middleName) {

        HashSet<String> set = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bnpid", "root", "root123")) {

            Statement stmt = con.createStatement();
            ResultSet result = stmt.executeQuery("select lastname from members");

            String name;

            set = new HashSet<>();

            while (result.next()) {
                name = result.getString("name");

                set.add(name);
            }

            result.close();
            stmt.close();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return set;
    }

    public HashMap<String, Parish> getParishes() {

        HashMap<String, Parish> map = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bnpid", "root", "root123")) {

            Statement stmt = con.createStatement();
            ResultSet result = stmt.executeQuery("select * from parish");

            Parish parish;
            Address address;
            String name;

            map = new HashMap<>();

            while (result.next()) {
                address = new Address(result.getString("street"), result.getString("city"), result.getString("state"), result.getString("country"),
                    result.getInt("zip"));

                name = result.getString("name");

                parish = new Parish(name, address);

                map.put(name, parish);
            }

            result.close();
            stmt.close();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return map;
    }

    public boolean addParish(Parish parish) {

        boolean ret;

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bnpid", "root", "root123")) {

            Statement stmt = con.createStatement();
            StringBuffer sql = new StringBuffer();
            sql.append("insert into parish (name, street, city, state, country, zip) values ('");
            sql.append(parish.getName()).append("', '");
            sql.append(parish.getAddress().getStreet()).append("', '");
            sql.append(parish.getAddress().getCity()).append("', '");
            sql.append(parish.getAddress().getState()).append("', '");
            sql.append(parish.getAddress().getCountry()).append("', ");
            sql.append(parish.getAddress().getZip());
            sql.append(")");

            System.out.println("Query: " + sql.toString());

            ret = stmt.execute(sql.toString());

            stmt.close();
        } catch (SQLException e) {
            System.err.println("Error " + e.getErrorCode() + " while attempting to add parish (" + parish.toString() + "): " + e.getMessage());
            ret = false;
        }

        return ret;
    }

    public int updateParish(Parish parish, String newName, Address newAddress) {

        int ret = 0;
        boolean nameUpdated = false;

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bnpid", "root", "root123")) {

            Statement stmt = con.createStatement();
            StringBuffer sql = new StringBuffer();
            sql.append("UPDATE parish SET ");

            if (!parish.getName().equals(newName)) {
                sql.append("`name`='").append(newName).append("' ");
                nameUpdated = true;
            }

            if (!parish.getAddress().equals(newAddress)) {
                if (nameUpdated) {
                    sql.append(StringConstants.COMMA_SPACE);
                }
                sql.append("`street`='").append(newAddress.getStreet()).append("' ");
                sql.append("`city`='").append(newAddress.getCity()).append("' ");
                sql.append("`state`='").append(newAddress.getState()).append("' ");
                sql.append("`country`='").append(newAddress.getCountry()).append("' ");
                sql.append("`zip`=").append(newAddress.getZip()).append(" ");
            }

            sql.append("WHERE `org.bnp.id` = ").append(parish.getId()).append(StringConstants.SPACE);
            sql.append("AND `name`='").append(parish.getName()).append("' ");
            sql.append("AND `street`='").append(parish.getAddress().getStreet()).append("' ");
            sql.append("AND `city`='").append(parish.getAddress().getCity()).append("' ");
            sql.append("AND `state`='").append(parish.getAddress().getState()).append("' ");
            sql.append("AND `country`='").append(parish.getAddress().getCountry()).append("' ");
            sql.append("AND `zip`=").append(parish.getAddress().getZip()).append(" ");
            sql.append("AND `datemodified`='").append(parish.getDateModified()).append("';");

            System.out.println("Query: " + sql.toString());

            ret = stmt.executeUpdate(sql.toString());

            stmt.close();
        } catch (SQLException e) {
            System.err.println("Error " + e.getErrorCode() + " while attempting to update parish (" + parish.toString() + "): " + e.getMessage());
        }

        return ret;
    }

    public int deleteParish(Parish parish) {

        int ret = 0;

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bnpid", "root", "root123")) {

            Statement stmt = con.createStatement();
            StringBuffer sql = new StringBuffer();
            sql.append("DELETE FROM parish ");
            sql.append("WHERE `org.bnp.id` = ").append(parish.getId()).append(StringConstants.SPACE);
            sql.append("AND `name`='").append(parish.getName()).append("' ");
            sql.append("AND `street`='").append(parish.getAddress().getStreet()).append("' ");
            sql.append("AND `city`='").append(parish.getAddress().getCity()).append("' ");
            sql.append("AND `state`='").append(parish.getAddress().getState()).append("' ");
            sql.append("AND `country`='").append(parish.getAddress().getCountry()).append("' ");
            sql.append("AND `zip`=").append(parish.getAddress().getZip()).append(" ");
            sql.append("AND `datemodified`='").append(parish.getDateModified()).append("';");

            System.out.println("Query: " + sql.toString());

            ret = stmt.executeUpdate(sql.toString());

            stmt.close();
        } catch (SQLException e) {
            System.err.println("Error " + e.getErrorCode() + " while attempting to delete parish (" + parish.toString() + "): " + e.getMessage());
        }

        return ret;
    }
}
