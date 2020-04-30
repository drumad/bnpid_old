package org.bnp.id.model.member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bnp.id.constants.EducationLevel;
import org.bnp.id.constants.MaritalStatus;
import org.bnp.id.model.field.Address;
import org.bnp.id.model.field.Contact;
import org.bnp.id.model.field.Name;
import org.bnp.id.model.info.Parish;

import java.util.Date;

/**
 * This the base class for all persons involved with Banal Na Pag-aaral. This contains all the basic contact
 * info.
 *
 * @author Andrew Madrazo
 */
@Getter
@Setter
@NoArgsConstructor
public class Person {

    private Integer id;

    private Name name;

    private Address address;

    private Contact homePhone;

    private Contact workPhone;

    private Contact mobilePhone;

    private EducationLevel educationLevel;

    private String email;

    private String degree;

    private Date birthday;

    private Character gender;

    private MaritalStatus maritalStatus;

    private Adorer contactPerson;

    private Parish parish;

    private Boolean briefedAboutBnp;
}
