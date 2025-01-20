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
import at.chess.chesssimulator.piece.movement.KingMovementStrategy;
import at.chess.chesssimulator.piece.movement.PawnMovementStrategy;
import at.chess.chesssimulator.utils.FenNotation;
import javafx.scene.image.Image;
import lombok.Getter;
import lombok.Setter;


import java.util.Stack;

import static at.chess.chesssimulator.board.enums.MoveType.CHECK;
import static at.chess.chesssimulator.board.utils.PositionUtils.sameCoordinates;
import static at.chess.chesssimulator.piece.enums.PieceType.KING;
import static at.chess.chesssimulator.piece.enums.PieceType.ROOK;
import static at.chess.chesssimulator.piece.movement.MovementStrategyRegistry.getStrategy;

public class GameMaster {

    @Setter
    private ChessBoard chessBoard;
    private Player blackPlayer;
    private Player whitePlayer;
    @Getter
    private PieceColor turn = PieceColor.WHITE;
    @Getter
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

        if ( this.inCheck && chessBoard.getPieceAt(originalPosition).getType() != KING ) {
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

        if(newPosition.containsPiece()) {
            if (chessBoard.getPieceAt(originalPosition).getType() == KING
                && chessBoard.getPieceAt(newPosition).getType() == ROOK) {

                KingMovementStrategy strategy = (KingMovementStrategy) getStrategy(KING);

                if (strategy.canQueenSideCastle(newPosition)) {
                    return true;
                } else if (strategy.canKingSideCastle(newPosition)) {
                    return true;
                }
            }
        }


        return  getStrategy(piece.getType()).canCapture(piecePosition, newPosition) ||
                getStrategy(piece.getType()).canMove(piecePosition, newPosition);
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
            case QCASTLING -> new QueenCastlingCommand(chessBoard, move);
            case KCASTLING -> new KingCastlingCommand(chessBoard, move);
            case CHECK -> new CheckCommand(chessBoard, move);
            case CHECKMATE -> new CheckmateCommand(chessBoard, move);
            default -> null;
        };

        chessBoard.setCheck(move.getMoveType() == CHECK || move.getMoveType() == MoveType.CHECKMATE);
        if(!chessBoard.isInCheck()) {
            chessBoard.getPosition(move.getOriginalPosition()).setInCheck(false);
        }

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

    public boolean isOccupiedByColor(Position pos, PieceColor color) {
        return chessBoard.isOccupiedByColor(pos, color);
    }

    public Image getPieceImage(Position pos) {
        ChessPiece piece = chessBoard.getPieceAt(pos);
        return piece != null ? piece.getImage() : null;
    }

    public void endTurn() {
        turn = PieceColor.getOppositeColor(turn);
        if (turn == PieceColor.WHITE) {
            whitePlayer.notifyTurn(PieceColor.WHITE);
        } else {
            blackPlayer.notifyTurn(PieceColor.BLACK);
        }
        chessBoard.setTurn(turn);
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

        if (piece.getType() == KING) {

            KingMovementStrategy kingStrategy = (KingMovementStrategy) getStrategy(KING);

            if (kingStrategy.canQueenSideCastle(newPosition)) {
                move = new Move(originalPosition, newPosition, MoveType.QCASTLING);
            } else if (kingStrategy.canKingSideCastle(newPosition)) {
                move = new Move(originalPosition, newPosition, MoveType.KCASTLING);
            } else {
                move = new Move(originalPosition, newPosition, MoveType.MOVE);
            }

        } else if (piece.getType() == PieceType.PAWN) {

            PawnMovementStrategy pawnStrategy = (PawnMovementStrategy) getStrategy(piece.getType());

            // Check for pawn promotion first
            if (pawnStrategy.canPromote(originalPosition, newPosition)) {
                move = new Move(originalPosition, newPosition, MoveType.PROMOTE);
            } else if (getStrategy(piece.getType()).canCapture(originalPosition, newPosition)) {
                move = new Move(originalPosition, newPosition, MoveType.CAPTURE);

                // Check if the capture results in a check
                if (isCheck(piece, newPosition)) {
                    move = new Move(originalPosition, newPosition, MoveType.CHECK);
                }

            } else if (getStrategy(piece.getType()).canMove(originalPosition, newPosition)) {

                // Check if the pawn is moving diagonally and checking the king
                if (isCheck(piece, newPosition)) {
                    move = new Move(originalPosition, newPosition, MoveType.CHECK);
                } else {
                    move = new Move(originalPosition, newPosition, MoveType.MOVE);
                }
            }

        } else if (getStrategy(piece.getType()).canCapture(originalPosition, newPosition)) {
            move = new Move(originalPosition, newPosition, MoveType.CAPTURE);

        } else if (getStrategy(piece.getType()).canMove(originalPosition, newPosition)) {

            if (isCheckmate(originalPosition.getPiece(), newPosition)) {
                move = new Move(originalPosition, newPosition, MoveType.CHECKMATE);
            } else if (isCheck(originalPosition.getPiece(), newPosition)) {
                move = new Move(originalPosition, newPosition, MoveType.CHECK);
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

    private boolean isCheckmate(ChessPiece piece, Position piecePosition) {
        PieceType kingType = chessBoard.getKingPosition(turn).getPiece().getType();
        return ((KingMovementStrategy) getStrategy(kingType)).isCheckmate(piece, piecePosition);
    }

    private boolean isCheck(ChessPiece piece, Position piecePosition) {
        PieceType kingType = chessBoard.getKingPosition(turn).getPiece().getType();
        return ((KingMovementStrategy) getStrategy(kingType)).isCheck(piece, piecePosition);
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
        getStrategy(pos.getPiece().getType())
            .getPossibleMoves(pos)
            .forEach(p -> chessBoard.toggleIndicator(p));
    }

    public void resetTile() {
        chessBoard.resetTile();
    }

    public boolean isTileInCheck(Position pos) {
        return chessBoard.getPosition(pos).isInCheck();
    }

    public boolean amIInCheck(PieceColor turn) {
        return chessBoard.isInCheck() && chessBoard.getTurn() == turn;
    }

    public boolean isMyKing(Position clickedPosition) {
        return sameCoordinates(chessBoard.getKingPosition(turn), clickedPosition);
    }

    public void undoMove() {
        if (commandHistory.isEmpty()) {
            return;
        }

        Command command = commandHistory.pop();
        command.undo();

        if (command instanceof CheckCommand) {
            this.inCheck = false;
            chessBoard.setInCheck(false);
        }
        getActivePlayer().updateBoard();
        endTurn();

    }

}