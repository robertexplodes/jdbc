package presentation.controller.update;

import domain.Bestellung;
import domain.Mitarbeiter;
import domain.Produkt;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.SneakyThrows;
import persistence.BestellungRepository;
import persistence.JdbcBestellungRepository;
import presentation.controller.DetailController;
import utils.ConnectionManager;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class BestellungUpdateController implements Initializable, DetailController<Bestellung> {

    @FXML
    private Button save;

    @FXML
    private Label id;

    @FXML
    private Label datum;

    @FXML
    private Label kundenName;

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
        holzart.setCellValueFactory(p -> p.getValue().holzartProperty());
        produktart.setCellValueFactory(new PropertyValueFactory<>("produktart"));
    }

    private BestellungRepository bestellungRepository;

    @SneakyThrows
    @Override
    public void setEntity(Bestellung entity) {
        id.setText(entity.getBestellungId().toString());
        datum.setText(entity.getBestellDatum().toString());
        kundenName.setText(entity.getKunde().getName());

        var connection = ConnectionManager.getConnection();
        bestellungRepository = JdbcBestellungRepository.getInstance(connection);

        var produkteInBestellung = bestellungRepository.findAllProdukteInBestellung(entity);

        anzahl.setCellValueFactory(p ->  new SimpleObjectProperty<>(produkteInBestellung.get(p.getValue())));

        produkteInBestellung.forEach((key, value) -> produkte.getItems().add(key));

    }
}
