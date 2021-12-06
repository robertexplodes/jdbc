package presentation.controller.update;

import domain.interfaces.Persitable;
import javafx.scene.Parent;

public interface UpdateControllerManager<T extends Persitable> {

    UpdateController<T> getUpdateController(String resource);

    Parent getParentForResource(String resource);
}
