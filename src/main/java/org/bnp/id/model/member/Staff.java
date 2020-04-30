package org.bnp.id.model.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Staff extends Adorer {

    private Adorer adorerData;

    private Date hirangStaff;
}
