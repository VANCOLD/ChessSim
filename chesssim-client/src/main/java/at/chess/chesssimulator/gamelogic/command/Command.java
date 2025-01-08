package at.chess.chesssimulator.gamelogic.command;

public interface Command {
    void execute();
    void undo();
}
