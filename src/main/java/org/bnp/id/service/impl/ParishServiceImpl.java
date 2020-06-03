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
    public Parish findById(Integer id) throws SQLException {

        Parish ret;

        try (Connection con = config.getConnection()) {

            PreparedStatement stmt = con.prepareStatement("SELECT * FROM `parish` WHERE parish_id = ?");
            stmt.setInt(1, id);

            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                ret = getObject(result);
            } else {
                throw new ParishNotFoundException(id);
            }

            result.close();
            stmt.close();
        } catch (SQLException e) {
            log.error("Error " + e.getErrorCode() + " while attempting to get parish with id " + id, e);
            throw e;
        }

        return ret;
    }

    @Override
    public Collection<Parish> findByName(String name) throws SQLException {

        List<Parish> ret = new ArrayList<>();

        try (Connection con = config.getConnection()) {

            PreparedStatement stmt =
                con.prepareStatement("SELECT * FROM `parish` WHERE upper(`parish_name`) like ?", ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY);

            collectParishes(stmt, name, ret);
            stmt.close();
        } catch (SQLException e) {
            log.error("Error " + e.getErrorCode() + " while attempting to get parish with name " + name, e);
            throw e;
        }

        return ret;
    }

    private void collectParishes(PreparedStatement stmt, String searchString, List<Parish> ret) throws SQLException {

        stmt.setFetchSize(Integer.MIN_VALUE);
        stmt.setString(1, "%" + searchString.toUpperCase() + "%");

        log.debug("Query: " + stmt);

        ResultSet result = stmt.executeQuery();
        while (result.next()) {
            ret.add(getObject(result));
        }

        result.close();
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
                ret.add(getObject(result));
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
    public Collection<Parish> findByAddress(Address address) throws SQLException {

        List<Parish> ret = new ArrayList<>();

        try (Connection con = config.getConnection()) {

            PreparedStatement stmt =
                con.prepareStatement("SELECT * FROM `parish` WHERE upper(`parish_address`) LIKE ?", ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY);
            collectParishes(stmt, addressUtil.removeCommas(addressUtil.convert(address)), ret);

            stmt.close();
        } catch (SQLException e) {
            log.error("Error " + e.getErrorCode() + " while attempting to get all parishes.", e);
            throw e;
        }

        return ret;
    }

    @Override
    public Collection<Parish> findByAddress(String addr) throws SQLException {

        List<Parish> ret = new ArrayList<>();

        try (Connection con = config.getConnection()) {

            PreparedStatement stmt =
                con.prepareStatement("SELECT * FROM `parish` WHERE upper(`parish_address`) LIKE ?", ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY);
            collectParishes(stmt, addr, ret);

            stmt.close();
        } catch (SQLException e) {
            log.error("Error " + e.getErrorCode() + " while attempting to get all parishes.", e);
            throw e;
        }

        return ret;
    }

    @Override
    public int save(Parish parish) throws SQLException {

        int id = 0;

        try (Connection con = config.getConnection()) {

            PreparedStatement stmt = con.prepareStatement("INSERT INTO `parish` (`parish_name`, `parish_address`) values (?, ?)",
                Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, parish.getName());
            stmt.setString(2, addressUtil.convert(parish.getAddress()));

            log.debug("Query: " + stmt);

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

            PreparedStatement stmt =
                con.prepareStatement("UPDATE `parish` SET `parish_name` = ?, `parish_address` = ? WHERE parish_id = ?");
            stmt.setString(1, parish.getName());
            stmt.setString(2, addressUtil.convert(parish.getAddress()));
            stmt.setInt(3, parish.getId());

            log.debug("Query: " + stmt);

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

            PreparedStatement stmt = con.prepareStatement("DELETE FROM `parish` WHERE parish_id = ?");
            stmt.setInt(1, id);

            log.debug("Query: " + stmt);

            deleted = stmt.executeUpdate();
            stmt.close();

            // Reset auto-increment
            stmt = con.prepareStatement("ALTER TABLE `parish` AUTO_INCREMENT = 1");

            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            log.error("Error " + e.getErrorCode() + " while attempting to delete parish with id (" + id + "): " + e.getMessage());
            throw e;
        }

        return deleted;
    }

    @Override
    public int count() throws SQLException {

        int count = 0;

        try (Connection con = config.getConnection()) {

            PreparedStatement stmt = con.prepareStatement("SELECT count(*) FROM `parish`");

            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                count = result.getInt(1);
            }

            result.close();
            stmt.close();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw e;
        }
        return count;
    }

    @Override
    public void deleteAll() {
        // Do not delete all.
    }

    @Override
    public boolean isExists(Parish parish) throws SQLException {

        boolean exists;

        try (Connection con = config.getConnection()) {

            StringBuffer sql = new StringBuffer();

            sql.append("SELECT * FROM `parish` WHERE ");
            int originalLength = sql.length();

            if (parish.getId() != null && parish.getId() != 0) {
                sql.append("`id` = ").append(parish.getId()).append(" ");
            }

            if (!StringUtils.isNullOrEmpty(parish.getName())) {
                appendAnd(sql, originalLength);
                sql.append("`parish_name` LIKE '%").append(parish.getName()).append("%'");
            }

            String address = addressUtil.removeCommas(addressUtil.convert(parish.getAddress()));
            if (!StringUtils.isNullOrEmpty(address)) {
                appendAnd(sql, originalLength);
                sql.append("`parish_address` LIKE '%").append(address).append("%'");
            }

            log.debug("Query: " + sql.toString());

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql.toString());

            exists = rs.next();

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            log.error("Error " + e.getErrorCode() + " while attempting to find parish " + parish.toString(), e);
            throw e;
        }
        return exists;
    }

    @Override
    public Parish getObject(ResultSet result) throws SQLException {

        Parish ret;
        ret = new Parish();
        ret.setId(result.getInt("parish_id"));
        ret.setName(result.getString("parish_name"));
        ret.setAddress(addressUtil.convert(result.getString("parish_address")));
        ret.setDateCreated(result.getDate("date_created"));
        ret.setDateUpdated(result.getDate("date_updated"));
        return ret;
    }
}

