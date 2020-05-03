package org.bnp.id.model.field;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Address {

    private String street;

    private String city;

    private String state;

    private String country;

    private Integer zip;

    public boolean equals(@NonNull Address address) {

        return street.equals(address.getStreet()) && city.equals(address.getCity()) && state.equals(address.getState()) && country.equals(address.getCountry())
            && zip == address.getZip();
    }
}
