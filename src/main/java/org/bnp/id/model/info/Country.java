package org.bnp.id.model.info;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Country {

    private Integer id;

    private String iso;

    private String iso3;

    private String name;

    private String niceName;

    private String numCode;

    private String phoneCode;
}
