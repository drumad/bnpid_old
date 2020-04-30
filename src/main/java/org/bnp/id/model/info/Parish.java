package org.bnp.id.model.info;

import lombok.Getter;
import lombok.Setter;
import org.bnp.id.model.field.Address;

import java.util.Date;

@Getter
@Setter
public class Parish {

    private Address address;

    private Date dateModified;

    private Integer id;

    private String name;

    public Parish(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    public String toString() {
        return this.getName();
    }
}
