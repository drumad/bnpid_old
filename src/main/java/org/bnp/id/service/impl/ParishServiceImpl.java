package org.bnp.id.service.impl;

import com.mysql.cj.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.bnp.id.config.DBConfig;
import org.bnp.id.constants.StringConstants;
import org.bnp.id.exception.ParishNotFoundException;
import org.bnp.id.model.field.Address;
import org.bnp.id.model.info.Parish;
import org.bnp.id.service.ParishService;
import org.bnp.id.util.AddressUtil;
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
public class ParishServiceImpl implements ParishService {

    private DBConfig config;

    @Override
    public Parish findbyId(Integer id) {

        Parish ret = null;

        try (Connection con = config.getConnection()) {

            Statement stmt = con.createStatement();
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT * FROM `parish` WHERE `id` = ").append(id);

            ResultSet result = stmt.executeQuery(sql.toString());
            if (result.next()) {
                ret = getParish(result);
            } else {
                log.error("No parish found with id " + id);
            }

            result.close();
            stmt.close();
        } catch (SQLException e) {
            log.error("Error " + e.getErrorCode() + " while attempting to get parish with id " + id, e);
            throw new ParishNotFoundException(id);
        }

        return ret;
    }

    @Override
    public Collection<Parish> findbyName(String name) {

        List<Parish> ret = new ArrayList<>();

        try (Connection con = config.getConnection()) {

            Statement stmt = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(Integer.MIN_VALUE);
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT * FROM `parish` WHERE `name` like %").append(name).append("%");

            ResultSet result = stmt.executeQuery(sql.toString());
            while (result.next()) {
                ret.add(getParish(result));
            }

            result.close();
            stmt.close();
        } catch (SQLException e) {
            log.error("Error " + e.getErrorCode() + " while attempting to get parish with name " + name, e);
            throw new ParishNotFoundException(name);
        }

        return ret;
    }

    @Override
    public Collection<Parish> findAll() {

        List<Parish> ret = new ArrayList<>();

        try (Connection con = config.getConnection()) {

            Statement stmt = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(Integer.MIN_VALUE);
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT * FROM `parish`");

            ResultSet result = stmt.executeQuery(sql.toString());
            while (result.next()) {
                ret.add(getParish(result));
            }

            result.close();
            stmt.close();
        } catch (SQLException e) {
            log.error("Error " + e.getErrorCode() + " while attempting to get all parishes.", e);
            throw new ParishNotFoundException();
        }

        return ret;
    }

    @Override
    public Collection<Parish> findByAddress(Address address) {

        List<Parish> ret = new ArrayList<>();

        try (Connection con = config.getConnection()) {

            Statement stmt = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(Integer.MIN_VALUE);
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT * FROM `parish` WHERE `address` LIKE %");
            sql.append(AddressUtil.convert(address)).append("%");

            ResultSet result = stmt.executeQuery(sql.toString());
            while (result.next()) {
                ret.add(getParish(result));
            }

            result.close();
            stmt.close();
        } catch (SQLException e) {
            log.error("Error " + e.getErrorCode() + " while attempting to get all parishes.", e);
            throw new ParishNotFoundException();
        }

        return ret;
    }

    @Override
    public int save(Parish parish) {

        int id = 0;

        try (Connection con = config.getConnection()) {

            Statement stmt = con.createStatement();
            StringBuffer sql = new StringBuffer();

            sql.append("INSERT INTO `parish` (`name`, `address`) values ('");
            sql.append(parish.getName()).append("', '");
            sql.append(AddressUtil.convert(parish.getAddress()));
            sql.append(")");

            log.debug("Query: " + sql.toString());

            stmt.executeUpdate(sql.toString());
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            }

            stmt.close();
        } catch (SQLException e) {
            log.error("Error " + e.getErrorCode() + " while attempting to add parish (" + parish.toString() + "): " + e.getMessage(), e);
        }

        return id;
    }

    @Override
    public void update(Parish parish) {

        try (Connection con = config.getConnection()) {

            Statement stmt = con.createStatement();
            StringBuffer sql = new StringBuffer();

            sql.append("UPDATE `parish` SET ");
            sql.append("`name`='").append(parish.getName()).append("' ");
            sql.append(StringConstants.COMMA_SPACE);
            sql.append("`address`='").append(AddressUtil.convert(parish.getAddress())).append("' ");
            sql.append("WHERE `id` = ").append(parish.getId());

            log.debug("Query: " + sql.toString());

            stmt.executeUpdate(sql.toString());
            stmt.close();
        } catch (SQLException e) {
            log.error("Error " + e.getErrorCode() + " while attempting to update parish (" + parish.toString() + "): " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteById(Integer id) {

        try (Connection con = config.getConnection()) {

            Statement stmt = con.createStatement();
            StringBuffer sql = new StringBuffer();
            sql.append("DELETE FROM `parish` ");
            sql.append("WHERE `id` = ").append(id);

            log.debug("Query: " + sql.toString());

            stmt.executeUpdate(sql.toString());
            stmt.close();
        } catch (SQLException e) {
            log.error("Error " + e.getErrorCode() + " while attempting to delete parish with id (" + id + "): " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        // Do not delete all.
    }

    @Override
    public boolean isExists(Parish parish) {

        boolean ret = false;
        try (Connection con = config.getConnection()) {

            Statement stmt = con.createStatement();
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT * FROM `parish` WHERE ");
            if (parish.getId() != null && parish.getId() != 0) {
                sql.append("`id` = ").append(parish.getId());
            }

            if (StringUtils.isNullOrEmpty(parish.getName())) {
                if (!sql.toString().endsWith("WHERE ")) {
                    sql.append("AND ");
                }
                sql.append("`name` = ").append(parish.getName());
            }

            String address = AddressUtil.convert(parish.getAddress());
            if (StringUtils.isNullOrEmpty(address)) {
                if (!sql.toString().endsWith("WHERE ")) {
                    sql.append("AND ");
                }
                sql.append("`address` = ").append(address);
            }

            ResultSet rs = stmt.executeQuery(sql.toString());

            ret = rs.next();

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            log.error("Error " + e.getErrorCode() + " while attempting to find parish " + parish.toString());
        }
        return ret;
    }

    private Parish getParish(ResultSet result) throws SQLException {

        Parish ret;
        ret = new Parish();
        ret.setId(result.getInt("id"));
        ret.setName(result.getString("name"));
        ret.setAddress(AddressUtil.convert(result.getString("address")));
        ret.setDateCreated(result.getDate("date_created"));
        ret.setDateUpdated(result.getDate("date_updated"));
        return ret;
    }
}
