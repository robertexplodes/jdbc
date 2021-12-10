package presentation.controller.update;

import domain.interfaces.Persitable;
import javafx.scene.Parent;

import java.util.function.Consumer;

public interface UpdateControllerManager<T extends Persitable> {

    void executeNewStage(Parent root, UpdateController<T> controller, Consumer<T> consumer);
}
