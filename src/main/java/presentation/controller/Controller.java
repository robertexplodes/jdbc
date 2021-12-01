package presentation.controller;

import domain.Kunde;
import domain.Mitarbeiter;
import domain.Persitable;
import domain.Produkt;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.SneakyThrows;
import persistence.*;

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

    @FXML
    private TableColumn<Mitarbeiter, String> mitarbeiterRolle;

    @FXML
    private TableView<Produkt> produkte;

    @FXML
    private TableColumn<Produkt, Integer> produktId;

    @FXML
    private TableColumn<Produkt, String> holzart;

    @FXML
    private TableColumn<Produkt, String> produktart;

    @FXML
    private TableView<Kunde> kundentable;

    @FXML
    private TableColumn<Kunde, Integer> kundenId;

    @FXML
    private TableColumn<Kunde, String> kundenName;

    @FXML
    private TableColumn<Kunde, String> kundenEmail;

    @FXML
    private Button add;

    private Connection connection;
    private MitarbeiterRepository mitarbeiterRepository;
    private ProduktRepository produktRepository;
    private KundenRepository kundenRepository;

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/tischlerunternehmen", "admin", "password");
        mitarbeiterRepository = new JdbcMitarbeiterRepository(connection);
        produktRepository = new JdbcProduktRepository(connection);
        kundenRepository = new JdbcKundenRepository(connection);

        namenskuerzel.setCellValueFactory(new PropertyValueFactory<>("namenskuerzel"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        gehalt.setCellValueFactory(new PropertyValueFactory<>("gehalt"));
        mitarbeiterRolle.setCellValueFactory(r -> r.getValue().rollenProperty());

        setMitarbeiterTable(mitarbeiterRepository.findAll());

        produktId.setCellValueFactory(new PropertyValueFactory<>("id"));
        produktart.setCellValueFactory(new PropertyValueFactory<>("produktart"));
        holzart.setCellValueFactory(p -> p.getValue().getHolzartProperty());

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
    }

    private void setKundenTable(List<Kunde> all) {
        var produktList = FXCollections.observableArrayList(all);

        this.kundentable.setItems(produktList);
    }


    private void setProdukteTable(List<Produkt> produkte) {
        ObservableList<Produkt> produktList = FXCollections.observableArrayList(produkte);
        this.produkte.setItems(produktList);
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
