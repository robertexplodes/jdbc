package presentation.controller;

import domain.Mitarbeiter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.SneakyThrows;
import persistence.JdbcMitarbeiterRepository;
import persistence.MitarbeiterRepository;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private TabPane tabpane;

    @FXML
    private TableView<Mitarbeiter> mitarbeiterTable;

    @FXML
    private TableColumn<Mitarbeiter, String> namenskuerzel;

    @FXML
    private TableColumn<Mitarbeiter, String> name;

    @FXML
    private TableColumn<Mitarbeiter, String> gehalt;


    private Connection connection;
    private MitarbeiterRepository mitarbeiterRepository;

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/tischlerunternehmen", "admin", "password");
        mitarbeiterRepository = new JdbcMitarbeiterRepository(connection);
        namenskuerzel.setCellValueFactory(new PropertyValueFactory<>("namenskuerzel"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        gehalt.setCellValueFactory(new PropertyValueFactory<>("gehalt"));

        setMitarbeiterTable(mitarbeiterRepository.findAll());
    }

    private void setMitarbeiterTable(List<Mitarbeiter> mitarbeiterList) {
        var olist = FXCollections.observableList(mitarbeiterList);
        mitarbeiterTable.setItems(olist);
    }

    @SneakyThrows
    public void closeDB() {
        connection.close();
    }
}
