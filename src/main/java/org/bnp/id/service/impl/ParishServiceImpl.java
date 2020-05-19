package org.bnp.id.service.impl;

import com.mysql.cj.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.bnp.id.config.DBConfig;
import org.bnp.id.exception.ParishNotFoundException;
import org.bnp.id.model.field.Address;
import org.bnp.id.model.info.Parish;
import org.bnp.id.service.ParishService;
import org.bnp.id.util.AddressUtil;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@Log4j
@Service
public class ParishServiceImpl implements ParishService {

    private DBConfig config;

    private AddressUtil addressUtil;

    @Override
    public Parish findById(Integer id) {

        Parish ret = null;

        try (Connection con = config.getConnection()) {

            PreparedStatement stmt = con.prepareStatement("SELECT * FROM `parish` WHERE `id` = ?");
            stmt.setInt(1, id);

            ResultSet result = stmt.executeQuery();
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
    public Collection<Parish> findByName(String name) {

        List<Parish> ret = new ArrayList<>();

        try (Connection con = config.getConnection()) {

            PreparedStatement stmt =
                con.prepareStatement("SELECT * FROM `parish` WHERE `name` like ?", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(Integer.MIN_VALUE);
            stmt.setString(1, "%" + name + "%");

            ResultSet result = stmt.executeQuery();
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

            PreparedStatement stmt = con.prepareStatement("SELECT * FROM `parish` WHERE `address` LIKE ?", ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(Integer.MIN_VALUE);
            stmt.setString(1, addressUtil.convert(address));

            ResultSet result = stmt.executeQuery();
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
    public Collection<Parish> findByAddress(String addr) {

        List<Parish> ret = new ArrayList<>();

        try (Connection con = config.getConnection()) {

            PreparedStatement stmt = con.prepareStatement("SELECT * FROM `parish` WHERE `address` LIKE ?", ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(Integer.MIN_VALUE);
            stmt.setString(1, "%" + addr + "%");

            ResultSet result = stmt.executeQuery();
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
    public int save(Parish parish) throws SQLException {

        int id = 0;

        try (Connection con = config.getConnection()) {

            PreparedStatement stmt = con.prepareStatement("INSERT INTO `parish` (`name`, `address`) values (?, ?)");
            stmt.setString(1, parish.getName());
            stmt.setString(2, addressUtil.convert(parish.getAddress()));

            log.debug("Query: " + stmt.toString());

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            log.error("Error " + e.getErrorCode() + " while attempting to add parish (" + parish.getName() + "): " + e.getMessage(), e);
            throw e;
        }

        return id;
    }

    @Override
    public int update(Parish parish) throws SQLException {

        int updated = 0;

        try (Connection con = config.getConnection()) {

            PreparedStatement stmt = con.prepareStatement("UPDATE `parish` SET `name` = ?, `address` = ? WHERE `id` = ?");
            stmt.setString(1, parish.getName());
            stmt.setString(2, addressUtil.convert(parish.getAddress()));
            stmt.setInt(3, parish.getId());

            log.debug("Query: " + stmt.toString());

            updated = stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            log.error("Error " + e.getErrorCode() + " while attempting to update parish (" + parish.toString() + "): " + e.getMessage(), e);
            throw e;
        }

        return updated;
    }

    @Override
    public int deleteById(Integer id) throws SQLException {

        int deleted = 0;

        try (Connection con = config.getConnection()) {

            PreparedStatement stmt = con.prepareStatement("DELETE FROM `parish` WHERE `id` = ?");
            stmt.setInt(1, id);

            log.debug("Query: " + stmt.toString());

            deleted = stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            log.error("Error " + e.getErrorCode() + " while attempting to delete parish with id (" + id + "): " + e.getMessage());
            throw e;
        }

        return deleted;
    }

    @Override
    public void deleteAll() {
        // Do not delete all.
    }

    @Override
    public boolean isExists(Parish parish) {

        boolean ret = false;
        boolean idBool = false;
        boolean nameBool = false;
        boolean addressBool = false;

        try (Connection con = config.getConnection()) {

            StringBuffer sql = new StringBuffer();
            int paramIndex = 0;

            sql.append("SELECT * FROM `parish` WHERE ");
            if (parish.getId() != null && parish.getId() != 0) {
                sql.append("`id` = ?");
                idBool = true;
            }

            if (!StringUtils.isNullOrEmpty(parish.getName())) {
                if (!sql.toString().endsWith("WHERE ")) {
                    sql.append(" AND ");
                }
                sql.append("`name` = ?");
                nameBool = true;
            }

            String address = addressUtil.convert(parish.getAddress());
            if (!StringUtils.isNullOrEmpty(address)) {
                if (!sql.toString().endsWith("WHERE ")) {
                    sql.append(" AND ");
                }
                sql.append("`address` = ?");
                addressBool = true;
            }

            log.debug("Query = " + sql.toString());

            PreparedStatement stmt = con.prepareStatement(sql.toString());
            if (idBool) {
                stmt.setInt(++paramIndex, parish.getId());
            }

            if (nameBool) {
                stmt.setString(++paramIndex, parish.getName());
            }

            if (addressBool) {
                stmt.setString(++paramIndex, addressUtil.convert(parish.getAddress()));
            }

            log.debug("Query: " + stmt.toString());

            ResultSet rs = stmt.executeQuery(sql.toString());

            ret = rs.next();

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            log.error("Error " + e.getErrorCode() + " while attempting to find parish " + parish.toString(), e);
        }
        return ret;
    }

    private Parish getParish(ResultSet result) throws SQLException {

        Parish ret;
        ret = new Parish();
        ret.setId(result.getInt("id"));
        ret.setName(result.getString("name"));
        ret.setAddress(addressUtil.convert(result.getString("address")));
        ret.setDateCreated(result.getDate("date_created"));
        ret.setDateUpdated(result.getDate("date_updated"));
        return ret;
    }
}

