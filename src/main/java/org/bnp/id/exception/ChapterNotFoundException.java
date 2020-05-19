package org.bnp.id.exception;

import org.bnp.id.model.info.Country;

public class ChapterNotFoundException extends RuntimeException {

    public ChapterNotFoundException() {

        super("No chapter found!");
    }

    public ChapterNotFoundException(Integer id) {

        super(String.format("Chapter with id [%d] was not found.", id));
    }

    public ChapterNotFoundException(String name) {

        super(String.format("Chapter containing [%s] in name was not found.", name));
    }

    public ChapterNotFoundException(Country country) {

        super(String.format("Chapter from [%s] was not found.", country.getNiceName()));
    }
}
