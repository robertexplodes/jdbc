package presentation.controller;

import domain.Mitarbeiter;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import lombok.SneakyThrows;
import persistence.JdbcMitarbeiterRepository;
import persistence.MitarbeiterRepository;
import utils.ConnectionManager;

import java.net.URL;
import java.sql.Connection;
import java.util.*;

public class MitarbeiterController implements Initializable {

    @FXML
    private TableView<Mitarbeiter> mitarbeiterTable;

    @FXML
    private TableColumn<Mitarbeiter, String> namenskuerzel;

    @FXML
    private TableColumn<Mitarbeiter, String> name;

    @FXML
    private TableColumn<Mitarbeiter, String> gehalt;

    @FXML
    private TableColumn<Mitarbeiter, String> rolle;

    private Connection connection;
    private MitarbeiterRepository mitarbeiterRepository;
    private EditController<Mitarbeiter> editController;


    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connection = ConnectionManager.getConnection();
        mitarbeiterRepository = JdbcMitarbeiterRepository.getInstance(connection);
        editController = new EditControllerFX<>(mitarbeiterRepository, mitarbeiterTable);

        namenskuerzel.setCellValueFactory(new PropertyValueFactory<>("namenskuerzel"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        gehalt.setCellValueFactory(new PropertyValueFactory<>("gehalt"));
        rolle.setCellValueFactory(r -> r.getValue().rollenProperty());

        setMitarbeiterTable(mitarbeiterRepository.findAll());

        mitarbeiterTable.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() != 2) {
                return;
            }
            var mitarbeiter = mitarbeiterTable.getSelectionModel().getSelectedItem();
            if(mitarbeiter == null) {
                return;
            }
            editController.openEditWindow(mitarbeiter);
        });
    }

    private void setMitarbeiterTable(List<Mitarbeiter> mitarbeiterList) {
        var observableList = FXCollections.observableList(mitarbeiterList);
        mitarbeiterTable.setItems(observableList);
    }
}
