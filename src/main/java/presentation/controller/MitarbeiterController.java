package presentation.controller;

import domain.Holzart;
import domain.Mitarbeiter;
import domain.Rolle;
import domain.interfaces.NotEditable;
import domain.interfaces.Persitable;
import domain.interfaces.Render;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import persistence.JdbcMitarbeiterRepository;
import persistence.MitarbeiterRepository;
import presentation.Utils;

import java.lang.reflect.Method;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
    private Utils utils;

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/tischlerunternehmen", "admin", "password");
        mitarbeiterRepository = JdbcMitarbeiterRepository.getInstance(connection);
        utils = Utils.instance(connection);


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
            utils.openEditWindow(Mitarbeiter.class, mitarbeiter);
        });
    }

    private void setMitarbeiterTable(List<Mitarbeiter> mitarbeiterList) {
        var observableList = FXCollections.observableList(mitarbeiterList);
        mitarbeiterTable.setItems(observableList);
    }

    public void closeDB() throws SQLException {
        connection.close();
    }
}
