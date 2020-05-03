package org.bnp.id.controller;

import lombok.Getter;
import lombok.Setter;
import org.bnp.id.config.DBConfig;
import org.bnp.id.constants.StringConstants;
import org.bnp.id.exception.ParishNotFoundException;
import org.bnp.id.model.field.Address;
import org.bnp.id.model.info.Parish;
import org.bnp.id.util.AddressUtil;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@RestController
public class ParishController {

    private Parish model;

    private Map<Integer, String> countryMap;

    private DBConfig config;

    private DBController dbController;

    @RequestMapping("/parish")
    public String view() {

        return "Hello Parish World!";
    }

    @GetMapping("/parish/{id}")
    public Parish getParish(@PathVariable Integer id) {

        Parish ret = null;

        try (Connection con = config.getConnection()) {

            Statement stmt = con.createStatement();
            StringBuffer sql = new StringBuffer();
            sql.append("select * from parish where id = ").append(id);

            ResultSet result = stmt.executeQuery(sql.toString());
            if (result.next()) {
                ret = new Parish();
                ret.setId(result.getInt("id"));
                ret.setName(result.getString("name"));
                ret.setAddress(AddressUtil.convert(result.getString("address")));
                ret.setDateCreated(result.getDate("date_created"));
                ret.setDateUpdated(result.getDate("date_updated"));
            } else {
                System.err.println("No parish found with id " + id);
            }

            result.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Error " + e.getErrorCode() + " while attempting to get parish with id " + id);
            throw new ParishNotFoundException(id);
        }

        return ret;
    }

    @GetMapping("/parish/list")
    public List<Parish> getParishes() {

        List<Parish> ret = new ArrayList<>();

        try (Connection con = config.getConnection()) {

            Statement stmt = con.createStatement();
            String sql = "select * from parish";

            ResultSet result = stmt.executeQuery(sql);

            while (result.next()) {
                Parish temp = new Parish();
                temp.setId(result.getInt("id"));
                temp.setName(result.getString("name"));
                temp.setAddress(AddressUtil.convert(result.getString("address")));
                temp.setDateCreated(result.getDate("date_created"));
                temp.setDateUpdated(result.getDate("date_updated"));

                ret.add(temp);
            }

            result.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Error " + e.getErrorCode() + " while attempting to get the list of parishes");
        }

        return ret;
    }

    @PostMapping("/parish")
    public boolean addParish(@RequestBody Parish parish) {

        boolean ret;

        try (Connection con = config.getConnection()) {

            Statement stmt = con.createStatement();
            StringBuffer sql = new StringBuffer();
            sql.append("insert into parish (name, address) values ('");
            sql.append(parish.getName()).append("', '");
            sql.append(AddressUtil.convert(parish.getAddress()));
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

    @PutMapping("/parish/{id}")
    public int updateParish(@PathVariable Integer id, @RequestBody Parish parish) {

        int ret = 0;
        boolean nameUpdated = false;

        try (Connection con = config.getConnection()) {

            Statement stmt = con.createStatement();
            StringBuffer sql = new StringBuffer();
            sql.append("UPDATE parish SET ");

            String newName = parish.getName();
            Address newAddress = parish.getAddress();

            if (!parish.getName().equals(newName)) {
                sql.append("`name`='").append(newName).append("' ");
                nameUpdated = true;
            }

            if (!parish.getAddress().equals(newAddress)) {
                if (nameUpdated) {
                    sql.append(StringConstants.COMMA_SPACE);
                }
                sql.append("`address`='").append(newAddress.toString()).append("' ");
            }

            sql.append("WHERE `id` = ").append(id);

            System.out.println("Query: " + sql.toString());

            ret = stmt.executeUpdate(sql.toString());

            stmt.close();
        } catch (SQLException e) {
            System.err.println("Error " + e.getErrorCode() + " while attempting to update parish (" + parish.toString() + "): " + e.getMessage());
        }

        return ret;
    }

    @DeleteMapping("/employees/{id}")
    public int deleteParish(@PathVariable Integer id) {

        int ret = 0;

        try (Connection con = config.getConnection()) {

            Statement stmt = con.createStatement();
            StringBuffer sql = new StringBuffer();
            sql.append("DELETE FROM parish ");
            sql.append("WHERE `id` = ").append(id);

            System.out.println("Query: " + sql.toString());

            ret = stmt.executeUpdate(sql.toString());
            stmt.close();

            dbController.getParishes().remove(id);

        } catch (SQLException e) {
            System.err.println("Error " + e.getErrorCode() + " while attempting to delete parish with id (" + id + "): " + e.getMessage());
        }

        return ret;
    }
}
