package mintel;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;

/**
 * Represents a dialog box consisting of an ImageView to represent the speaker's face
 * and a label containing text from the speaker.
 */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;

    private DialogBox(String text, Image img) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Circle clip = new Circle();
        clip.setRadius(50);  // Adjust radius as needed
        clip.setCenterX(50); // Half of diameter
        clip.setCenterY(50); // Half of diameter

        displayPicture.setImage(img);
        displayPicture.setClip(clip);  // Apply circular clipping
        displayPicture.setFitWidth(100);  // Match clip diameter
        displayPicture.setFitHeight(100); // Match clip diameter
        displayPicture.setPreserveRatio(true);

        dialog.setText(text);
        displayPicture.setImage(img);
    }

    /**
     * Flips the dialog box such that the ImageView is on the left and text on the right.
     */
    private void flip() {
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
        dialog.getStyleClass().add("reply-label");
    }

    /**
     * Factory method to create a dialog box representing a user's message.
     * This creates a dialog box with the user's avatar on the right side
     * and the specified text message.
     *
     * @param text The message text to display in the dialog box
     * @param img The user's avatar image to display
     * @return A DialogBox instance configured for user messages
     */
    public static DialogBox getUserDialog(String text, Image img) {
        return new DialogBox(text, img);
    }

    /**
     * Factory method to create a dialog box representing Mintel's response.
     * This creates a dialog box with Mintel's avatar on the left side
     * and flips the layout to distinguish it from user messages.
     *
     * @param text Mintel's response text to display in the dialog box
     * @param img Mintel's avatar image to display
     * @return A DialogBox instance configured for Mintel responses with flipped layout
     */
    public static DialogBox getMintelDialog(String text, Image img) {
        var db = new DialogBox(text, img);
        db.flip();
        return db;
    }
}

