package at.chess.chesssimulator.controller;

import at.chess.chesssimulator.board.ChessBoard;
import at.chess.chesssimulator.board.ui.ChessBoardPane;
import at.chess.chesssimulator.board.ui.ChessBoardTilePane;
import at.chess.chesssimulator.piece.ChessPiece;
import at.chess.chesssimulator.piece.enums.PieceColor;
import at.chess.chesssimulator.piece.enums.PieceType;
import javafx.fxml.FXML;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static at.chess.chesssimulator.board.config.ChessBoardConfig.*;

public class BoardController {

    private ChessBoard chessBoard;
    private ChessBoardTilePane selectedTile;
    private List<ChessBoardTilePane> possibleTiles;

    @FXML
    private ChessBoardPane chessBoardPane;

    @FXML
    public void initialize() {

        this.chessBoard   = ChessBoard.getInstance();
        this.selectedTile = null;
        this.possibleTiles = null;
        this.loadBoard();

        this.chessBoardPane.setOnMouseClicked(click -> {

            int rowClick = (int) click.getSceneX() / getTileWidth();
            int colClick = (int) click.getSceneY() / getTileHeight();


            if (this.selectedTile != null) {
                this.selectedTile.resetColor();

                if(this.possibleTiles != null) {
                    this.possibleTiles.forEach(ChessBoardTilePane::toggleIndicator);
                }
            }

            if(!this.chessBoard.isOccupied(rowClick,colClick)) {
                System.out.println("Clicked at: (" + rowClick + "/" + colClick +")");
                this.selectedTile = null;
                this.possibleTiles = null;
                return;
            }

            this.selectedTile = this.chessBoardPane.get(rowClick,colClick);
            this.selectedTile.setColor(getSelectedTileColor());

            var piece = this.chessBoard.getPieceAt(rowClick,colClick);
            System.out.println(piece.getColor().name().toLowerCase() + " " + piece.getType().name().toLowerCase() + " (" + rowClick + "/" + colClick +")");

            var possiblePositions = piece.getMovementRange(chessBoard.getPosition(rowClick,colClick));

            /*
             * 1. filter all the position we acquired, and remove the ones that are occupied by a piece
             * 2. get the panes to toggle the indicators.
             */
            this.possibleTiles = possiblePositions
                                   .stream()
                                   .map(pos -> chessBoardPane.get(pos.getRow(), pos.getCol()))
                                   .toList();

            this.possibleTiles.forEach(ChessBoardTilePane::toggleIndicator);

        });

    }


    private void loadBoard() {
        String csvFile = "/config/default_layout.csv";
        String line;
        String csvSplitBy = ";";

        try (BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(csvFile)))) {

            PieceColor color;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(csvSplitBy);
                color = PieceColor.getPieceColor(Integer.parseInt(values[0]));

                for (int i = 1; i < values.length; i++) {

                    PieceType type = PieceType.getPieceType(values[i].charAt(0));
                    int col = Integer.parseInt("" + values[i].charAt(1));
                    int row = Integer.parseInt("" + values[i].charAt(2));
                    ChessPiece piece = ChessPiece.generateChessPiece(color,type);
                    chessBoardPane.setImageOfTile(row, col, piece.getImage());
                    chessBoard.placePiece(row, col, piece);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
