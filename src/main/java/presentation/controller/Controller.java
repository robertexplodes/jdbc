package presentation.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import utils.ConnectionManager;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.util.*;


public class Controller implements Initializable {

    @FXML
    private TabPane tabpane;

    @FXML
    private MenuItem helpAbout;

    @FXML
    private Button add;

    @FXML
    private Pane mitarbeiterTab;

    @FXML
    private MitarbeiterController mitarbeiterTabController;

    @FXML
    private Pane produkteTab;

    @FXML
    private ProduktController produktTabController;

    @FXML
    private Pane kundenTab;

    @FXML
    private KundeController kundeTabController;

    private Connection connection;

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connection = ConnectionManager.getConnection();

        add.setOnAction(event -> {
            var text = tabpane.getSelectionModel().getSelectedItem().getText();
            if (text.equals("Produkte")) {
                System.out.println("produkte");
            } else {
                System.out.println("mitarbeiter");
            }
        });

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


    @SneakyThrows
    public void closeDB() {
        connection.close();
    }
}
