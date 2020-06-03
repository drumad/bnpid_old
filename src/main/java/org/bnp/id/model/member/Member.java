package org.bnp.id.model.member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.bnp.id.constants.EducationLevel;
import org.bnp.id.constants.MaritalStatus;
import org.bnp.id.constants.MemberStatus;
import org.bnp.id.model.field.Address;
import org.bnp.id.model.field.Contact;
import org.bnp.id.model.field.Name;
import org.bnp.id.model.info.Chapter;
import org.bnp.id.model.info.Parish;
import org.bnp.id.model.retreat.ClassInfo;

import java.util.Date;
import java.util.List;

/**
 * This the base class for all persons involved with Banal Na Pag-aaral. This contains all the basic contact
 * info.
 *
 * @author Andrew Madrazo
 */
@Getter
@Setter
@NoArgsConstructor
public class Member {

    @NonNull
    private Integer id;

    private Name name;

    private Address address;

    private String homePhone;

    private String workPhone;

    private String cellular;

    private String email;

    private EducationLevel educationLevel;

    private String degree;

    private String illness;

    private String physicalLimitations;

    private String dietaryRequirements;

    private String dietaryRequirementsDesc;

    private Date dateOfBirth;

    private MaritalStatus maritalStatus;

    private MemberStatus memberStatus;

    private Character sex;

    private String religion;

    private Integer contactId;

    private Parish parish;

    private Boolean briefedAboutBnp;

    private Date dateCreated;

    private Date dateUpdated;

    private String barcodeId;

    private Chapter chapter;

    private List<ClassInfo> classes;

    private Integer sponsorId;

    private Date dateAdorer;

    private Date hirangStaff;

    private Date hirangCouncil;
}
