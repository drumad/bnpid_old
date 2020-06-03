package org.bnp.id.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public interface Service<T> {

    T findById(Integer id) throws SQLException;

    Collection<T> findByName(String name) throws SQLException;

    Collection<T> findAll() throws SQLException;

    int save(T t) throws SQLException;

    int update(T t) throws SQLException;

    int deleteById(Integer id) throws SQLException;

    int count() throws SQLException;

    void deleteAll();

    boolean isExists(T t) throws SQLException;

    T getObject(ResultSet result) throws SQLException;

    default void appendAnd(StringBuffer sql, int originalLength) {

        if (sql.length() > originalLength) {
            sql.append(" AND ");
        }
    }
}
