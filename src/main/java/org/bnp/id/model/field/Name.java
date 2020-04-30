package org.bnp.id.model.field;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Name {

    private String salutation;

    private String firstName;

    private String middleName;

    private String lastName;

    private String suffix;
}
