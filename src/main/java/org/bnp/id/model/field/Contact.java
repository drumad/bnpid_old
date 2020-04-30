package org.bnp.id.model.field;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Contact {

    private Integer countryCode;

    private Integer areaCode;

    private Integer prefix;

    private Integer lineNumber;

    private Integer extension;
}
