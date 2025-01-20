package at.chess.chesssimulator.controller.popup;

import at.chess.chesssimulator.piece.enums.PieceColor;
import at.chess.chesssimulator.utils.PngLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * The {@code PromotionPopup} class is responsible for displaying a popup
 * that allows the player to promote a pawn to one of the four possible piece types:
 * Queen, Rook, Bishop, or Knight.
 */
public class PromotionPopup {

    private PieceColor pieceColor;

    /**
     * Sets the initial state for the promotion popup, specifically the color of the pawn being promoted.
     *
     * @param pieceColor The color of the pawn being promoted (either WHITE or BLACK).
     */
    public void setInitialState(PieceColor pieceColor) {
        this.pieceColor = pieceColor;
    }

    /**
     * Displays the promotion popup, allowing the player to choose which piece to promote a pawn to.
     * The user can select one of the following options: Queen, Rook, Bishop, or Knight.
     *
     * @return A {@link String} representing the piece selected for promotion. The value will be one of:
     *         "Queen", "Rook", "Bishop", or "Knight".
     */
    public String showPromotionPopup() {

        // Create a new popup stage.
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Pawn Promotion");

        // Create a container for the buttons.
        VBox container = new VBox(10);
        container.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // Create promotion buttons for each piece type.
        Button queenButton = createPromotionButton("queen", pieceColor.name().toLowerCase());
        Button rookButton = createPromotionButton("rook", pieceColor.name().toLowerCase());
        Button bishopButton = createPromotionButton("bishop", pieceColor.name().toLowerCase());
        Button knightButton = createPromotionButton("knight", pieceColor.name().toLowerCase());

        // Array to hold the selected piece name.
        final String[] selectedPiece = new String[1];

        // Set up event handlers for each button.
        queenButton.setOnAction(e -> {
            selectedPiece[0] = "Queen";
            popupStage.close();
        });
        rookButton.setOnAction(e -> {
            selectedPiece[0] = "Rook";
            popupStage.close();
        });
        bishopButton.setOnAction(e -> {
            selectedPiece[0] = "Bishop";
            popupStage.close();
        });
        knightButton.setOnAction(e -> {
            selectedPiece[0] = "Knight";
            popupStage.close();
        });

        // Create a horizontal box to hold the buttons.
        HBox buttonContainer = new HBox(20, queenButton, rookButton, bishopButton, knightButton);
        buttonContainer.setStyle("-fx-alignment: center;");

        // Add the button container to the main container.
        container.getChildren().add(buttonContainer);

        // Set the scene and display the popup.
        Scene popupScene = new Scene(container, 300, 150);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();

        // Return the selected piece type.
        return selectedPiece[0];
    }

    /**
     * Creates a button for the promotion options, displaying an image and label for the given piece type.
     *
     * @param pieceName The name of the piece (e.g., "queen", "rook", "bishop", "knight").
     * @param pieceColor The color of the piece (either "white" or "black").
     * @return A {@link Button} with the piece's image and label as its graphic.
     */
    private Button createPromotionButton(String pieceName, String pieceColor) {

        // Load the image for the piece.
        Image image = PngLoader.getInstance().getImage(pieceColor,"_", pieceName, ".png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);

        // Create a label for the piece.
        Label label = new Label(pieceName);
        label.setStyle("-fx-font-size: 12px; -fx-text-fill: black;");

        // Create a vertical box to hold the image and label.
        VBox vbox = new VBox(5, imageView, label);
        vbox.setStyle("-fx-alignment: center;");

        // Create a button with the image and label as its graphic.
        Button button = new Button();
        button.setGraphic(vbox);

        return button;
    }
}
