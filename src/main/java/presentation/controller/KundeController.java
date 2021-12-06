package presentation.controller;

import domain.Kunde;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import lombok.SneakyThrows;
import persistence.JdbcKundenRepository;
import persistence.KundenRepository;
import presentation.controller.update.UpdateController;
import utils.ConnectionManager;

import java.net.URL;
import java.sql.SQLException;
import java.util.Collection;
import java.util.ResourceBundle;

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
    private UpdateController<Kunde> editController;


    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        var connection = ConnectionManager.getConnection();
        kundenRepository = JdbcKundenRepository.getInstance(connection);
//        editController = new UpdateControllerFX<>();
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));


        setKundeTable(kundenRepository.findAll());

        kundeTable.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() != 2) {
                return;
            }
            var kunde = kundeTable.getSelectionModel().getSelectedItem();
            if(kunde == null) {
                return;
            }
            var newValue = editController.getValue();
            if(newValue.isEmpty()) {
                return;
            }
            try {
                kundenRepository.update(newValue.get());
                setKundeTable(kundenRepository.findAll());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void setKundeTable(Collection<Kunde> kunden) {
        var sorted = kunden.stream().sorted().toList();
        var observableList = FXCollections.observableList(sorted);
        kundeTable.setItems(observableList);
        kundeTable.refresh();
    }

    @Override
    public void openNewWindow() {

    }

    @SneakyThrows
    @Override
    public void searchForString(String value) {
        var allByStringInNameOrEmail = kundenRepository.findAllByStringInNameOrEmail(value);
        setKundeTable(allByStringInNameOrEmail);
    }
}
