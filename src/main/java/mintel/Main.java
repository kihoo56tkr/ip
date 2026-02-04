package mintel;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Mintel using FXML.
 */
public class Main extends Application {

    private Mintel mintel = new Mintel("./data/list_of_task.txt");

    /**
     * The main entry point for all JavaFX applications.
     * This method is called after the JavaFX runtime has been initialized.
     *
     * @param stage The primary stage for this application, onto which
     *              the application scene can be set. The primary stage
     *              is provided by the JavaFX platform.
     * @throws IOException If the FXML file cannot be loaded
     * @see FXMLLoader
     * @see Scene
     */    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setMinHeight(220);
            stage.setMinWidth(417);
            fxmlLoader.<MainWindow>getController().setMintel(mintel);  // inject the Duke instance
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


