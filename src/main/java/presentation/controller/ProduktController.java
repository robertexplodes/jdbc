package presentation.controller;

import domain.Produkt;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.SneakyThrows;
import persistence.JdbcProduktRepository;
import persistence.ProduktRepository;
import utils.ConnectionManager;

import java.net.URL;
import java.sql.SQLException;
import java.util.Collection;
import java.util.ResourceBundle;

public class ProduktController implements Initializable, PersistableSearchController {

    @FXML
    private TableView<Produkt> produktTable;

    @FXML
    private TableColumn<Produkt, Integer> id;

    @FXML
    private TableColumn<Produkt, String> holzart;

    @FXML
    private TableColumn<Produkt, String> produktart;

    private ProduktRepository produktRepository;

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        var connection = ConnectionManager.getConnection();
        produktRepository = JdbcProduktRepository.getInstance(connection);

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        holzart.setCellValueFactory(p -> p.getValue().holzartProperty());
        produktart.setCellValueFactory(new PropertyValueFactory<>("produktart"));

        setProduktTable(produktRepository.findAll());

    }

    private void setProduktTable(Collection<Produkt> produkte) {
        var sorted = produkte.stream().sorted().toList();
        var observableList = FXCollections.observableList(sorted);
        produktTable.setItems(observableList);
        produktTable.refresh();
    }

    @Override
    public void searchForString(String value) {
        try {
            var allByHolzart = produktRepository.findAllByString(value);
            setProduktTable(allByHolzart);
        } catch (SQLException | IllegalArgumentException e) {
            // ignore
        }
    }

    @SneakyThrows
    @Override
    public void loadAll() {
        setProduktTable(produktRepository.findAll());
    }
}
