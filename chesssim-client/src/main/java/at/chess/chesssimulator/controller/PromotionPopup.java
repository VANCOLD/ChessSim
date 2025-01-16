package at.chess.chesssimulator.controller;

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

public class PromotionPopup {

    private PieceColor pieceColor;

    public void setInitialState(PieceColor pieceColor) {
        this.pieceColor = pieceColor;
    }

    public String showPromotionPopup() {
        // Create a new Stage for the popup
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Pawn Promotion");

        // Container for promotion options
        VBox container = new VBox(10);
        container.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // Buttons for each promotion option
        Button queenButton = createPromotionButton("queen", pieceColor.name().toLowerCase());
        Button rookButton = createPromotionButton("rook",  pieceColor.name().toLowerCase());
        Button bishopButton = createPromotionButton("bishop",  pieceColor.name().toLowerCase());
        Button knightButton = createPromotionButton("knight",  pieceColor.name().toLowerCase());

        // Capture the user's selection
        final String[] selectedPiece = new String[1]; // Array to hold selection
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

        HBox buttonContainer = new HBox(20, queenButton, rookButton, bishopButton, knightButton);
        buttonContainer.setStyle("-fx-alignment: center;");

        container.getChildren().add(buttonContainer);

        Scene popupScene = new Scene(container, 300, 150);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();

        return selectedPiece[0];
    }

    private Button createPromotionButton(String pieceName, String pieceColor) {

        Image image = PngLoader.getInstance().getImage(pieceColor,"_", pieceName, ".png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);

        // Create the Label for the piece name
        Label label = new Label(pieceName);
        label.setStyle("-fx-font-size: 12px; -fx-text-fill: black;");

        // Create the VBox to hold the image and the label
        VBox vbox = new VBox(5, imageView, label);
        vbox.setStyle("-fx-alignment: center;");

        // Create the button with the VBox
        Button button = new Button();
        button.setGraphic(vbox);  // Set the button graphic to the VBox

        return button;
    }
}
