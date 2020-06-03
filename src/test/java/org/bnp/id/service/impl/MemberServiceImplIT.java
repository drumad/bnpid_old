package org.bnp.id.service.impl;

import org.assertj.core.util.DateUtil;
import org.bnp.id.constants.EducationLevel;
import org.bnp.id.constants.MaritalStatus;
import org.bnp.id.constants.MemberStatus;
import org.bnp.id.model.member.Member;
import org.bnp.id.util.AddressUtil;
import org.bnp.id.util.NameUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class MemberServiceImplIT {

    @Autowired
    private MemberServiceImpl memberService;

    @Autowired
    private AddressUtil addressUtil;

    @Autowired
    private NameUtil nameUtil;

    @Before
    public void setUp() {


    }

    @Test
    public void test_findById() throws SQLException {

        Member member = memberService.findById(1);

        assertNotNull(member);

        assertEquals("Andrew Nicolo", member.getName().getFirstName());
        assertEquals("Francisco", member.getName().getMiddleName());
        assertEquals("Madrazo", member.getName().getLastName());
        assertEquals("Andrew", member.getName().getShortName());
        assertEquals("Andrew Nicolo \"Andrew\" Francisco Madrazo", nameUtil.convert(member.getName()));
        assertEquals("W0000000TX", member.getBarcodeId());
        assertEquals(addressUtil.convert("30 Acacia St., Juna Subd., Matina, Davao City, Davao del Sur 8021, Philippines"),
            member.getAddress());
        assertEquals(MaritalStatus.MARRIED, member.getMaritalStatus());
        assertEquals(MemberStatus.ADORER, member.getMemberStatus());
        assertEquals(EducationLevel.NA, member.getEducationLevel());
        assertEquals(1, member.getChapter().getId().intValue());
        assertEquals("Claremont Chapter", member.getChapter().getName());
        assertEquals(1, member.getParish().getId().intValue());
        assertEquals("San Pablo Parish", member.getParish().getName());
        assertEquals(DateUtil.parse("1984-06-09"), member.getDateOfBirth());
        assertEquals("9515339690", member.getCellular());
    }
}