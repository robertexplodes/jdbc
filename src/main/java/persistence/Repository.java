package persistence;

import domain.interfaces.Persitable;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Repository<P, T extends Persitable> {

    List<T> findAll() throws SQLException;

    /**
     * Saves the given entity to the database.
     * @param entity
     * @return Entity with the new primary key
     * @throws SQLException
     */
    T save(T entity) throws SQLException;

    void delete(T entity) throws SQLException;

    void update(T entity) throws SQLException;

    Optional<T> findById(P primaryKey) throws SQLException;
}
