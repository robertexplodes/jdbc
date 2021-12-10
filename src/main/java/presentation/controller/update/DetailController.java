package presentation.controller.update;

import domain.interfaces.Persitable;

public interface DetailController<T extends Persitable> {
    void setEntity(T entity);
}
