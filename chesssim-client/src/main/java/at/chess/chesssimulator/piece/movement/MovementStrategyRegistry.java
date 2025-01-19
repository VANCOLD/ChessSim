package at.chess.chesssimulator.piece.movement;

import at.chess.chesssimulator.piece.enums.PieceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * The {@code MovementStrategyRegistry} class is responsible for managing and providing the appropriate movement strategy
 * for each type of chess piece. It stores a registry of strategies and provides methods to retrieve them.
 * The strategies define the movement rules for different piece types in the game of chess.
 */
public class MovementStrategyRegistry {

    private static final Map<PieceType, MovementStrategy> strategies = new HashMap<>();
    protected static final Logger logger = LoggerFactory.getLogger(MovementStrategyRegistry.class);

    /*
     * Static block that initializes and registers all movement strategies.
     */
    static {
        logger.info("Registering movement strategies");
        registerStrategies();
    }

    /**
     * Registers movement strategies for all chess pieces (King, Rook, Bishop, Knight, Queen, Pawn).
     * Each piece type is associated with its corresponding movement strategy.
     */
    private static void registerStrategies() {
        strategies.put(PieceType.KING, new KingMovementStrategy());
        strategies.put(PieceType.ROOK, new RookMovementStrategy());
        strategies.put(PieceType.BISHOP, new BishopMovementStrategy());
        strategies.put(PieceType.KNIGHT, new KnightMovementStrategy());
        strategies.put(PieceType.QUEEN, new QueenMovementStrategy());
        strategies.put(PieceType.PAWN, new PawnMovementStrategy());
    }

    /**
     * Retrieves the movement strategy for a given piece type.
     *
     * @param type The type of the chess piece (e.g., KING, ROOK, BISHOP, etc.)
     * @return The corresponding movement strategy for the given piece type, or {@code null} if no strategy is found.
     */
    public static MovementStrategy getStrategy(PieceType type) {
        return strategies.get(type);
    }

    /**
     * Reloads the chessboard by clearing and re-registering the movement strategies.
     * This method is useful when resetting or reloading the game state.
     */
    public static void reloadChessBoard() {
        strategies.clear();
        registerStrategies();
    }
}
