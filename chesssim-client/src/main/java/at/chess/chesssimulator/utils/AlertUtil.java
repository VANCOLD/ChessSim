package at.chess.chesssimulator.utils;


import javafx.scene.control.Alert;

public class AlertUtil {

    /**
     * Displays an information dialog to the user.
     *
     * @param title   The title of the dialog.
     * @param message The message to display.
     */
    public static void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().getButtonTypes().clear();
        alert.showAndWait();
    }

    /**
     * Displays an error dialog to the user.
     *
     * @param title   The title of the dialog.
     * @param message The message to display.
     */
    public static void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
