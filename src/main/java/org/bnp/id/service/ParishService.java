package org.bnp.id.service;

import org.bnp.id.model.field.Address;
import org.bnp.id.model.info.Parish;

import java.sql.SQLException;
import java.util.Collection;

public interface ParishService extends Service<Parish> {

    Collection<Parish> findByAddress(Address address) throws SQLException;

    Collection<Parish> findByAddress(String addr) throws SQLException;
}
