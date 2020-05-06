package org.bnp.id.service;

import java.util.Collection;

public interface Service<T> {

    T findbyId(Integer id);

    Collection<T> findbyName(String name);

    Collection<T> findAll();

    int save(T t);

    void update(T t);

    void deleteById(Integer id);

    void deleteAll();

    boolean isExists(T t);
}
