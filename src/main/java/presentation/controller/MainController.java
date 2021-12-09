package presentation.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import utils.ConnectionManager;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.util.*;


public class MainController implements Initializable {

    @FXML
    private TabPane tabpane;

    @FXML
    private MenuItem helpAbout;

    @FXML
    private Button add;

    @FXML
    private PersistableController mitarbeiterTabController;

    @FXML
    private ProduktController produktTabController;

    @FXML
    private KundeController kundenTabController;

    @FXML
    private PersistableController bestellungTabController;

    @FXML
    private TextField searchbar;

    private Connection connection;

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connection = ConnectionManager.getConnection();
        add.setOnAction(event -> {
            var text = tabpane.getSelectionModel().getSelectedItem().getText();
            if (text.equals("Produkte")) {
                produktTabController.openNewWindow();
            } else if ("Mitarbeiter".equals(text)) {
                mitarbeiterTabController.openNewWindow();
            } else if ("Kunden".equals(text)) {
                kundenTabController.openNewWindow();
            }
        });

        helpAbout.setOnAction(event -> {
            URI path = null;
            try {
                path = getClass().getResource("/about.mp4").toURI();
            } catch (URISyntaxException e) {
                return;
            }
            var media = new Media(path.toString());
            var player = new MediaPlayer(media);
            player.setAutoPlay(true);
            player.setVolume(1);
            var mediaView = new MediaView(player);
            var pane = new Pane();
            pane.getChildren().add(mediaView);
            var scene = new Scene(pane);
            var stage = new Stage();
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.show();
        });

        searchbar.textProperty().addListener(i -> {
            String value = searchbar.getText();

            mitarbeiterTabController.searchForString(value);
            produktTabController.searchForString(value);
            kundenTabController.searchForString(value);
        });
    }

    @SneakyThrows
    public void closeDB() {
        connection.close();
    }
}
