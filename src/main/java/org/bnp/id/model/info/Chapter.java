package org.bnp.id.model.info;

import lombok.Getter;
import lombok.Setter;
import org.bnp.id.model.field.Address;

import java.util.Date;

@Getter
@Setter
public class Chapter {

    private Integer id;

    private String name;

    private Address location;

    private Date start;

    private Date dateCreated;

    private Date dateModified;
}
