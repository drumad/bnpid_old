package org.bnp.id.constants;

import lombok.extern.log4j.Log4j;

@Log4j
public enum MaritalStatus {

    NA,
    SINGLE,
    MARRIED,
    DIVORCE,
    SEPARATED,
    WIDOWED;

    public static MaritalStatus convert(String name) {

        try {
            return valueOf(name.toUpperCase());
        } catch (NullPointerException e) {
            log.debug("MaritalStatus - returning NA.");
            return NA;
        }
    }

}
