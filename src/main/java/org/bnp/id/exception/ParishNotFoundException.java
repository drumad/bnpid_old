package org.bnp.id.exception;

public class ParishNotFoundException extends RuntimeException {

    public ParishNotFoundException() {
        super("No parish found!");
    }

    public ParishNotFoundException(Integer id) {

        super(String.format("Parish with id [%d] was not found.", id));
    }

    public ParishNotFoundException(String name) {
        super(String.format("Parish containing [%s] in name was not found.", name));
    }
}
