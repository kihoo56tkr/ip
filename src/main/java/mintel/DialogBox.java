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
    private static final int DIAMETER = 100;

    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;

    private DialogBox(String text, Image img) {
        assert text != null : "Dialog text cannot be null";
        assert img != null : "Dialog image cannot be null";
        assert !text.trim().isEmpty() : "Dialog text cannot be empty or whitespace";
        assert img.getWidth() > 0 : "Image must have positive width";
        assert img.getHeight() > 0 : "Image must have positive height";

        try {
            String fxmlPath = "/view/DialogBox.fxml";
            assert MainWindow.class.getResource(fxmlPath) != null
                    : "DialogBox FXML not found: " + fxmlPath;

            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource(fxmlPath));
            assert fxmlLoader != null : "FXMLLoader creation failed";

            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();

            assert dialog != null : "FXML: 'dialog' Label not injected";
            assert displayPicture != null : "FXML: 'displayPicture' ImageView not injected";
        } catch (IOException e) {
            e.printStackTrace();
            assert false : "Failed to load DialogBox FXML: " + e.getMessage();
        }

        Circle clip = new Circle();
        clip.setRadius(DIAMETER / 2);
        clip.setCenterX(DIAMETER / 2);
        clip.setCenterY(DIAMETER / 2);

        displayPicture.setImage(img);
        displayPicture.setClip(clip);
        displayPicture.setFitWidth(DIAMETER);
        displayPicture.setFitHeight(DIAMETER);
        displayPicture.setPreserveRatio(true);

        assert displayPicture.getImage() == img : "Image not set on displayPicture";
        assert displayPicture.getClip() == clip : "Clip not applied to displayPicture";
        assert displayPicture.getFitWidth() == 100 : "Fit width not set correctly";
        assert displayPicture.getFitHeight() == 100 : "Fit height not set correctly";
        assert displayPicture.isPreserveRatio() : "Preserve ratio should be true";

        dialog.setText(text);
        displayPicture.setImage(img);

        assert dialog.getText().equals(text) : "Dialog text not set correctly";
        assert !dialog.getText().isEmpty() : "Dialog text should not be empty";
    }

    /**
     * Flips the dialog box such that the ImageView is on the left and text on the right.
     */
    private void flip() {
        assert this.getChildren() != null : "Cannot flip empty HBox";
        assert this.getChildren().size() == 2 : "Must have exactly 2 children to flip";

        ObservableList<Node> original = this.getChildren();
        ObservableList<Node> tmp = FXCollections.observableArrayList(original);
        assert tmp.size() == 2 : "Temporary list should have 2 elements";

        Collections.reverse(tmp);

        assert !tmp.equals(original) : "Reversal didn't change order";
        assert tmp.get(0) == original.get(1) : "First element not swapped correctly";
        assert tmp.get(1) == original.get(0) : "Second element not swapped correctly";

        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);

        assert this.getAlignment() == Pos.TOP_LEFT : "Alignment not set to TOP_LEFT";

        dialog.getStyleClass().add("reply-label");
        assert dialog.getStyleClass().contains("reply-label") : "Reply style class not added to dialog";
    }

    /**
     * Factory method to create a dialog box representing a user's message.
     * This creates a dialog box with the user's avatar on the right side
     * and the specified text message.
     *
     * @param text The message text to display in the dialog box
     * @param img  The user's avatar image to display
     * @return A DialogBox instance configured for user messages
     */
    public static DialogBox getUserDialog(String text, Image img) {
        assert text != null : "User dialog text cannot be null";
        assert img != null : "User dialog image cannot be null";

        DialogBox db = new DialogBox(text, img);

        assert db != null : "User DialogBox creation failed";
        assert db.getAlignment() == Pos.TOP_RIGHT || db.getAlignment() == null
                : "User dialog should be right-aligned or default";

        return db;
    }

    /**
     * Factory method to create a dialog box representing Mintel's response.
     * This creates a dialog box with Mintel's avatar on the left side
     * and flips the layout to distinguish it from user messages.
     *
     * @param text Mintel's response text to display in the dialog box
     * @param img  Mintel's avatar image to display
     * @return A DialogBox instance configured for Mintel responses with flipped layout
     */
    public static DialogBox getMintelDialog(String text, Image img) {
        assert text != null : "Mintel dialog text cannot be null";
        assert img != null : "Mintel dialog image cannot be null";

        var db = new DialogBox(text, img);
        assert db != null : "Mintel DialogBox creation failed";

        db.flip();

        assert db.getAlignment() == Pos.TOP_LEFT : "Mintel dialog should be left-aligned";
        assert db.dialog.getStyleClass().contains("reply-label")
                : "Mintel dialog missing reply style class";

        return db;
    }
}
