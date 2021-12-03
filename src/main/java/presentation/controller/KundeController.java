package presentation.controller;

import domain.Kunde;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.SneakyThrows;
import persistence.JdbcKundenRepository;
import persistence.KundenRepository;
import utils.ConnectionManager;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class KundeController implements Initializable {

    @FXML
    private TableView<Kunde> kundeTable;

    @FXML
    private TableColumn<Kunde, Integer> id;

    @FXML
    private TableColumn<Kunde, String> name;

    @FXML
    private TableColumn<Kunde, String> email;

    private KundenRepository kundenRepository;

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        var connection = ConnectionManager.getConnection();
        kundenRepository = JdbcKundenRepository.getInstance(connection);

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));


        setKundeTable(kundenRepository.findAll());
    }

    private void setKundeTable(List<Kunde> kunden) {
        var observableList = FXCollections.observableList(kunden);
        kundeTable.setItems(observableList);
    }
}
