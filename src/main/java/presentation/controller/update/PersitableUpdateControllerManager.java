package presentation.controller.update;

import domain.interfaces.Persitable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.SneakyThrows;

public class PersitableUpdateControllerManager<T extends Persitable> implements UpdateControllerManager<T> {

    @SneakyThrows
    @Override
    public UpdateController<T> getUpdateController(String resource) {
        var loader = new FXMLLoader(getClass().getResource(resource));
        loader.load();
        return loader.getController();
    }

    @SneakyThrows
    @Override
    public Parent getParentForResource(String resource) {
        var loader = new FXMLLoader(getClass().getResource(resource));
        return loader.load();
    }
}
