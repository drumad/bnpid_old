package org.bnp.id.exception;

public class CountryNotFoundException extends RuntimeException {

    public CountryNotFoundException() {

        super("No country found.");
    }

    public CountryNotFoundException(Integer id) {

        super(String.format("Country with id [%d] was not found.", id));
    }

    public CountryNotFoundException(String name) {

        super(String.format("Country containing [%s] in name was not found.", name));
    }

}
