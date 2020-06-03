package org.bnp.id.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.bnp.id.config.DBConfig;
import org.bnp.id.constants.EducationLevel;
import org.bnp.id.constants.MaritalStatus;
import org.bnp.id.constants.MemberStatus;
import org.bnp.id.exception.MemberNotFoundException;
import org.bnp.id.model.field.Name;
import org.bnp.id.model.member.Member;
import org.bnp.id.service.ChapterService;
import org.bnp.id.service.MemberService;
import org.bnp.id.service.ParishService;
import org.bnp.id.util.AddressUtil;
import org.bnp.id.util.NameUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@Log4j
public class MemberServiceImpl implements MemberService {

    private DBConfig config;

    private NameUtil nameUtil;

    private AddressUtil addressUtil;

    private ChapterService chapterService;

    private ParishService parishService;

    @Override
    public Member findById(Integer id) throws SQLException {

        Member member;

        try (Connection con = config.getConnection()) {

            PreparedStatement stmt = con.prepareStatement(
                "SELECT m.*, c.*, p.* FROM `members` m LEFT JOIN `chapter_info` c ON m.chapter_id = c.chapter_id "
                    + "LEFT JOIN `parish` p ON m.parish_id = p.parish_id WHERE m.member_id = ?");
            stmt.setInt(1, id);

            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                member = getObject(result);
            } else {
                throw new MemberNotFoundException(id);
            }

        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw e;
        }
        return member;
    }

    @Override
    public Collection<Member> findByName(String name) throws SQLException {

        List<Member> members = new ArrayList<>();

        try (Connection con = config.getConnection()) {

            PreparedStatement stmt = con.prepareStatement(
                "SELECT m.*, c.*, p.* FROM `members` m LEFT JOIN `chapter_info` c ON m.chapter_id = c.chapter_id "
                    + "LEFT JOIN `parish` p ON m.parish_id = p.parish_id "
                    + "WHERE concat(m.first_name, ' ', m.short_name, ' ', m.middle_name, ' ', m.last_name) like ?");
            stmt.setString(1, "%" + name + "%");

            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                members.add(getObject(result));
            }

            if (members.isEmpty()) {
                throw new MemberNotFoundException(name);
            }

            result.close();
            stmt.close();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw e;
        }
        return members;
    }

    @Override
    public Collection<Member> findAll() throws SQLException {

        List<Member> members = new ArrayList<>();

        try (Connection con = config.getConnection()) {

            PreparedStatement stmt = con.prepareStatement(
                "SELECT m.*, c.*, p.* FROM `members` m LEFT JOIN `chapter_info` c ON m.chapter_id = c.chapter_id "
                    + "LEFT JOIN `parish` p ON m.parish_id = p.parish_id");

            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                members.add(getObject(result));
            }

            if (members.isEmpty()) {
                throw new MemberNotFoundException();
            }

            result.close();
            stmt.close();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw e;
        }
        return members;
    }

    @Override
    public int save(Member member) throws SQLException {

        int id = 0;

        try (Connection con = config.getConnection()) {

            PreparedStatement stmt = con.prepareStatement(
                "INSERT INTO `members` (barcode_id, last_name, first_name, middle_name, suffix, short_name, "
                    + "member_address, home_phone, work_phone, cellular, email, date_of_birth, marital_status, member_status, sex, "
                    + "religion, education_level, degree, illness, physical_limitations, dietary_requirements, "
                    + "dietary_requirements_desc, contact_id, sponsor_id, chapter_id, parish_id, date_adorer, date_hirang, date_council, "
                    + "briefed_about_bnp) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);

            setValues(member, stmt);

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            log.error("Error " + e.getErrorCode() + " while attempting to add member (" + member.getName() + "): " + e.getMessage(), e);
            throw e;
        }

        return id;
    }

    @Override
    public int update(Member member) throws SQLException {

        int rows = 0;

        try (Connection con = config.getConnection()) {

            PreparedStatement stmt = con.prepareStatement(
                "UPDATE `members` SET barcode_id = ?, last_name = ?, first_name = ?, middle_name = ?, suffix = ?, short_name = ?, "
                    + "member_address = ?, home_phone = ?, work_phone = ?, cellular = ?, email = ?, date_of_birth = ?, marital_status = ?, "
                    + "member_status = ?, sex = ?, religion = ?, education_level = ?, degree = ?, illness = ?, physical_limitations = ?, "
                    + "dietary_requirements = ?, dietary_requirements_desc = ?, contact_id = ?, sponsor_id = ?, chapter_id = ?, "
                    + "parish_id = ?, date_adorer = ?, date_hirang = ?, date_council = ?, briefed_about_bnp = ? WHERE member_id = ?");

            int i = setValues(member, stmt);
            stmt.setInt(i++, member.getId());

            stmt.executeUpdate();

            stmt.close();
        } catch (SQLException e) {
            log.error("Error " + e.getErrorCode() + " while attempting to update member (" + member.getName() + "): " + e.getMessage(), e);
            throw e;
        }

        return rows;
    }

    @Override
    public int deleteById(Integer id) throws SQLException {

        int rows = 0;

        try (Connection con = config.getConnection()) {

            PreparedStatement stmt = con.prepareStatement("DELETE FROM `members` WHERE member_id = ?");

            stmt.setInt(1, id);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            log.error("Error " + e.getErrorCode() + " while attempting to delete member with id (" + id + "): " + e.getMessage(), e);
            throw e;
        }

        return rows;
    }

    @Override
    public int count() throws SQLException {

        int count = 0;

        try (Connection con = config.getConnection()) {

            PreparedStatement stmt = con.prepareStatement("SELECT count(*) FROM `members`");

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }
            stmt.close();
        } catch (SQLException e) {
            log.error("Error " + e.getErrorCode() + " while attempting to get the number of members: " + e.getMessage(), e);
            throw e;
        }

        return count;
    }

    @Override
    public void deleteAll() {

        throw new UnsupportedOperationException("Unsupported deleteAll() for members.");
    }

    @Override
    public boolean isExists(Member member) throws SQLException {

        return false;
    }

    @Override
    public Member getObject(ResultSet result) throws SQLException {

        Member member = new Member();
        member.setId(result.getInt("member_id"));
        member.setBarcodeId(result.getString("barcode_id"));

        String first = result.getString("first_name");
        String middle = result.getString("middle_name");
        String last = result.getString("last_name");
        String suffix = result.getString("suffix");
        String shortName = result.getString("short_name");

        member.setName(new Name(first, middle, last, suffix, shortName));
        member.setAddress(addressUtil.convert(result.getString("member_address")));

        member.setHomePhone(result.getString("home_phone"));
        member.setWorkPhone(result.getString("work_phone"));
        member.setCellular(result.getString("cellular"));

        member.setEmail(result.getString("email"));
        member.setEducationLevel(EducationLevel.convert(result.getString("education_level")));
        member.setDegree(result.getString("degree"));
        member.setDateOfBirth(result.getDate("date_of_birth"));

        member.setSex(result.getString("gender").charAt(0));
        member.setMaritalStatus(MaritalStatus.convert(result.getString("marital_status")));
        member.setMemberStatus(MemberStatus.convert(result.getString("member_status")));
        member.setContactId(result.getInt("contact_id"));

        member.setParish(parishService.getObject(result));

        member.setBriefedAboutBnp(result.getBoolean("briefed_about_bnp"));

        member.setDateCreated(result.getDate("date_created"));
        member.setDateUpdated(result.getDate("date_updated"));

        member.setBarcodeId(result.getString("barcode_id"));
        member.setChapter(chapterService.getObject(result));

        // TODO: Fill list of classes here after ClassInfoService is finished.

        member.setSponsorId(result.getInt("sponsor_id"));
        member.setDateAdorer(result.getDate("date_adorer"));
        member.setHirangStaff(result.getDate("date_hirang"));
        member.setHirangCouncil(result.getDate("date_council"));

        return member;
    }

    private int setValues(Member member, PreparedStatement stmt) throws SQLException {

        int i = 1;

        // TODO: Compute for BarcodeID here
        stmt.setString(i++, "BarcodeID");
        stmt.setString(i++, member.getName().getLastName());
        stmt.setString(i++, member.getName().getFirstName());
        stmt.setString(i++, member.getName().getMiddleName());
        stmt.setString(i++, member.getName().getSuffix());
        stmt.setString(i++, member.getName().getShortName());
        stmt.setString(i++, addressUtil.convert(member.getAddress()));
        stmt.setString(i++, member.getHomePhone());
        stmt.setString(i++, member.getWorkPhone());
        stmt.setString(i++, member.getCellular());
        stmt.setString(i++, member.getEmail());
        stmt.setDate(i++, new Date(member.getDateOfBirth().getTime()));
        stmt.setString(i++, member.getMaritalStatus().name());
        stmt.setString(i++, member.getMemberStatus().name());
        stmt.setString(i++, member.getSex().toString());
        stmt.setString(i++, member.getReligion());
        stmt.setString(i++, member.getEducationLevel().name());
        stmt.setString(i++, member.getDegree());
        stmt.setString(i++, member.getIllness());
        stmt.setString(i++, member.getPhysicalLimitations());
        stmt.setString(i++, member.getDietaryRequirements());
        stmt.setString(i++, member.getDietaryRequirementsDesc());
        stmt.setInt(i++, member.getContactId());
        stmt.setInt(i++, member.getSponsorId());
        stmt.setInt(i++, member.getChapter().getId());
        stmt.setInt(i++, member.getParish().getId());
        stmt.setDate(i++, new Date(member.getDateAdorer().getTime()));
        stmt.setDate(i++, new Date(member.getHirangStaff().getTime()));
        stmt.setDate(i++, new Date(member.getHirangCouncil().getTime()));
        stmt.setBoolean(i++, member.getBriefedAboutBnp());

        return i;
    }
}
