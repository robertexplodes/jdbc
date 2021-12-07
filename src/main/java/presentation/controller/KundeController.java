package presentation.controller;

import domain.Kunde;
import domain.Mitarbeiter;
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
import persistence.JdbcKundenRepository;
import persistence.KundenRepository;
import presentation.controller.update.PersitableUpdateControllerManager;
import presentation.controller.update.UpdateController;
import presentation.controller.update.UpdateControllerManager;
import utils.ConnectionManager;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class KundeController implements Initializable, PersistableController {

    @FXML
    private TableView<Kunde> kundeTable;

    @FXML
    private TableColumn<Kunde, Integer> id;

    @FXML
    private TableColumn<Kunde, String> name;

    @FXML
    private TableColumn<Kunde, String> email;

    private KundenRepository kundenRepository;

    private UpdateControllerManager<Kunde> updateControllerManager;

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        var connection = ConnectionManager.getConnection();
        kundenRepository = JdbcKundenRepository.getInstance(connection);
//        editController = new UpdateControllerFX<>();
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));

        updateControllerManager = new PersitableUpdateControllerManager<>();

        setKundeTable(kundenRepository.findAll());

        kundeTable.setOnMouseClicked(event -> {
            if (event.getButton() != MouseButton.PRIMARY || event.getClickCount() != 2) {
                return;
            }
            var kunde = kundeTable.getSelectionModel().getSelectedItem();
            if(kunde == null) {
                return;
            }
            handleEdit(kunde);
        });
    }

    private void handleEdit(Kunde kunde) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout/edit/kundeEdit.fxml"));
            Parent root = loader.load();
            UpdateController<Kunde> controller = loader.getController();
            controller.setEntity(kunde);
            Consumer<Kunde> updateMitarbeiter = m -> {
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
    private void update(Kunde k) {
        kundenRepository.update(k);
    }

    private void setKundeTable(Collection<Kunde> kunden) {
        var sorted = kunden.stream().sorted().toList();
        var observableList = FXCollections.observableList(sorted);
        kundeTable.setItems(observableList);
        kundeTable.refresh();
    }

    @SneakyThrows
    @Override
    public void openNewWindow() {
        var loader = new FXMLLoader(getClass().getResource("/layout/edit/kundeEdit.fxml"));
        Parent root = loader.load();
        UpdateController<Kunde> editController = loader.getController();
        Consumer<Kunde> save = (k) -> {
            save(k);
            loadAll();
        };
        updateControllerManager.executeNewStage(root, editController, save);
    }

    @SneakyThrows
    public void loadAll() {
        setKundeTable(kundenRepository.findAll());
    }

    @SneakyThrows
    public void save(Kunde kunde) {
        kundenRepository.save(kunde);
    }

    @SneakyThrows
    @Override
    public void searchForString(String value) {
        var allByStringInNameOrEmail = kundenRepository.findAllByStringInNameOrEmail(value);
        setKundeTable(allByStringInNameOrEmail);
    }
}
