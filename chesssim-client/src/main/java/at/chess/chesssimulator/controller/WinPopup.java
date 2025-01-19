package at.chess.chesssimulator.controller;

import at.chess.chesssimulator.board.ChessBoard;
import at.chess.chesssimulator.gamelogic.command.Command;
import at.chess.chesssimulator.piece.enums.PieceColor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Stack;

public class WinPopup {

    public enum ButtonChoice {
        REMATCH,
        CLOSE,
        SAVE_HISTORY
    }

    public ButtonChoice showWinPopup(PieceColor winner, Stack<Command> moveHistory) {
        // Create a new window (popup)
        Stage popup = new Stage();

        // Create a VBox to arrange the message and buttons vertically
        VBox vbox = new VBox(20);  // 20 is the spacing between elements
        vbox.setStyle("-fx-padding: 20; -fx-alignment: center;");

        String color = winner.toString();
        String winnerMsg = color.substring(0, 1).toUpperCase() + color.substring(1).toLowerCase() + " wins!";

        // Create a label with the winner message
        Label message = new Label(winnerMsg);
        message.setStyle("-fx-font-size: 20px;");

        // Create the Rematch button
        Button rematchButton = new Button("Rematch");
        rematchButton.setOnAction(event -> {
            // Return Rematch choice
            popup.close();
            handleButtonChoice(ButtonChoice.REMATCH);
        });

        // Create the Close button
        Button closeButton = new Button("Close");
        closeButton.setOnAction(event -> {
            // Return Close choice
            popup.close();
            handleButtonChoice(ButtonChoice.CLOSE);
        });

        // Create the Save Move History button
        Button saveHistoryButton = new Button("Save Move History");
        saveHistoryButton.setOnAction(event -> {
            // Only trigger action, but do not close the window
            handleButtonChoice(ButtonChoice.SAVE_HISTORY);
            ChessBoard.getInstance();
            for (Command command : moveHistory) {
                System.out.println(command);
            }
        });

        // Add the message and buttons to the VBox
        vbox.getChildren().addAll(message, rematchButton, saveHistoryButton, closeButton);

        // Create a scene and set it to the popup window
        Scene scene = new Scene(vbox, 300, 200);
        popup.setScene(scene);

        // Handle window close event (pressing X)
        popup.setOnCloseRequest(event -> {
            // Return Close choice if the window is closed using "X"
            handleButtonChoice(ButtonChoice.CLOSE);
        });

        popup.showAndWait(); // Show the popup window and wait for it to close

        // We are returning the value that corresponds to the clicked button
        return getButtonChoice();
    }

    // This is just a placeholder to handle button choice.
    private ButtonChoice currentChoice;

    private void handleButtonChoice(ButtonChoice choice) {
        this.currentChoice = choice;
    }

    // Method to get the current button choice after the popup closes
    private ButtonChoice getButtonChoice() {
        return currentChoice;
    }
}
