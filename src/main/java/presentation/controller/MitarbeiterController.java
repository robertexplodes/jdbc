package presentation.controller;

import domain.Mitarbeiter;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import lombok.SneakyThrows;
import persistence.JdbcMitarbeiterRepository;
import persistence.MitarbeiterRepository;
import presentation.controller.update.PersitableUpdateControllerManager;
import presentation.controller.update.UpdateController;
import presentation.controller.update.UpdateControllerManager;
import utils.ConnectionManager;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;

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

    private UpdateControllerManager<Mitarbeiter> updateControllerManager;

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
        updateControllerManager = new PersitableUpdateControllerManager<>();

        mitarbeiterTable.setOnMouseClicked(event -> {
            if (event.getButton() != MouseButton.PRIMARY || event.getClickCount() != 2) {
                return;
            }
            var mitarbeiter = mitarbeiterTable.getSelectionModel().getSelectedItem();
            if (mitarbeiter == null) {
                return;
            }
            handleEdit(mitarbeiter);
        });

        mitarbeiterTable.setOnKeyPressed(event -> {
            if (event.getCode() != KeyCode.DELETE) {
                return;
            }
            var selectedItem = mitarbeiterTable.getSelectionModel().getSelectedItem();
            if(selectedItem == null) {
                return;
            }
            try {
                mitarbeiterRepository.delete(selectedItem);
                setMitarbeiterTable(mitarbeiterRepository.findAll());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void handleEdit(Mitarbeiter mitarbeiter) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout/edit/mitarbeiterEdit.fxml"));
            Parent root = loader.load();
            UpdateController<Mitarbeiter> controller = loader.getController();
            controller.setEntity(mitarbeiter);
            Consumer<Mitarbeiter> updateMitarbeiter = m -> {
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

    private void update(Mitarbeiter v) {
        try {
            mitarbeiterRepository.update(v);
        } catch (SQLException ex) {
            // ignore
        }
    }

    private void save(Mitarbeiter v) {
        try {
            mitarbeiterRepository.save(v);
        } catch (SQLException ex) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("ERROR");
            a.setHeaderText("Could not save Mitarbeiter!");
            a.show();
        }
    }

    @SneakyThrows
    @Override
    public void openNewWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout/edit/mitarbeiterEdit.fxml"));
        Parent root = loader.load();
        UpdateController<Mitarbeiter> controller = loader.getController();

        Consumer<Mitarbeiter> c = (Mitarbeiter m) -> {
            save(m);
            loadAll();
        };
        updateControllerManager.executeNewStage(root, controller, c);
    }

    @SneakyThrows
    @Override
    public void searchForString(String value) {
        var byName = mitarbeiterRepository.findByName(value);
        setMitarbeiterTable(byName);
    }

    @SneakyThrows
    @Override
    public void loadAll() {
        setMitarbeiterTable(mitarbeiterRepository.findAll());
    }

    private void setMitarbeiterTable(Collection<Mitarbeiter> mitarbeiterList) {
        var sorted = mitarbeiterList.stream().sorted().toList();
        var observableList = FXCollections.observableList(sorted);
        mitarbeiterTable.setItems(observableList);
        mitarbeiterTable.refresh();
    }
}
