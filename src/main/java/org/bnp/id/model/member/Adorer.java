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
public class Adorer extends Graduate {

    private Graduate graduateData;

    private Date dateAdorer;
}
