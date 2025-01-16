// src/main/java/at/chess/chesssimulator/gamelogic/GameMaster.java
package at.chess.chesssimulator.gamelogic;

import at.chess.chesssimulator.board.ChessBoard;
import at.chess.chesssimulator.board.Move;
import at.chess.chesssimulator.board.Position;
import at.chess.chesssimulator.board.enums.MoveType;
import at.chess.chesssimulator.board.utils.PositionUtils;
import at.chess.chesssimulator.gamelogic.command.*;
import at.chess.chesssimulator.piece.ChessPiece;
import at.chess.chesssimulator.piece.enums.PieceColor;
import at.chess.chesssimulator.piece.enums.PieceType;
import at.chess.chesssimulator.piece.movement.KingMovement;
import at.chess.chesssimulator.piece.movement.PawnMovement;
import at.chess.chesssimulator.utils.FenNotation;
import javafx.scene.image.Image;

import java.util.List;
import java.util.Stack;

public class GameMaster {

    private ChessBoard chessBoard;
    private Player blackPlayer;
    private Player whitePlayer;
    private PieceColor turn = PieceColor.WHITE;
    private Stack<Command> commandHistory = new Stack<>();
    private boolean inCheck;

    public GameMaster(Player blackPlayer, Player whitePlayer) {
        this(new FenNotation(), blackPlayer, whitePlayer);
    }

    public GameMaster(FenNotation gameState, Player blackPlayer, Player whitePlayer) {
        this.chessBoard = ChessBoard.getInstance();
        this.blackPlayer = blackPlayer;
        this.whitePlayer = whitePlayer;
        this.chessBoard.loadBoard(gameState);
        this.inCheck = false;
    }

    public void startGame() {
        this.blackPlayer.updateBoard();
        this.whitePlayer.updateBoard();
        this.turn = chessBoard.getTurn();
        if (turn == PieceColor.WHITE) {
            whitePlayer.notifyTurn(PieceColor.WHITE);
        } else {
            blackPlayer.notifyTurn(PieceColor.BLACK);
        }
    }

    public boolean validateMove(Position originalPosition, Position newPosition) {

        if ( this.inCheck && chessBoard.getPieceAt(originalPosition).getType() != PieceType.KING ) {
            return false;
        }

        if (!chessBoard.isOccupied(originalPosition)) {
            return false;
        }

        Position piecePosition = chessBoard.getPosition(originalPosition);
        ChessPiece piece = piecePosition.getPiece();
        if (piece.getColor() != turn) {
            return false;
        }

        if (chessBoard.getPieceAt(originalPosition).getType() == PieceType.KING
            && chessBoard.getPieceAt(newPosition).getType() == PieceType.ROOK) {
           if(  ( (KingMovement) piece.getMovementStrategy()).canQueenSideCastle(originalPosition, newPosition)) {
               return true;
           } else if ( ((KingMovement) piece.getMovementStrategy()).canKingSideCastle(originalPosition, newPosition)) {
               return true;
           }
        }


        return  piece.getMovementStrategy().canCapture(piecePosition, newPosition) ||
                piece.getMovementStrategy().canMove(piecePosition, newPosition);
    }

    public void makeMove(Move move) {

        Position originalPosition = move.getOriginalPosition();

        ChessPiece piece = chessBoard.getPieceAt(originalPosition);
        if (piece == null) {
            return;
        }

        Command command = switch (move.getMoveType()) {
            case MOVE -> new MoveCommand(chessBoard, move);
            case CAPTURE -> new CaptureCommand(chessBoard, move);
            case PROMOTE -> new PromotionCommand(chessBoard, move);
            case QCASTELING -> new QueenCastelingCommand(chessBoard, move);
            case KCASTELING -> new KingCastelingCommand(chessBoard, move);
            default -> null;
        };

        assert command != null;
        command.execute();
        commandHistory.push(command);
    }


    private Player getActivePlayer() {
        if (turn == PieceColor.WHITE) {
            return whitePlayer;
        } else {
            return blackPlayer;
        }
    }

    private void updateActivePlayer(Move move) {
        switch (turn) {
            case WHITE:
                whitePlayer.updateBoard();
                whitePlayer.notifyTurn(PieceColor.BLACK);
                blackPlayer.notifyTurn(PieceColor.BLACK);
                break;
            case BLACK:
                blackPlayer.updateBoard();
                blackPlayer.notifyTurn(PieceColor.WHITE);
                whitePlayer.notifyTurn(PieceColor.WHITE);
                break;
        }
    }

    public boolean isOccupiedByColor(Position pos, PieceColor color) {
        return chessBoard.isOccupiedByColor(pos, color);
    }

    public Image getPieceImage(Position pos) {
        ChessPiece piece = chessBoard.getPieceAt(pos);
        return piece != null ? piece.getImage() : null;
    }

    public List<Position> getPossibleMoves(Position pos) {
        ChessPiece piece = chessBoard.getPieceAt(pos);
        return piece != null ? piece.getMovementRange(chessBoard.getPosition(pos.getRow(), pos.getCol())) : List.of();
    }

    public void endTurn() {
        turn = (turn == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;
        if (turn == PieceColor.WHITE) {
            whitePlayer.notifyTurn(PieceColor.WHITE);
        } else {
            blackPlayer.notifyTurn(PieceColor.BLACK);
        }
    }

    public void processInput(Position originalPosition, Position newPosition) {

        originalPosition = PositionUtils.loadAndCopyPosition(originalPosition);
        newPosition = PositionUtils.loadAndCopyPosition(newPosition);

        if (!validateMove(originalPosition, newPosition)) {
            Move move = new Move(originalPosition, newPosition, MoveType.INVALID);
            getActivePlayer().receiveMoveResult(move);
            return;
        }

        ChessPiece piece = originalPosition.getPiece();
        if (piece == null) {
            Move move = new Move(originalPosition, newPosition, MoveType.INVALID);
            getActivePlayer().receiveMoveResult(move);
            return;
        }

        Move move = null;

        if (piece.getType() == PieceType.KING) {
            if( ((KingMovement) piece.getMovementStrategy()).canQueenSideCastle(originalPosition, newPosition) ) {
                move = new Move(originalPosition, newPosition, MoveType.QCASTELING);
            } else if( ((KingMovement) piece.getMovementStrategy()).canKingSideCastle(originalPosition, newPosition) ) {
                move = new Move(originalPosition, newPosition, MoveType.KCASTELING);
            }
        } else if (piece.getMovementStrategy().canCapture(originalPosition, newPosition)) {
            move = new Move(originalPosition, newPosition, MoveType.CAPTURE);
        } else if (piece.getMovementStrategy().canMove(originalPosition, newPosition)) {

            if( piece.getType() == PieceType.PAWN && ((PawnMovement) piece.getMovementStrategy()).canPromote( originalPosition, newPosition) ) {
                    move = new Move(originalPosition, newPosition, MoveType.PROMOTE);
            } else {
                move = new Move(originalPosition, newPosition, MoveType.MOVE);
            }

        } else {
            move = new Move(originalPosition, newPosition, MoveType.INVALID);
        }


        if (move.getMoveType() != MoveType.INVALID) {
            makeMove(move);
            getActivePlayer().receiveMoveResult(move);
        }
    }

    public boolean isTileIndicator(Position pos) {
        return chessBoard.getPosition(pos).isIndicator();
    }

    public boolean isTileSelected(Position pos) {
        return chessBoard.getPosition(pos).isSelected();
    }

    public void selectTile(Position selectedPosition) {

        Position pos = chessBoard.getPosition(selectedPosition);
        chessBoard.selectPosition(pos);
        pos.getPiece()
            .getMovementStrategy()
            .getPossibleMoves(pos)
            .forEach(p -> chessBoard.toggleIndicator(p));
    }

    public void resetTile() {
        chessBoard.resetTile();
    }
}