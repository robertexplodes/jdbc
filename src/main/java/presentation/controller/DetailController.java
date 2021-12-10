package presentation.controller;

import domain.interfaces.Persitable;

public interface DetailController<T extends Persitable> {
    void setEntity(T entity);
}
