package presentation.controller;

import domain.Mitarbeiter;
import domain.Rolle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

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
        double newGehalt;
        try {
            newGehalt = Double.parseDouble(gehalt.getText());
            var m = new Mitarbeiter(namenskuerzel.getText(), name.getText(), rolle.getValue(), newGehalt);
            return Optional.of(m);
        } catch (NumberFormatException e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("ERROR");
            a.setHeaderText("Gehalt nicht korrekt!");
            a.showAndWait();
        }
        return Optional.empty();
    }

    public void setEntity(Mitarbeiter m) {
        rolle.getSelectionModel().select(m.getRolle());
        namenskuerzel.setText(m.getNamenskuerzel());
        namenskuerzel.setEditable(false);
        name.setText(m.getName());
        gehalt.setText(String.valueOf(m.getGehalt()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rolle.getItems().addAll(Rolle.values());
        save.setOnAction(e -> {

            var stage = (Stage) rolle.getScene().getWindow();
            stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        });
    }
}
