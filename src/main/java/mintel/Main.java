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

    private final Mintel mintel = new Mintel("./data/list_of_task.txt");

    {
        assert mintel != null : "Mintel instance failed to initialize";
        assert !mintel.isExit() : "Mintel should not start in exit state";
    }

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
     */
    @Override
    public void start(Stage stage) {
        assert stage != null : "JavaFX stage cannot be null";
        assert mintel != null : "Mintel must be initialized before starting GUI";

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            assert fxmlLoader != null : "FXMLLoader creation failed";

            AnchorPane ap = fxmlLoader.load();
            assert ap != null : "FXML loading failed - AnchorPane is null";

            Scene scene = new Scene(ap);
            assert scene != null : "Scene creation failed";

            stage.setScene(scene);
            stage.setMinHeight(220);
            stage.setMinWidth(417);
            MainWindow controller = fxmlLoader.getController();
            assert controller != null : "FXML controller not found";

            controller.setMintel(mintel);  // inject the Mintel instance

            stage.show();

            assert stage.isShowing() : "Stage failed to show";
            assert stage.getScene() != null : "Stage has no scene";

        } catch (IOException e) {
            e.printStackTrace();
            assert false : "FXML loading failed with IOException: " + e.getMessage();
        }
    }
}
