package org.example.application.services.interfaces;

import java.util.List;

public interface JdbcService<T> {

    List<T> findAll();

    T findById(T entity);

    T create(T entity);

    void update(T entity);

    void delete(T entity);

    boolean existsById(T entity);

}
