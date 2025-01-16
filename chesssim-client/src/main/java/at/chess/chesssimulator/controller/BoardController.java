package at.chess.chesssimulator.controller;

import at.chess.chesssimulator.board.Move;
import at.chess.chesssimulator.board.Position;
import at.chess.chesssimulator.board.enums.MoveType;
import at.chess.chesssimulator.board.ui.ChessBoardPane;
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

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static at.chess.chesssimulator.board.config.ChessBoardConfig.*;

public class BoardController implements Player {

    protected static final Logger logger = LoggerFactory.getLogger(BoardController.class);

    private SoundManager soundManager;

    @Setter
    private GameMaster gameMaster;

    @Getter
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
        MouseInputHandler mouseInputHandler = new MouseInputHandler();
        chessBoardPane.setOnMousePressed(mouseInputHandler::mousePressed);
        chessBoardPane.setOnMouseReleased(mouseInputHandler::mouseReleased);
        chessBoardPane.setOnMouseDragged(mouseInputHandler::mouseMoved);
        logger.info("Setting up Mouse Handlers");
    }

    @Override
    public void notifyTurn(PieceColor turn) {
        this.turn = turn;
        logger.info("It is now {}'s turn", turn);
    }

    @Override
    public void receiveMoveResult(Move move) {

        if (move.getMoveType() == MoveType.INVALID) {
            logger.error("Invalid move type: {}", move.getMoveType());
        } else {
            logger.info("Move made");
            soundManager.playSound(SoundType.getSound(move.getMoveType()));
            this.endTurn();
        }

        gameMaster.resetTile();
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

                if (gameMaster.isTileSelected(pos)) {
                    chessBoardPane.toggleTile(pos);
                } else {
                    chessBoardPane.resetTile(pos);
                }

                if(gameMaster.isTileIndicator(pos)) {
                    chessBoardPane.toggleIndicator(pos);
                } else {
                    chessBoardPane.resetIndicator(pos);
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


    private class MouseInputHandler {

        private void mousePressed(MouseEvent pressed) {
            Position clickedPosition = getPositionFromMouseEvent(pressed);
            logger.info("Selected tile at position: {}", clickedPosition);

            if (waitForConfirmation) {
                ignoreInput = true;
                return;
            }

            if (gameMaster.isOccupiedByColor(clickedPosition, turn)) {
                selectedPosition = clickedPosition;
                prepareDragImage(clickedPosition, pressed);
                gameMaster.selectTile(clickedPosition);
                updateBoard();
                ignoreInput = false;
            } else {
                ignoreInput = true;
            }
        }

        private void mouseReleased(MouseEvent released) {
            if (ignoreInput || waitForConfirmation) {
                ignoreInput = false;
                resetDrag();
                return;
            }

            Position releasedPosition = getPositionFromMouseEvent(released);
            logger.info("Released tile at position: {}", releasedPosition);

            if (gameMaster.validateMove(selectedPosition, releasedPosition)) {
                logger.info("Valid move from {} to {}", selectedPosition, releasedPosition);
                sendMove(selectedPosition, releasedPosition);
                selectedPosition = null;
            } else {
                logger.info("Invalid move, deselecting piece at {}", selectedPosition);
                selectedPosition = null;
                gameMaster.resetTile();
                updateBoard();
            }

            resetDrag();
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
    }

}

