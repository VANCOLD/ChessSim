package at.chess.chesssimulator.controller.popup;

import at.chess.chesssimulator.gamelogic.command.Command;
import at.chess.chesssimulator.piece.enums.PieceColor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Stack;

/**
 * The {@code WinPopup} class is responsible for displaying a popup window
 * when a player wins the game. The popup provides options to rematch, save the move history, or close the application.
 */
public class WinPopup {

    /**
     * Enum representing the user's button choice in the popup.
     */
    public enum ButtonChoice {
        REMATCH,      // Indicates the user chose to start a rematch.
        CLOSE,        // Indicates the user chose to close the application.
        SAVE_HISTORY  // Indicates the user chose to save the move history.
    }

    private ButtonChoice currentChoice;

    /**
     * Displays the win popup with the specified winner and move history.
     *
     * @param winner      The {@link PieceColor} representing the winner (e.g., WHITE or BLACK).
     * @param moveHistory A {@link Stack} of {@link Command} objects representing the move history.
     * @return The {@link ButtonChoice} selected by the user.
     */
    public ButtonChoice showWinPopup(PieceColor winner, Stack<Command> moveHistory) {

        Stage popup = new Stage();
        popup.setTitle("Game Complete");
        popup.initModality(Modality.APPLICATION_MODAL);

        VBox vbox = new VBox(20);
        vbox.setStyle("-fx-padding: 20; -fx-alignment: center;");

        String color = winner.toString();
        String winnerMsg = color.substring(0, 1).toUpperCase() + color.substring(1).toLowerCase() + " wins!";

        Label message = new Label(winnerMsg);
        message.setStyle("-fx-font-size: 20px;");

        Button rematchButton = new Button("Rematch");
        rematchButton.setOnAction(event -> {
            popup.close();
            handleButtonChoice(ButtonChoice.REMATCH);
        });

        Button closeButton = new Button("Close");
        closeButton.setOnAction(event -> {
            popup.close();
            handleButtonChoice(ButtonChoice.CLOSE);
        });

        Button saveHistoryButton = setupHistoryButton(moveHistory);
        vbox.getChildren().addAll(message, rematchButton, saveHistoryButton, closeButton);

        Scene scene = new Scene(vbox, 300, 200);
        popup.setScene(scene);

        popup.setOnCloseRequest(event -> {
            handleButtonChoice(ButtonChoice.CLOSE);
        });

        popup.showAndWait();

        return getButtonChoice();
    }

    /**
     * Handles the user's button choice by setting the current choice.
     *
     * @param choice The {@link ButtonChoice} selected by the user.
     */
    private void handleButtonChoice(ButtonChoice choice) {
        this.currentChoice = choice;
    }

    /**
     * Retrieves the user's button choice.
     *
     * @return The {@link ButtonChoice} selected by the user.
     */
    private ButtonChoice getButtonChoice() {
        return currentChoice;
    }

    /**
     * Sets up the "Save Move History" button.
     * <p>
     *     When clicked, this button saves the move history to a file named "move_history.txt".
     *     If an error occurs while saving the file, a confirmation popup is displayed to the user.
     *     The confirmation popup will display either a success message or an error message.
     * </p>
     *
     * @param moveHistory A {@link Stack} of {@link Command} objects representing the move history.
     * @return The "Save Move History" button.
     */
    private Button setupHistoryButton(Stack<Command> moveHistory) {

        Button saveHistoryButton = new Button("Save Move History");
        saveHistoryButton.setOnAction(event -> {
            handleButtonChoice(ButtonChoice.SAVE_HISTORY);

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "move_history_" + timestamp + ".txt";

            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter(fileName));

                for (Command command : moveHistory) {
                    writer.write(command.toString());
                    writer.newLine();
                }

                ConfirmationPopup popup = new ConfirmationPopup("Success", "Move history has been saved successfully.");
                popup.show();

            } catch (IOException e) {

                ConfirmationPopup popup = new ConfirmationPopup("Error", "An error occurred while saving the move history.");
                popup.show();
                e.printStackTrace();

            } finally {

                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return saveHistoryButton;
    }

}