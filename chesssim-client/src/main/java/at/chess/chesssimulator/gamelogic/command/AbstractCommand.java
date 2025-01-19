package at.chess.chesssimulator.gamelogic.command;

import at.chess.chesssimulator.board.ChessBoard;
import at.chess.chesssimulator.board.Move;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@code AbstractCommand} class serves as a base class for specific commands in the chess game.
 * It implements the {@code Command} interface and provides a base implementation for executing and undoing commands.
 * The actual logic for executing and undoing commands should be provided by subclasses.
 */
public class AbstractCommand implements Command {

    // Logger to log command actions
    protected static final Logger logger = LoggerFactory.getLogger(AbstractCommand.class);

    // The chessboard on which the command is being executed
    protected final ChessBoard chessBoard;

    // The move associated with this command
    protected final Move move;

    /**
     * Constructor to initialize the {@code AbstractCommand} with a chessboard and a move.
     *
     * @param chessBoard The chessboard on which the command operates
     * @param move The move associated with the command
     */
    public AbstractCommand(ChessBoard chessBoard, Move move) {
        this.chessBoard = chessBoard;
        this.move = move;
    }

    /**
     * Executes the command. This method should be overridden in subclasses to define the action.
     * In this base class, it is a stub and does nothing.
     */
    @Override
    public void execute() {
        // stub, overwrite this in implementation
    }

    /**
     * Undoes the command. This method should be overridden in subclasses to define the undo action.
     * In this base class, it is a stub and does nothing.
     */
    @Override
    public void undo() {
        // stub, overwrite this in implementation
    }

    /**
     * Returns a string representation of the command, which is the string representation of the move.
     *
     * @return A string representation of the move associated with this command
     */
    @Override
    public String toString() {
        return move.toString();
    }
}
