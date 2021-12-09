package presentation.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lombok.SneakyThrows;
import utils.ConnectionManager;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;


public class MainController implements Initializable {

    @FXML
    private TabPane tabpane;

    @FXML
    private MenuItem helpAbout;

    @FXML
    private MenuItem reload;

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


    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tabpane.getSelectionModel().selectedIndexProperty().addListener(i -> {
            var index = tabpane.getSelectionModel().getSelectedIndex();
            var tab = tabpane.getTabs().get(index);
            var text = tab.getText();

            switch (text) {
                case "Produkte", "Bestellungen" -> Platform.runLater(()->add.setVisible(false));
                case "Mitarbeiter", "Kunde" -> Platform.runLater(() -> add.setVisible(true));
                default -> throw new IllegalArgumentException();
            }
        });
        add.setOnAction(event -> {
            var text = tabpane.getSelectionModel().getSelectedItem().getText();
            switch (text) {
                case "Mitarbeiter" -> mitarbeiterTabController.openNewWindow();
                case "Kunde" -> kundenTabController.openNewWindow();
                default -> throw new IllegalArgumentException();
            }
        });

        reload.setOnAction(event -> {
            mitarbeiterTabController.loadAll();
            produktTabController.loadAll();
            kundenTabController.loadAll();
            bestellungTabController.loadAll();
        });

        helpAbout.setOnAction(event -> {
            URI path;
            try {
                path = Objects.requireNonNull(getClass().getResource("/about.mp4")).toURI();
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
            stage.setOnCloseRequest(e -> player.stop());
            scene.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.ESCAPE)
                    stage.fireEvent(new WindowEvent(scene.getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST));
            });
            stage.setFullScreen(true);
            stage.show();
        });

        searchbar.textProperty().addListener(i -> {
            String value = searchbar.getText();

            mitarbeiterTabController.searchForString(value);
            produktTabController.searchForString(value);
            kundenTabController.searchForString(value);
            bestellungTabController.searchForString(value);
        });
    }

    @SneakyThrows
    public void closeDB() {
        ConnectionManager.closeConnection();
    }
}
