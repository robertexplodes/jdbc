package presentation.controller;


import java.util.Optional;

public interface UpdateController<T> {

    /**
     * Opens a window to update an object
     * @param entity the object to update
     * @return the updated object
     */
    Optional<T> getValue();

    void setEntity(T entity);
}
