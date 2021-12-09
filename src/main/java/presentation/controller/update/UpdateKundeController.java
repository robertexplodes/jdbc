package presentation.controller.update;

import domain.Kunde;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class UpdateKundeController implements Initializable, UpdateController<Kunde> {

    @FXML
    private TextField id;

    @FXML
    private TextField name;

    @FXML
    private TextField email;

    @FXML
    private Button save;

    @Override
    public Optional<Kunde> getValue() {
        if (name.getText().isEmpty() || email.getText().isEmpty()) {
            return Optional.empty();
        }
        try {
            if(id.getText().isEmpty())
                return Optional.of(new Kunde(name.getText(), email.getText()));
            return Optional.of(new Kunde(Integer.parseInt(id.getText()), name.getText(), email.getText()));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    @Override
    public void setEntity(Kunde entity) {
        id.setText(String.valueOf(entity.getId()));
        name.setText(entity.getName());
        email.setText(entity.getEmail());
    }

    @Override
    public void setOnSave(Consumer<Kunde> onSave) {
        save.setOnAction(event -> {
            var value = getValue();
            if (value.isPresent()) {
                onSave.accept(value.get());
                ((Stage) save.getScene().getWindow()).close();
            } else {
                System.err.println("alert");
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        id.setEditable(false);
        save.setOnAction(event -> {
            if (getValue().isPresent()) {

            }
        });
    }
}
