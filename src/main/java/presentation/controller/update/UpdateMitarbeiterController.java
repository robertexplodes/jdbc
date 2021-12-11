package presentation.controller.update;

import domain.Mitarbeiter;
import domain.Rolle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class UpdateMitarbeiterController implements UpdateController<Mitarbeiter>, Initializable {

    @FXML
    private TextField namenskuerzel;

    @FXML
    private TextField name;

    @FXML
    private TextField gehalt;

    @FXML
    private ChoiceBox<Rolle> rolle;

    @FXML
    private Button save;


    @Override
    public Optional<Mitarbeiter> getValue() {
        if (namenskuerzel.getText().isEmpty() || name.getText().isEmpty() || gehalt.getText().isEmpty()) {
            return Optional.empty();
        }
        try {
            double newGehalt = Double.parseDouble(gehalt.getText());
            var m = new Mitarbeiter(namenskuerzel.getText(), name.getText(), rolle.getValue(), newGehalt);
            return Optional.of(m);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public void setEntity(Mitarbeiter m) {
        rolle.getSelectionModel().select(m.getRolle());
        namenskuerzel.setText(m.getNamenskuerzel());
        namenskuerzel.setEditable(false);
        name.setText(m.getName());
        gehalt.setText(String.valueOf(m.getGehalt()));
    }

    @Override
    public void setOnSave(Consumer<Mitarbeiter> onSave) {
        save.setOnAction(event -> {
            var value = getValue();
            if (value.isPresent()) {
                onSave.accept(value.get());
                ((Stage) save.getScene().getWindow()).close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Could not save Object");
                alert.show();
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rolle.getItems().addAll(Rolle.values());
        save.setOnAction(event -> {

        });
    }
}
