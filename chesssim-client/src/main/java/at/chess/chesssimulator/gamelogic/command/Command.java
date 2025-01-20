package at.chess.chesssimulator.gamelogic.command;

/**
 * The {@code Command} interface represents a command in the chess game.
 * It defines the contract for commands that can be executed and undone.
 * Commands in the game (such as making a move, undoing a move, etc.) should implement this interface.
 * The {@code execute()} method performs the action of the command, and the {@code undo()} method reverts it.
 */
public interface Command {

    /**
     * Executes the command. The implementation of this method will define the action
     * that the command performs, such as moving a piece on the chessboard.
     */
    void execute();

    /**
     * Undoes the command. The implementation of this method will define the action
     * to reverse the effect of the {@code execute()} method, such as undoing a move.
     */
    void undo();
}
