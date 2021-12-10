package presentation.controller.update;

import domain.interfaces.Persitable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class PersitableUpdateControllerManager<T extends Persitable> implements UpdateControllerManager<T> {

    @Override
    public void executeNewStage(Parent root, UpdateController<T> controller, Consumer<T> executeOnSave) {
        var scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        controller.setOnSave(executeOnSave);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }
}
