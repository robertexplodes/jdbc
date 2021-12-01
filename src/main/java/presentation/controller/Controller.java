package presentation.controller;

import domain.*;
import domain.interfaces.NotEditable;
import domain.interfaces.Persitable;
import domain.interfaces.Render;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import persistence.*;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Arrays;
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

        mitarbeiterTable.setOnMouseClicked(event -> handleEdit(event, mitarbeiterTable, Mitarbeiter.class));
        produkteTable.setOnMouseClicked(event -> handleEdit(event, produkteTable, Produkt.class));
        kundentable.setOnMouseClicked(event -> handleEdit(event, kundentable, Kunde.class));

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
        openEditWindow(clazz, selected);
    }

    private void setKundenTable(List<Kunde> all) {
        var produktList = FXCollections.observableArrayList(all);
        this.kundentable.setItems(produktList);
    }

    private void setProdukteTable(List<Produkt> produkte) {
        ObservableList<Produkt> produktList = FXCollections.observableArrayList(produkte);
        this.produkteTable.setItems(produktList);
    }

    private void setMitarbeiterTable(List<Mitarbeiter> mitarbeiterList) {
        var olist = FXCollections.observableList(mitarbeiterList);
        mitarbeiterTable.setItems(olist);
    }

    @SneakyThrows
    private void openEditWindow(Class<? extends Persitable> clazz, Persitable persitable) {
        var fields = Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Render.class))
                .toList();

        var pane = new GridPane();
        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.setHgap(10);
        pane.setVgap(30);

        for (int i = 0; i < fields.size(); i++) {
            var field = fields.get(i);
            var methodName = "get" + toFirstLetterUppercase(field.getName());

            var getMethod = clazz.getMethod(methodName);
            var returnValue = getMethod.invoke(persitable);

            Node type = getNodeForType(getMethod, returnValue);
            if (field.isAnnotationPresent(NotEditable.class)) {
                type = new Label(returnValue.toString());
            }
            var labelText = toFirstLetterUppercase(field.getName());
            pane.addRow(i, new Label(labelText), type);
        }
        var stage = new Stage();
        stage.setScene(new Scene(pane));
        stage.setTitle("Edit " + clazz.getSimpleName());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(this.tabpane.getScene().getWindow());
        stage.resizableProperty().setValue(false);
        stage.show();
    }

    private String toFirstLetterUppercase(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    private Node getNodeForType(Method getMethod, Object returnValue) {
        return switch (getMethod.getReturnType().getSimpleName()) {
            case "String", "Integer", "Double", "double" -> new TextField(returnValue.toString());
            case "Rolle", "Holzart" -> {
                var choiceBox = new ChoiceBox<>();
                if (returnValue instanceof Rolle r) {
                    choiceBox.getItems().addAll(Rolle.values());
                    choiceBox.getSelectionModel().select(r);
                } else if (returnValue instanceof Holzart h) {
                    choiceBox.getItems().addAll(Holzart.values());
                    choiceBox.getSelectionModel().select(h);
                }
                yield choiceBox;
            }
            default -> throw new IllegalArgumentException("Unsupported type" + getMethod.getReturnType().getSimpleName());
        };
    }

    @SneakyThrows
    public void closeDB() {
        connection.close();
    }


}
