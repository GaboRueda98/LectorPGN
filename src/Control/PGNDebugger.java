package Control;

import Pieces.Piece;

import java.util.List;

public class PGNDebugger {
    private Board board;

    public PGNDebugger(Board board) {
        this.board = board;
    }

    /**
     * Validates and converts a PGN move to a Move object.
     * @param pgnMove PGN move string.
     * @return Move object, or null if invalid.
     */
    public Move parseMove(String pgnMove) {
        try {
            // Handle castling
            if (pgnMove.equals("O-O")) {
                Piece king = board.findKing(board.isWhiteToMove);
                int newCol = king.col + 2; // Kingside castling
                return new Move(board, king, newCol, king.row);
            } else if (pgnMove.equals("O-O-O")) {
                Piece king = board.findKing(board.isWhiteToMove);
                int newCol = king.col - 2; // Queenside castling
                return new Move(board, king, newCol, king.row);
            }

            // Handle other moves
            String pieceNotation = pgnMove.substring(0, 1).matches("[RNBQK]") ? pgnMove.substring(0, 1) : "";
            String target = pgnMove.replace("x", "").replace("+", "").replace("#", "");
            int col = target.charAt(target.length() - 2) - 'a'; // Convert letter to column
            int row = 8 - Character.getNumericValue(target.charAt(target.length() - 1)); // Convert to row

            for (Piece piece : board.pieceList) {
                if (piece.isWhite == board.isWhiteToMove &&
                        piece.name.startsWith(pieceNotation) &&
                        piece.isValidMovement(col, row)) {
                    return new Move(board, piece, col, row);
                }
            }
        } catch (Exception e) {
            System.err.println("Invalid PGN move: " + pgnMove);
        }
        return null;
    }

    /**
     * Executes a list of PGN moves on the board.
     * @param pgnMoves List of PGN moves.
     */
    public void executeMoves(List<String> pgnMoves) {
        for (String pgnMove : pgnMoves) {
            Move move = parseMove(pgnMove);
            if (move != null && board.isValidMove(move)) {
                board.makeMove(move);
            } else {
                System.err.println("Invalid or unplayable move: " + pgnMove);
            }
        }
    }
}
