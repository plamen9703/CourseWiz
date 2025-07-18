package org.example.application.repository;

import java.util.List;
import java.util.Optional;

public interface JdbcRepository<T> {

    List<T> findAll();

    Optional<T> findById(T entity);

    T insert(T entity);

    int update(T entity);

    int delete(T entity);

    boolean existsById(T entity);

}
