package at.chess.chesssimulator.controller;

import at.chess.chesssimulator.board.ChessBoard;
import at.chess.chesssimulator.board.Move;
import at.chess.chesssimulator.board.Position;
import at.chess.chesssimulator.board.enums.MoveType;
import at.chess.chesssimulator.board.ui.ChessBoardPane;
import at.chess.chesssimulator.controller.popup.WinPopup;
import at.chess.chesssimulator.gamelogic.GameMaster;
import at.chess.chesssimulator.gamelogic.Player;
import at.chess.chesssimulator.piece.enums.PieceColor;
import at.chess.chesssimulator.sound.SoundManager;
import at.chess.chesssimulator.sound.SoundType;
import at.chess.chesssimulator.utils.FxmlFiles;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static at.chess.chesssimulator.board.ChessBoard.resetInstance;
import static at.chess.chesssimulator.board.config.ChessBoardConfig.*;
import static at.chess.chesssimulator.piece.movement.MovementStrategyRegistry.reloadChessBoard;

public class BoardController implements Player {

    protected static final Logger logger = LoggerFactory.getLogger(BoardController.class);

    private SoundManager soundManager;

    @Setter
    private GameMaster gameMaster;

    @Getter
    private Position selectedPosition;

    private boolean ignoreInput;

    private PieceColor turn;

    private MouseInputHandler mouseInputHandler;

    @Setter
    private boolean onlyOnePlayer;

    @Setter
    private PieceColor myTurn;

    @Setter
    private Stage stage;

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
        soundManager.playSound(SoundType.GAME_START);
    }

    public void handleQuit(ActionEvent event) {
        resetInstance();
        reloadChessBoard();
        MainController.loadStage(FxmlFiles.MAIN);
        this.stage.close();
    }

    public void handleUndo(ActionEvent event) {
        if (waitForConfirmation) {
            return;
        }
        gameMaster.undoMove();
    }

    private void setMouseHandlers() {
        mouseInputHandler = new MouseInputHandler();
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

        logger.info("Move result: {}", move.getMoveType());

        if(move.getMoveType() == MoveType.CHECKMATE) {

            soundManager.playSound(SoundType.getSound(move.getMoveType()));
            gameMaster.endTurn();
            this.updateBoard();
            waitForConfirmation = false;
            this.mouseInputHandler.resetDrag();
            resetInstance();
            reloadChessBoard();

            WinPopup winPopup = new WinPopup();
            WinPopup.ButtonChoice choice = winPopup.showWinPopup(turn, gameMaster.getCommandHistory());

            switch (choice) {
                case REMATCH:
                    MainController.loadStage(FxmlFiles.BOARD);
                    gameMaster.setChessBoard(ChessBoard.getInstance());
                    gameMaster.startGame();
                    this.updateBoard();
                    break;
                case CLOSE:
                    MainController.loadStage(FxmlFiles.MAIN);
                    this.stage.close();
                    break;
                default:
                    logger.info("Unknown choice");
            }

            return;
        }

        if (move.getMoveType() == MoveType.INVALID) {
            logger.error("Invalid move type: {}", move.getMoveType());
        } else {
            logger.info("Move made");
            soundManager.playSound(SoundType.getSound(move.getMoveType()));
            gameMaster.endTurn();
        }

        gameMaster.resetTile();
        this.updateBoard();
        waitForConfirmation = false;
    }


    @Override
    public void updateBoard() {
        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getCols(); col++) {

                Position pos = new Position(row, col);
                chessBoardPane.get(pos).resetImage();
                Image image = gameMaster.getPieceImage(pos);
                chessBoardPane.get(row, col).setImage(new ImageView(image));

                if(gameMaster.isTileSelected(pos)) {
                    chessBoardPane.toggleTile(pos);
                    chessBoardPane.opacity(pos, 0.5);
                } else if(gameMaster.isTileInCheck(pos)) {
                    chessBoardPane.setCheck(pos);
                } else {
                    chessBoardPane.resetTile(pos);
                    chessBoardPane.resetOpacity(pos);
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
        waitForConfirmation = true;
        gameMaster.processInput(originalPosition, newPosition);
    }

    private class MouseInputHandler {

        private void mousePressed(MouseEvent pressed) {

            if(onlyOnePlayer && myTurn != turn) {
                ignoreInput = true;
                return;
            }

            Position clickedPosition = getPositionFromMouseEvent(pressed);
            logger.info("Selected tile at position: {}", clickedPosition);

            if (waitForConfirmation) {
                ignoreInput = true;
                return;
            }

            if (gameMaster.isOccupiedByColor(clickedPosition, turn)) {

                if(gameMaster.amIInCheck(turn)) {
                    if(!gameMaster.isMyKing(clickedPosition)) {
                        ignoreInput = true;
                        return;
                    }
                }

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

            sendMove(selectedPosition, releasedPosition);
            selectedPosition = null;
            gameMaster.resetTile();
            updateBoard();

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

