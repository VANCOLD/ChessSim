// src/main/java/at/chess/chesssimulator/controller/BoardController.java
package at.chess.chesssimulator.controller;

import at.chess.chesssimulator.board.Move;
import at.chess.chesssimulator.board.Position;
import at.chess.chesssimulator.board.ui.ChessBoardPane;
import at.chess.chesssimulator.board.ui.ChessBoardTilePane;
import at.chess.chesssimulator.gamelogic.GameMaster;
import at.chess.chesssimulator.gamelogic.Player;
import at.chess.chesssimulator.piece.enums.PieceColor;
import at.chess.chesssimulator.sound.SoundManager;
import at.chess.chesssimulator.sound.SoundType;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import java.util.List;

import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static at.chess.chesssimulator.board.config.ChessBoardConfig.*;

public class BoardController implements Player {

    protected static final Logger logger = LoggerFactory.getLogger(BoardController.class);

    private SoundManager soundManager;

    @Setter
    private GameMaster gameMaster;

    private Position selectedPosition;
    private boolean ignoreInput;
    private PieceColor turn;

    @Setter
    private boolean onlyOnePlayer;

    @FXML
    private Pane container;
    @FXML
    private ImageView draggedImage;
    @FXML
    private ChessBoardPane chessBoardPane;
    private boolean waitForConfirmation;

    @FXML
    public void initialize() {
        soundManager = SoundManager.getInstance();
        setMouseHandlers();
    }

    private void setMouseHandlers() {
        chessBoardPane.setOnMousePressed(this::mousePressed);
        chessBoardPane.setOnMouseReleased(this::mouseReleased);
        chessBoardPane.setOnMouseDragged(this::mouseMoved);
        logger.info("Setting up Mouse Handlers");
    }

    private void mousePressed(MouseEvent pressed) {
        Position clickedPosition = getPositionFromMouseEvent(pressed);
        logger.debug("Pressed tile at position: {}", clickedPosition);

        if (isInvalidClick(clickedPosition)) {
            ignoreInput = true;
            return;
        }

        selectedPosition = clickedPosition;
        prepareDragImage(clickedPosition, pressed);
    }

    private void mouseReleased(MouseEvent released) {
        if (ignoreInput || waitForConfirmation) {
            ignoreInput = false;
            return;
        }

        Position releasedPosition = getPositionFromMouseEvent(released);
        logger.debug("Released tile at position: {}", releasedPosition);
        sendMove(selectedPosition, releasedPosition);
    }

    private void mouseMoved(MouseEvent moved) {
        if (draggedImage != null && !ignoreInput) {
            draggedImage.setTranslateX(moved.getX() - (draggedImage.getImage().getWidth() / 2.0));
            draggedImage.setTranslateY(moved.getY() - (draggedImage.getImage().getHeight() / 2.0));
        }
    }

    private Position getPositionFromMouseEvent(MouseEvent event) {
        int row = (int) event.getX() / getTileWidth();
        int col = (int) event.getY() / getTileHeight();
        return new Position(row, col);
    }

    private void prepareDragImage(Position pos, MouseEvent pressed) {
        resetDrag();
        Image image = gameMaster.getPieceImage(pos);
        draggedImage = new ImageView(image);
        draggedImage.setTranslateX(pressed.getX() - (draggedImage.getImage().getWidth() / 2.0));
        draggedImage.setTranslateY(pressed.getY() - (draggedImage.getImage().getHeight() / 2.0));
        draggedImage.setVisible(true);
        container.getChildren().add(draggedImage);
    }

    public void resetDrag() {
        container.getChildren().remove(draggedImage);
        draggedImage = null;
    }

    private boolean isInvalidClick(Position clickedPosition) {
        if (clickedPosition.getRow() < 0 || clickedPosition.getRow() >= getTileHeight() ||
                clickedPosition.getCol() < 0 || clickedPosition.getCol() >= getTileWidth()) {
            return true;
        }

        if (!gameMaster.isOccupiedByColor(clickedPosition, turn)) {
            return true;
        }

        return false;
    }

    public void resetSelection() {
        if (selectedPosition != null) {
            chessBoardPane.toggleTile(selectedPosition);
            List<Position> positions = gameMaster.getPossibleMoves(selectedPosition);
            positions.forEach(p -> chessBoardPane.toggleIndicator(p));
            selectedPosition = null;
        }
    }

    @Override
    public void notifyTurn(PieceColor turn) {
        this.turn = turn;
        logger.info("It is now {}'s turn", turn);
    }

    @Override
    public void receiveMoveResult(Move move) {

        switch (move.getMoveType()) {
            case MOVE:
                logger.info("Move made");
                soundManager.playSound(SoundType.MOVE);
                break;
            case CAPTURE:
                logger.info("Capture made");
                soundManager.playSound(SoundType.CAPTURE);
                break;
            case SELECTION:
                logger.info("Selection made");
                break;
            case CHECK:
                logger.info("Checkmate!");
                break;
            case INVALID:
                logger.error("Invalid move type: {}", move.getMoveType());
                break;
        }
        resetDrag();
        this.updateBoard();
        waitForConfirmation = false;
    }

    @Override
    public void endTurn() {
        gameMaster.endTurn();
    }

    @Override
    public void updateBoard() {
        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getCols(); col++) {

                Position pos = new Position(row, col);
                chessBoardPane.get(pos).resetImage();
                Image image = gameMaster.getPieceImage(pos);
                chessBoardPane.get(row, col).setImage(new ImageView(image));

                if( gameMaster.isTileSelected(pos) ) {
                    chessBoardPane.toggleTile(pos);
                }

                if( gameMaster.isTileIndicator(pos) ) {
                    chessBoardPane.toggleIndicator(pos);
                }
            }
        }
    }

    @Override
    public void sendMove(Position originalPosition, Position newPosition) {
        boolean isValid = gameMaster.validateMove(originalPosition, newPosition);
        if (isValid) {
            waitForConfirmation = true;
            gameMaster.processInput(originalPosition, newPosition);
        }
    }
}