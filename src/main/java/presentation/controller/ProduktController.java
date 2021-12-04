package presentation.controller;

import domain.Produkt;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import lombok.SneakyThrows;
import persistence.JdbcProduktRepository;
import persistence.ProduktRepository;
import utils.ConnectionManager;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ProduktController implements Initializable {

    @FXML
    private TableView<Produkt> produktTable;

    @FXML
    private TableColumn<Produkt, Integer> id;

    @FXML
    private TableColumn<Produkt, String> holzart;

    @FXML
    private TableColumn<Produkt, String> produktart;

    private ProduktRepository produktRepository;

    private EditController<Produkt> editController;


    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        var connection = ConnectionManager.getConnection();
        produktRepository = JdbcProduktRepository.getInstance(connection);
        editController = new EditControllerFX<>(produktRepository, produktTable);

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        holzart.setCellValueFactory(p -> p.getValue().holzartProperty());
        produktart.setCellValueFactory(new PropertyValueFactory<>("produktart"));

        setProduktTable(produktRepository.findAll());

        produktTable.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() != 2) {
                return;
            }
            var mitarbeiter = produktTable.getSelectionModel().getSelectedItem();
            if(mitarbeiter == null) {
                return;
            }
            editController.openEditWindow(mitarbeiter);
        });
    }

    private void setProduktTable(List<Produkt> produkte) {
        var observableList = FXCollections.observableList(produkte);
        produktTable.setItems(observableList);
    }
}
