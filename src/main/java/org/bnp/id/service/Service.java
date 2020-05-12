package org.bnp.id.service;

import java.sql.SQLException;
import java.util.Collection;

public interface Service<T> {

    T findById(Integer id);

    Collection<T> findByName(String name);

    Collection<T> findAll();

    int save(T t) throws SQLException;

    int update(T t) throws SQLException;

    int deleteById(Integer id) throws SQLException;

    void deleteAll();

    boolean isExists(T t);
}
