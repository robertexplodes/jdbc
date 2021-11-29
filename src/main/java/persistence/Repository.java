package persistence;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Repository<P, T> {

    List<T> findAll() throws SQLException;

    void save(T entity) throws SQLException;

    void delete(T entity) throws SQLException;

    void update(T entity) throws SQLException;

    Optional<T> findById(P primaryKey) throws SQLException;
}
