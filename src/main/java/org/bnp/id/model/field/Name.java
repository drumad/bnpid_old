package org.bnp.id.model.field;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Name {

    private String firstName;

    private String middleName;

    private String lastName;

    private String suffix;

    private String shortName;
}
