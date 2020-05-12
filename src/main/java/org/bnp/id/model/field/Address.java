package org.bnp.id.model.field;

import com.mysql.cj.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bnp.id.model.info.Country;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    private String street;

    private String city;

    private String state;

    private Integer zip;

    private Country country;

    @Override
    public boolean equals(Object o) {

        boolean isEqual = false;

        if (o != null && o instanceof Address) {
            Address addr = (Address) o;

            if (this.street != null && addr.getStreet() != null) {
                isEqual = this.street.equals(addr.getStreet());
            } else if (StringUtils.isNullOrEmpty(this.street) && StringUtils.isNullOrEmpty(addr.getStreet())) {
                isEqual = true;
            } else {
                isEqual = false;
            }

            if (isEqual && this.city != null && addr.getCity() != null) {
                isEqual = this.city.equals(addr.getCity());
            } else if (StringUtils.isNullOrEmpty(this.city) && StringUtils.isNullOrEmpty(addr.getCity())) {
                isEqual = true;
            } else {
                isEqual = false;
            }

            if (isEqual && this.state != null && addr.getState() != null) {
                isEqual = this.state.equals(addr.getState());
            } else if (StringUtils.isNullOrEmpty(this.state) && StringUtils.isNullOrEmpty(addr.getState())) {
                isEqual = true;
            } else {
                isEqual = false;
            }

            if (isEqual && this.zip != null && addr.getZip() != null) {
                isEqual = this.zip.equals(addr.getZip());
            } else if (this.zip == null && addr.getZip() == null) {
                isEqual = true;
            } else {
                isEqual = false;
            }

            if (isEqual && this.country != null && addr.getCountry() != null) {
                isEqual = this.country.equals(addr.getCountry());
            } else if (this.country == addr.getCountry()) {
                isEqual = true;
            } else {
                isEqual = false;
            }
        }

        return isEqual;
    }
}
