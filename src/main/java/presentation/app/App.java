package presentation.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import presentation.controller.MainController;

public class App extends Application {
    private FXMLLoader loader;
    @Override
    public void start(Stage stage) throws Exception {
        loader = new FXMLLoader(getClass().getResource("/layout/app.fxml"));

        Parent root = loader.load();

        stage.setTitle("Tischlerunternehmen");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        MainController controller = loader.getController();
        controller.closeDB();
    }
}
