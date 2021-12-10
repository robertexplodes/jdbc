package presentation.controller.update;

import domain.Bestellung;
import domain.Produkt;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.SneakyThrows;
import persistence.JdbcBestellungRepository;
import utils.ConnectionManager;

import java.net.URL;
import java.util.ResourceBundle;

public class BestellungUpdateController implements Initializable, DetailController<Bestellung> {

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


    @SneakyThrows
    @Override
    public void setEntity(Bestellung entity) {
        id.setText(entity.getBestellungId().toString());
        datum.setText(entity.getBestellDatum().toString());
        kundenName.setText(entity.getKunde().getName());

        var connection = ConnectionManager.getConnection();
        var bestellungRepository = JdbcBestellungRepository.getInstance(connection);

        var produkteInBestellung = bestellungRepository.findAllProdukteInBestellung(entity);

        anzahl.setCellValueFactory(p ->  new SimpleObjectProperty<>(produkteInBestellung.get(p.getValue())));

        produkteInBestellung.forEach((key, value) -> produkte.getItems().add(key));
    }
}
