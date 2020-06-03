package org.bnp.id.service.impl;

import com.mysql.cj.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.bnp.id.config.DBConfig;
import org.bnp.id.exception.ChapterNotFoundException;
import org.bnp.id.model.info.Chapter;
import org.bnp.id.model.info.Country;
import org.bnp.id.service.ChapterService;
import org.bnp.id.util.AddressUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@Log4j
public class ChapterServiceImpl implements ChapterService {

    private DBConfig config;

    private AddressUtil addressUtil;

    @Override
    public Chapter findById(Integer id) throws SQLException {

        Chapter ret = null;

        try (Connection con = config.getConnection()) {

            PreparedStatement stmt = con.prepareStatement("SELECT * FROM `chapter_info` WHERE chapter_id = ?");
            stmt.setInt(1, id);

            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                ret = getObject(result);
            } else {
                log.error("No chapter found with id " + id);
            }

            result.close();
            stmt.close();

            if (ret == null) {
                throw new ChapterNotFoundException(id);
            }

        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw e;
        }

        return ret;
    }

    @Override
    public Collection<Chapter> findByName(String name) throws SQLException {

        List<Chapter> ret = new ArrayList<>();

        try (Connection con = config.getConnection()) {

            PreparedStatement stmt =
                con.prepareStatement("SELECT * FROM `chapter_info` WHERE `chapter_name` like ?", ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(Integer.MIN_VALUE);
            stmt.setString(1, "%" + name + "%");

            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                ret.add(getObject(result));
            }

            result.close();
            stmt.close();

            if (ret.isEmpty()) {
                throw new ChapterNotFoundException(name);
            }
        } catch (SQLException e) {
            log.error("Error " + e.getErrorCode() + " while attempting to get parish with name " + name, e);
            throw e;
        }

        return ret;
    }

    @Override
    public Collection<Chapter> findAll() throws SQLException {

        List<Chapter> ret = new ArrayList<>();

        try (Connection con = config.getConnection()) {

            Statement stmt = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(Integer.MIN_VALUE);
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT * FROM `chapter_info`");

            ResultSet result = stmt.executeQuery(sql.toString());
            while (result.next()) {
                ret.add(getObject(result));
            }

            result.close();
            stmt.close();

            if (ret.isEmpty()) {
                throw new ChapterNotFoundException();
            }
        } catch (SQLException e) {
            log.error("Error " + e.getErrorCode() + " while attempting to get all parishes.", e);
            throw e;
        }

        return ret;
    }

    @Override
    public int save(Chapter chapter) throws SQLException {

        int id = 0;

        try (Connection con = config.getConnection()) {

            PreparedStatement stmt =
                con.prepareStatement("INSERT INTO `chapter_info` (`chapter_name`, `location`, `started`) values (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, chapter.getName());
            stmt.setString(2, addressUtil.convert(chapter.getLocation()));
            stmt.setDate(3, new Date(chapter.getStarted().getTime()));

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            log.error("Error " + e.getErrorCode() + " while attempting to add parish (" + chapter.getName() + "): " + e.getMessage(), e);
            throw e;
        }

        return id;
    }

    @Override
    public int update(Chapter chapter) throws SQLException {

        int rows;

        try (Connection con = config.getConnection()) {

            PreparedStatement stmt =
                con.prepareStatement("UPDATE `chapter_info` SET `chapter_name`=?, `location`=?, `started`=? WHERE chapter_id=?");
            stmt.setString(1, chapter.getName());
            stmt.setString(2, addressUtil.convert(chapter.getLocation()));
            stmt.setDate(3, new Date(chapter.getStarted().getTime()));
            stmt.setInt(4, chapter.getId());

            log.debug("Query = " + stmt.toString());

            rows = stmt.executeUpdate();

            stmt.close();
        } catch (SQLException e) {
            log.error("Error " + e.getErrorCode() + " while attempting to update parish (" + chapter.getName() + "): " + e.getMessage(), e);
            throw e;
        }

        return rows;
    }

    @Override
    public int deleteById(Integer id) throws SQLException {

        int rows;

        try (Connection con = config.getConnection()) {

            PreparedStatement stmt = con.prepareStatement("DELETE FROM `chapter_info` WHERE chapter_id=?");
            stmt.setInt(1, id);

            log.debug("Query = " + stmt.toString());

            rows = stmt.executeUpdate();

            stmt.close();
        } catch (SQLException e) {
            log.error("Error " + e.getErrorCode() + " while attempting to delete parish with id [" + id + "]: " + e.getMessage(), e);
            throw e;
        }

        return rows;
    }

    @Override
    public int count() throws SQLException {

        int count = 0;

        try (Connection con = config.getConnection()) {

            PreparedStatement stmt = con.prepareStatement("SELECT count(*) FROM `chapter_info`");

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

        // Don't do anything.
    }

    @Override
    public boolean isExists(Chapter chapter) throws SQLException {

        boolean exists;
        try (Connection con = config.getConnection()) {

            StringBuffer sql = new StringBuffer();
            sql.append("SELECT * FROM `chapter_info` WHERE ");
            int originalLength = sql.length();
            if (chapter.getId() != null && chapter.getId() != 0) {
                sql.append("`id` = ").append(chapter.getId());
            }

            if (!StringUtils.isNullOrEmpty(chapter.getName())) {
                appendAnd(sql, originalLength);
                sql.append("`name` LIKE '%").append(chapter.getName()).append("%'");
            }

            String address = addressUtil.removeCommas(addressUtil.convert(chapter.getLocation()));
            if (!StringUtils.isNullOrEmpty(address)) {
                appendAnd(sql, originalLength);
                sql.append("`location` LIKE '%").append(address).append("%'");
            }

            if (chapter.getStarted() != null) {
                appendAnd(sql, originalLength);
                sql.append("`started` = '").append(new Date(chapter.getStarted().getTime())).append("'");
            }

            if (sql.length() == originalLength) {
                sql.delete(sql.indexOf("WHERE"), originalLength);
            }

            log.debug("Query = " + sql.toString());

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql.toString());

            exists = rs.next();

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            log.error("Error " + e.getErrorCode() + " while attempting to find chapter " + chapter.getName().concat(" | ").concat(
                addressUtil.removeCommas(addressUtil.convert(chapter.getLocation()))), e);
            throw e;

        }
        return exists;
    }

    @Override
    public Collection<Chapter> findByCountry(Country country) throws SQLException {

        List<Chapter> ret = new ArrayList<>();

        try (Connection con = config.getConnection()) {

            PreparedStatement stmt =
                con.prepareStatement("SELECT * FROM `chapter_info` WHERE `location` like ?", ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(Integer.MIN_VALUE);
            stmt.setString(1, "%" + country.getNiceName());

            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                ret.add(getObject(result));
            }

            result.close();
            stmt.close();

            if (ret.isEmpty()) {
                throw new ChapterNotFoundException(country);
            }
        } catch (SQLException e) {
            log.error("Error " + e.getErrorCode() + " while attempting to get chapter with location " + country.getNiceName(), e);
            throw e;
        }

        return ret;
    }

    @Override
    public Chapter getObject(ResultSet result) throws SQLException {

        Chapter ret;
        ret = new Chapter();
        ret.setId(result.getInt("chapter_id"));
        ret.setName(result.getString("chapter_name"));
        ret.setLocation(addressUtil.convert(result.getString("location")));
        ret.setStarted(result.getDate("started"));
        ret.setDateCreated(result.getDate("date_created"));
        ret.setDateUpdated(result.getDate("date_updated"));
        return ret;
    }
}
