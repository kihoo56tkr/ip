package mintel;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Mintel mintel;

    private final Image userImage = new Image(this.getClass().getResourceAsStream("/images/User.png"));
    private final Image mintelImage = new Image(this.getClass().getResourceAsStream("/images/Mintel.png"));

    /**
     * Initializes the JavaFX controller.
     * Verifies FXML injection, loads images, binds scroll pane, and shows welcome message.
     */
    @FXML
    public void initialize() {
        assert scrollPane != null : "ScrollPane FXML injection failed";
        assert dialogContainer != null : "DialogContainer FXML injection failed";
        assert userInput != null : "UserInput FXML injection failed";
        assert sendButton != null : "SendButton FXML injection failed";

        assert userImage != null : "User image failed to load";
        assert mintelImage != null : "Mintel image failed to load";

        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        dialogContainer.getChildren().addAll(
                DialogBox.getMintelDialog("MEOW! I'm Mintel!\n" + "What can I do for you? Enter '/help' for the list of commands!", mintelImage)
        );
    }

    /**
     * Shows a message from Mintel in the dialog container.
     * This is used for warnings, errors, and system messages.
     *
     * @param message The message to display
     */
    public void showMessage(String message) {
        dialogContainer.getChildren().add(
                DialogBox.getMintelDialog(message, mintelImage)
        );
    }

    /**
     * Injects the Mintel instance
     */
    public void setMintel(Mintel m) {
        assert m != null : "Cannot inject null Mintel instance";
        mintel = m;
        mintel.setMainWindow(this); // Connect back to GUI

        mintel.initializeTasks();
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Mintel's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        assert mintel != null : "Mintel instance must be set before handling input";
        assert userInput != null : "User input field must be available";
        assert dialogContainer != null : "Dialog container must be available";

        String input = userInput.getText();
        assert input != null : "TextField.getText() should not return null";

        String response = mintel.getResponse(input);
        assert response != null : "Mintel.getResponse() should not return null";

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getMintelDialog(response, mintelImage)
        );

        assert userImage != null : "User image must be loaded";
        assert mintelImage != null : "Mintel image must be loaded";

        userInput.clear();

        if (input.trim().equalsIgnoreCase("bye")) {
            assert userInput.getScene() != null : "User input should be in a scene";
            assert userInput.getScene().getWindow() != null : "Scene should have a window";

            userInput.setDisable(true);
            sendButton.setDisable(true);
            PauseTransition delay = new PauseTransition(Duration.seconds(3));
            delay.setOnFinished(event -> {
                Stage stage = (Stage) userInput.getScene().getWindow();
                assert stage != null : "Stage should not be null when closing";
                stage.close();
            });
            delay.play();
        }
    }
}
