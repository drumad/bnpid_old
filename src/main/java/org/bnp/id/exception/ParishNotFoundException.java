package org.bnp.id.exception;

public class ParishNotFoundException extends RuntimeException {

    public ParishNotFoundException(Integer id) {
        super(String.format("Parish with id [%d] was not found.", id));
    }

}
