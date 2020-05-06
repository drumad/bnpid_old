package org.bnp.id.model.info;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bnp.id.model.field.Address;
import org.bnp.id.util.AddressUtil;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class Parish {

    private Integer id;

    private String name;

    private Address address;

    private Date dateUpdated;

    private Date dateCreated;

    @Override
    public String toString() {

        return String.format("%d | %d", name, AddressUtil.convert(address));
    }
}
