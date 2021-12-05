package presentation.controller;

import domain.Mitarbeiter;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import persistence.JdbcMitarbeiterRepository;
import persistence.MitarbeiterRepository;
import utils.ConnectionManager;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class MitarbeiterController implements Initializable, PersistableController {

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

    private MitarbeiterRepository mitarbeiterRepository;


    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        var connection = ConnectionManager.getConnection();
        mitarbeiterRepository = JdbcMitarbeiterRepository.getInstance(connection);

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
            if (mitarbeiter == null) {
                return;
            }
            handleEdit(mitarbeiter);
        });
    }

    private void handleEdit(Mitarbeiter mitarbeiter) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout/edit/mitarbeiterEdit.fxml"));
            Parent root = loader.load();
            UpdateController<Mitarbeiter> controller = loader.getController();
            controller.setEntity(mitarbeiter);
            var scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setOnCloseRequest(e -> {
                var value = controller.getValue();
                value.ifPresent(v -> {
                    update(v);
                    loadAll();
                });
            });
            stage.show();
        } catch (IOException e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("ERROR");
            a.setHeaderText("Could not open new Window!");
            a.show();
        }
    }

    private void update(Mitarbeiter v) {
        try {
            mitarbeiterRepository.update(v);
        } catch (SQLException ex) {
            // ignore
        }
    }

    @SneakyThrows
    @Override
    public void openNewWindow() {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mitarbeiterEdit.fxml"));
//        Parent root = loader.load();
//        UpdateMitarbeiterController controller = loader.getController();
//        controller.openUpdateWindow()
    }

    @SneakyThrows
    @Override
    public void searchForString(String value) {
        var byName = mitarbeiterRepository.findByName(value);
        setMitarbeiterTable(byName);
    }

    @SneakyThrows
    private void loadAll() {
        setMitarbeiterTable(mitarbeiterRepository.findAll());
    }

    private void setMitarbeiterTable(Collection<Mitarbeiter> mitarbeiterList) {
        var sorted = mitarbeiterList.stream().sorted().toList();
        var observableList = FXCollections.observableList(sorted);
        mitarbeiterTable.setItems(observableList);
        mitarbeiterTable.refresh();
    }
}
