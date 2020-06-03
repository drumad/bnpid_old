package org.bnp.id.constants;

import lombok.extern.log4j.Log4j;

@Log4j
public enum EducationLevel {

    NA,
    ELEMENTARY,
    MIDDLE_SCHOOL,
    HIGH_SCHOOL,
    BACHELORS,
    MASTERS,
    DOCTORATE,
    PROFESSIONAL;

    public static EducationLevel convert(String name) {

        try {
            return valueOf(name.toUpperCase());
        } catch (NullPointerException e) {
            log.debug("EducationLevel - returning NA.");
            return NA;
        }
    }

}
