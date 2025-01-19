package at.chess.chesssimulator.piece.movement;

import at.chess.chesssimulator.piece.enums.PieceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class MovementStrategyRegistry {

    private static final Map<PieceType, MovementStrategy> strategies = new HashMap<>();
    protected static final Logger logger = LoggerFactory.getLogger(MovementStrategyRegistry.class);

    static {
        logger.info("Registering movement strategies");
        registerStrategies();
    }

    private static void registerStrategies() {
        strategies.put(PieceType.KING, new KingMovementStrategy());
        strategies.put(PieceType.ROOK, new RookMovementStrategy());
        strategies.put(PieceType.BISHOP, new BishopMovementStrategy());
        strategies.put(PieceType.KNIGHT, new KnightMovementStrategy());
        strategies.put(PieceType.QUEEN, new QueenMovementStrategy());
        strategies.put(PieceType.PAWN, new PawnMovementStrategy());
    }

    public static MovementStrategy getStrategy(PieceType type) {
        return strategies.get(type);
    }

    public static void reloadChessBoard() {
        strategies.clear();
        registerStrategies();
    }
}