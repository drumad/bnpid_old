package org.bnp.id.exception;

public class MemberNotFoundException extends RuntimeException {

    public MemberNotFoundException() {

        super("No members found!");
    }

    public MemberNotFoundException(Integer id) {

        super(String.format("Member with id [%d] was not found.", id));
    }

    public MemberNotFoundException(String name) {

        super(String.format("Member containing [%s] in name was not found.", name));
    }
}
