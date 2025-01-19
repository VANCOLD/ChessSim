package at.chess.chesssimulator.utils;

import at.chess.chesssimulator.piece.ChessPiece;
import at.chess.chesssimulator.piece.enums.PieceColor;
import at.chess.chesssimulator.piece.enums.PieceType;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static at.chess.chesssimulator.utils.Constants.FEN_FILE_PATH;

/**
 * Represents the FEN (Forsyth-Edwards Notation) utility class used for loading and interpreting chess board states.
 */
@Getter
public class FenNotation {

    protected static final Logger logger = LoggerFactory.getLogger(FenNotation.class);

    /**
     * The chessboard represented as an 8x8 array of {@link ChessPiece}.
     */
    private ChessPiece[][] board;

    /**
     * The current turn of the game, either {@link PieceColor#WHITE} or {@link PieceColor#BLACK}.
     */
    private PieceColor turn;

    /**
     * Default constructor that initializes the chessboard using the FEN file located at the default path.
     */
    public FenNotation() {
        this(FEN_FILE_PATH);
    }

    /**
     * Constructs a {@code FenNotation} instance by loading chessboard state from the specified FEN file.
     *
     * @param fenFilePath The path to the FEN file.
     */
    public FenNotation(String fenFilePath) {
        this.board = new ChessPiece[8][8];

        logger.info("Loading piece placements from the FEN: {}", fenFilePath);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(fenFilePath)))) {

            String fen = br.readLine();
            logger.info("Reading from FEN: {}", fen);
            String[] boardConfig = fen.split(" ");
            int currentRow = 0;

            for (String line : boardConfig[0].split("/")) {
                initializePieces(line, currentRow);
                currentRow++;
            }

            this.turn = boardConfig[1].equalsIgnoreCase("w") ? PieceColor.WHITE : PieceColor.BLACK;

        } catch (IOException e) {
            logger.error("An error occurred while reading data from the FEN file {}", FEN_FILE_PATH);
        }
        logger.info("Finished loading piece placements from the FEN file: {}", FEN_FILE_PATH);
    }

    /**
     * Parses a single row of FEN notation and populates the corresponding row of the chessboard.
     *
     * @param line       A string representing the row in FEN notation.
     * @param currentRow The index of the current row being initialized.
     */
    private void initializePieces(String line, int currentRow) {

        for (int i = 0; i < line.length(); i++) {
            char currentChar = line.charAt(i);

            if (Character.isDigit(currentChar)) {
                int emptySpaces = Character.getNumericValue(currentChar);
                i += emptySpaces - 1; // Skip over the empty spaces
                continue;
            }

            boolean isUpperCase = Character.isUpperCase(currentChar);
            PieceColor color = PieceColor.getPieceColor(isUpperCase ? 1 : 0);
            PieceType type = PieceType.getPieceType(Character.toLowerCase(currentChar));
            ChessPiece piece = ChessPiece.generateChessPiece(color, type);
            board[i][currentRow] = piece;
            logger.debug("Placing piece {} {} at row: {} - col: {}", color.name(), type.name(), i, currentRow);
        }
    }
}
