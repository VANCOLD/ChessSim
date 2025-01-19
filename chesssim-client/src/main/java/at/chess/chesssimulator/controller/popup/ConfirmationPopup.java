package at.chess.chesssimulator.controller.popup;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Pos;

/**
 * A simple popup window that displays a message with an "OK" button.
 * This popup blocks interaction with the main application window until closed.
 *
 * <p>Used to display a confirmation or informational message to the user.
 * The user can dismiss the popup by clicking the "OK" button.</p>
 *
 * Example usage:
 * <pre>
 *     ConfirmationPopup popup = new ConfirmationPopup("Title", "Your message here.");
 *     popup.show();
 * </pre>
 */
public class ConfirmationPopup {

    private String title;
    private String message;

    /**
     * Creates a new ConfirmationPopup instance with the specified title and message.
     *
     * @param title   the title of the popup window
     * @param message the message to display inside the popup
     */
    public ConfirmationPopup(String title, String message) {
        this.title = title;
        this.message = message;
    }

    /**
     * Displays the popup window with the specified title and message.
     *
     * <p>This method blocks the interaction with the main window until the popup is closed by the user.
     * It creates a new stage (window) with a label displaying the message and an "OK" button to close the popup.</p>
     */
    public void show() {

        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle(title);

        Label messageLabel = new Label(message);
        messageLabel.setStyle("-fx-font-size: 14px;");

        Button okButton = new Button("OK");
        okButton.setOnAction(e -> popupStage.close());

        VBox popupLayout = new VBox(10);
        popupLayout.setAlignment(Pos.CENTER);
        popupLayout.getChildren().addAll(messageLabel, okButton);

        Scene popupScene = new Scene(popupLayout, 300, 150);
        popupStage.setScene(popupScene);

        popupStage.showAndWait();
    }
}
