package presentation.controller;

import domain.*;
import domain.interfaces.Persitable;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import persistence.*;
import presentation.Utils;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.List;


public class Controller implements Initializable {

    @FXML
    private TabPane tabpane;


    @FXML
    private TableView<Produkt> produkteTable;

    @FXML
    private TableColumn<Produkt, Integer> produktId;

    @FXML
    private TableColumn<Produkt, String> holzart;

    @FXML
    private TableColumn<Produkt, String> produktart;

    @FXML
    private MenuItem helpAbout;

    @FXML
    private TableView<Kunde> kundenTable;

    @FXML
    private TableColumn<Kunde, Integer> kundenId;

    @FXML
    private TableColumn<Kunde, String> kundenName;

    @FXML
    private TableColumn<Kunde, String> kundenEmail;

    @FXML
    private Button add;

    @FXML
    private Pane mitarbeiterTab;

    @FXML
    private MitarbeiterController mitarbeiterTabController;

    private Connection connection;
    private ProduktRepository produktRepository;
    private KundenRepository kundenRepository;

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/tischlerunternehmen", "admin", "password");
        produktRepository = JdbcProduktRepository.getInstance(connection);
        kundenRepository = JdbcKundenRepository.getInstance(connection);


        produktId.setCellValueFactory(new PropertyValueFactory<>("id"));
        produktart.setCellValueFactory(new PropertyValueFactory<>("produktart"));
        holzart.setCellValueFactory(p -> p.getValue().holzartProperty());

        setProdukteTable(produktRepository.findAll());

        kundenEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        kundenName.setCellValueFactory(new PropertyValueFactory<>("name"));
        kundenId.setCellValueFactory(new PropertyValueFactory<>("id"));

        setKundenTable(kundenRepository.findAll());

        add.setOnAction(event -> {
            var text = tabpane.getSelectionModel().getSelectedItem().getText();
            if (text.equals("Produkte")) {
                System.out.println("produkte");
            } else {
                System.out.println("mitarbeiter");
            }
        });

//        mitarbeiterTable.setOnMouseClicked(event -> handleEdit(event, mitarbeiterTable, Mitarbeiter.class));
        produkteTable.setOnMouseClicked(event -> handleEdit(event, produkteTable, Produkt.class));
        kundenTable.setOnMouseClicked(event -> handleEdit(event, kundenTable, Kunde.class));

        helpAbout.setOnAction(event -> {
            var pane = new GridPane();
            pane.setPadding(new Insets(10));
            var hyperlink = new Hyperlink("More information here!");
            hyperlink.setOnAction(e -> {
                try {
                    Desktop.getDesktop().browse(new URL("https://www.youtube.com/watch?v=dQw4w9WgXcQ").toURI());
                } catch (IOException | URISyntaxException ex) {
                    ex.printStackTrace();
                }
            });
            pane.addRow(0, hyperlink);
            var stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("About");
            stage.setScene(new Scene(pane));
            stage.show();
        });
    }

    private void handleEdit(MouseEvent event, TableView<? extends Persitable> table, Class<? extends Persitable> clazz) {
        if (!event.getButton().equals(MouseButton.PRIMARY) || event.getClickCount() != 2) {
            return;
        }
        var selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }

        var utils = Utils.instance(connection);
        utils.openEditWindow(clazz, selected);
    }

    private void setKundenTable(List<Kunde> all) {
        var produktList = FXCollections.observableArrayList(all);
        Platform.runLater(() -> kundenTable.setItems(produktList));
    }

    private void setProdukteTable(List<Produkt> produkte) {
        ObservableList<Produkt> produktList = FXCollections.observableArrayList(produkte);
        Platform.runLater(() -> produkteTable.setItems(produktList));
    }


    @SneakyThrows
    public void closeDB() {
        connection.close();
        mitarbeiterTabController.closeDB();
    }
}
