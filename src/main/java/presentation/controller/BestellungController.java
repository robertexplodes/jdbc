package presentation.controller;

import domain.Bestellung;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import persistence.BestellungRepository;
import persistence.JdbcBestellungRepository;
import utils.ConnectionManager;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class BestellungController implements Initializable, PersistableSearchController {
    @FXML
    private TableView<Bestellung> bestellungTable;

    @FXML
    private TableColumn<Bestellung, Integer> id;

    @FXML
    private TableColumn<Bestellung, LocalDate> date;

    @FXML
    private TableColumn<Bestellung, String> kundenName;

    private BestellungRepository bestellungRepository;


    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        var connection = ConnectionManager.getConnection();
        bestellungRepository = JdbcBestellungRepository.getInstance(connection);

        id.setCellValueFactory(new PropertyValueFactory<>("bestellungId"));
        date.setCellValueFactory(new PropertyValueFactory<>("bestellDatum"));
        kundenName.setCellValueFactory(k -> {
            var name = k.getValue().getKunde().getName();
            return new SimpleObjectProperty<>(name);
        });
        setBestellungTable(bestellungRepository.findAll());
        bestellungTable.setOnMouseClicked(event -> {
            if (event.getButton() != MouseButton.PRIMARY || event.getClickCount() != 2) {
                return;
            }
            var bestellung = bestellungTable.getSelectionModel().getSelectedItem();
            if (bestellung == null) {
                return;
            }
            handleDetails(bestellung);
        });
    }

    private void handleDetails(Bestellung bestellung) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout/edit/bestellungEdit.fxml"));
            Parent root = loader.load();
            DetailController<Bestellung> controller = loader.getController();
            controller.setEntity(bestellung);
            var scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("ERROR");
            a.setHeaderText("Could not open new Window!");
            a.show();
        }
    }

    @SneakyThrows
    @Override
    public void loadAll() {
        setBestellungTable(bestellungRepository.findAll());
    }

    private void setBestellungTable(List<Bestellung> bestellungen) {
        var observableList = FXCollections.observableList(bestellungen);
        bestellungTable.getItems().clear();
        bestellungTable.setItems(observableList);
    }

    @SneakyThrows
    @Override
    public void searchForString(String value) {
        var byName = bestellungRepository.findAllByString(value);
        setBestellungTable(byName);
    }
}
