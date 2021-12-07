package presentation.controller.update;


import domain.interfaces.Persitable;

import java.util.Optional;
import java.util.function.Consumer;

public interface UpdateController<T extends Persitable> {

    Optional<T> getValue();

    void setEntity(T entity);

    void setOnSave(Consumer<T> onSave);
}
