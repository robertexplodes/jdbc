package presentation.controller;

import domain.Produkt;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import lombok.SneakyThrows;
import persistence.JdbcProduktRepository;
import persistence.ProduktRepository;
import presentation.controller.update.UpdateController;
import utils.ConnectionManager;

import java.net.URL;
import java.sql.SQLException;
import java.util.Collection;
import java.util.ResourceBundle;

public class ProduktController implements Initializable, PersistableController {

    @FXML
    private TableView<Produkt> produktTable;

    @FXML
    private TableColumn<Produkt, Integer> id;

    @FXML
    private TableColumn<Produkt, String> holzart;

    @FXML
    private TableColumn<Produkt, String> produktart;

    private ProduktRepository produktRepository;

    private UpdateController<Produkt> editController;

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        var connection = ConnectionManager.getConnection();
        produktRepository = JdbcProduktRepository.getInstance(connection);

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        holzart.setCellValueFactory(p -> p.getValue().holzartProperty());
        produktart.setCellValueFactory(new PropertyValueFactory<>("produktart"));

        setProduktTable(produktRepository.findAll());

        produktTable.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() != 2) {
                return;
            }
            var produkt = produktTable.getSelectionModel().getSelectedItem();
            if (produkt == null) {
                return;
            }
            var newValue = editController.getValue();
            if (newValue.isEmpty()) {
                return;
            }
            try {
                produktRepository.update(newValue.get());
                setProduktTable(produktRepository.findAll());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        produktTable.setOnKeyPressed(event -> {
            if (event.getCode() != KeyCode.DELETE) {
                return;
            }
            var selectedItem = produktTable.getSelectionModel().getSelectedItem();
            if(selectedItem == null) {
                return;
            }
            try {
                produktRepository.delete(selectedItem);
                setProduktTable(produktRepository.findAll());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void setProduktTable(Collection<Produkt> produkte) {
        var sorted = produkte.stream().sorted().toList();
        var observableList = FXCollections.observableList(sorted);
        produktTable.setItems(observableList);
        produktTable.refresh();
    }

    @Override
    public void openNewWindow() {

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
}
