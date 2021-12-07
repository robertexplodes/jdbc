package presentation.controller;

import domain.Bestellung;
import domain.Mitarbeiter;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import lombok.SneakyThrows;
import persistence.BestellungRepository;
import persistence.JdbcBestellungRepository;
import presentation.controller.update.BestellungUpdateController;
import presentation.controller.update.PersitableUpdateControllerManager;
import presentation.controller.update.UpdateController;
import presentation.controller.update.UpdateControllerManager;
import utils.ConnectionManager;

import java.io.IOException;
import java.net.URL;

import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class BestellungController implements Initializable, PersistableController {
    @FXML
    private TableView<Bestellung> bestellungTable;

    @FXML
    private TableColumn<Bestellung, Integer> id;

    @FXML
    private TableColumn<Bestellung, LocalDate> date;

    @FXML
    private TableColumn<Bestellung, String> kundenName;

    @FXML
    private TableColumn<Bestellung, String> mitarbeiter;

    private BestellungRepository bestellungRepository;

    private UpdateControllerManager<Bestellung> updateControllerManager;

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        var connection = ConnectionManager.getConnection();
        bestellungRepository = JdbcBestellungRepository.getInstance(connection);

        updateControllerManager = new PersitableUpdateControllerManager<>();

        id.setCellValueFactory(new PropertyValueFactory<>("bestellungId"));
        date.setCellValueFactory(new PropertyValueFactory<>("bestellDatum"));
        kundenName.setCellValueFactory(k -> {
            var name = k.getValue().getKunde().getName();
            return new SimpleObjectProperty<>(name);
        });
        mitarbeiter.setCellValueFactory(k -> {
            var mitarbeiter = k.getValue().getMitarbeiter();
            if (mitarbeiter == null) {
                return new SimpleObjectProperty<>("");
            }
            var namenskuerzel = mitarbeiter.getNamenskuerzel();
            return new SimpleObjectProperty<>(namenskuerzel);
        });
        setBestellungTable(bestellungRepository.findAll());
        bestellungTable.setOnMouseClicked(event -> {
            if (event.getButton() != MouseButton.PRIMARY || event.getClickCount() != 2) {
                return;
            }
            var bestellung = bestellungTable.getSelectionModel().getSelectedItem();
            if (mitarbeiter == null) {
                return;
            }
            handleEdit(bestellung);
        });
    }

    private void handleEdit(Bestellung bestellung) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout/edit/bestellungEdit.fxml"));
            Parent root = loader.load();
            UpdateController<Bestellung> controller = loader.getController();
            controller.setEntity(bestellung);
            Consumer<Bestellung> updateMitarbeiter = m -> {
                update(m);
                loadAll();
            };
            updateControllerManager.executeNewStage(root, controller, updateMitarbeiter);
        } catch (IOException e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("ERROR");
            a.setHeaderText("Could not open new Window!");
            a.show();
        }
    }

    @SneakyThrows
    private void update(Bestellung bestellung) {
        bestellungRepository.update(bestellung);
    }

    @SneakyThrows
    private void loadAll() {
        setBestellungTable(bestellungRepository.findAll());
    }

    private void setBestellungTable(List<Bestellung> bestellungen) {
        var observableList = FXCollections.observableList(bestellungen);
        bestellungTable.setItems(observableList);
    }

    @Override
    public void openNewWindow() {

    }

    @Override
    public void searchForString(String value) {

    }
}
