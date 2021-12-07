package presentation.controller.update;

import domain.Bestellung;
import domain.Mitarbeiter;
import domain.Produkt;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class BestellungUpdateController implements Initializable, UpdateController<Bestellung> {

    @FXML
    private Button save;

    @FXML
    private Label id;

    @FXML
    private Label datum;

    @FXML
    private Label kundenName;

    @FXML
    private ChoiceBox<Mitarbeiter> mitarbeiter;

    @FXML
    private TableView<Produkt> produkte;

    @FXML
    private TableColumn<Produkt, Integer> anzahl;

    @FXML
    private TableColumn<Produkt, String> holzart;

    @FXML
    private TableColumn<Produkt, String> produktart;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        anzahl.setCellValueFactory(p -> {

            return new SimpleObjectProperty<>();
        });
    }

    @Override
    public Optional<Bestellung> getValue() {
        return Optional.empty();
    }

    @Override
    public void setEntity(Bestellung entity) {
        id.setText(entity.getBestellungId().toString());
        datum.setText(entity.getBestellDatum().toString());
        kundenName.setText(entity.getKunde().getName());

    }

    @Override
    public void setOnSave(Consumer<Bestellung> onSave) {

    }
}
