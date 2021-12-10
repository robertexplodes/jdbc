package presentation.controller.update;


import domain.interfaces.Persitable;

import java.util.Optional;
import java.util.function.Consumer;

public interface UpdateController<T extends Persitable> extends DetailController<T> {

    Optional<T> getValue();


    void setOnSave(Consumer<T> onSave);
}
