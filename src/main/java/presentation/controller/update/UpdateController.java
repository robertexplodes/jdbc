package presentation.controller.update;


import java.util.Optional;

public interface UpdateController<T> {

    Optional<T> getValue();

    void setEntity(T entity);

}
