package org.bnp.id.service;

import org.bnp.id.model.field.Address;
import org.bnp.id.model.info.Parish;

import java.util.Collection;

public interface ParishService extends Service<Parish> {

    Collection<Parish> findByAddress(Address address);
}
