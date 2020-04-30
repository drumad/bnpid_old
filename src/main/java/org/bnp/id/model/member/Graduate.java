package org.bnp.id.model.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bnp.id.model.field.Address;
import org.bnp.id.model.retreat.ClassInfo;

import java.util.List;

/**
 * This is the base class for those who have graduated the Banal Na Pag-aaral.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Graduate extends Person {

    private Person personData;

    private String barcodeId;

    private Address chapter;

    private List<ClassInfo> classes;

    private Integer id;

    private Adorer sponsor;
}
