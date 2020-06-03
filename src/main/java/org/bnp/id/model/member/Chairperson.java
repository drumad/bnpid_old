package org.bnp.id.model.member;

import org.bnp.id.model.field.Name;

import java.sql.Date;

/**
 * This is the class that will hold the chairperson's info. There shall only be one static _instance of this.
 * Cannot be instantiated, nor modified. All info is static and final.
 */
public class Chairperson extends Member {

    public static Chairperson _instance = new Chairperson();

    private Chairperson() {
        this.setName(new Name("Salve", "Aguillon", "Stuart", "", "Ate Salve"));
        this.setDateOfBirth(Date.valueOf("March 14, 1947"));
        this.setSex('F');
    }

    public static Chairperson getInstance() {
        return _instance;
    }
}
