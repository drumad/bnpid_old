package org.bnp.id.constants;

import lombok.extern.log4j.Log4j;

@Log4j
public enum MemberStatus {

    NA,
    PROSPECT,
    VISITOR,
    GRADUATE,
    ADORER,
    STAFF,
    COUNCIL,
    CHAIRPERSON;

    public static MemberStatus convert(String name) {

        try {
            return valueOf(name.toUpperCase());
        } catch(NullPointerException e) {
            log.debug("MemberStatus - returning NA.");
            return NA;
        }
    }
}
