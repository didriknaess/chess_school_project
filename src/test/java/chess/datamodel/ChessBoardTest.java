package chess.datamodel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import chess.datamodel.Piece.*;

public class ChessBoardTest {
    ChessBoard board;
    @BeforeEach
    public void setUp() {
        this.board = new ChessBoard();
    }
    @Test
    @DisplayName("Test getPiece(Position)")
    public void testGetAndSetPiece() {
        // asserts that adding a piece with an invalid position throws an error
        assertThrows(IllegalArgumentException.class, () -> new Piece(PieceType.PAWN, Piece.Color.WHITE, new Position(-1, -1)));
        assertThrows(IllegalArgumentException.class, () -> new Piece(PieceType.BISHOP, Piece.Color.BLACK, new Position("x9")));
        // getting an invalid position
        assertThrows(IllegalArgumentException.class, () -> board.getPiece(new Position(-1, -1)));
        // tests that getting pieces from the spesified spots returns correct values
        Piece pawn = new Piece(PieceType.PAWN, Color.WHITE, new Position(3, 3));
        board.addPiece(pawn);
        for (int i = 0; i<8; i++) {
            for (int j = 0; j<8; j++) {
                if (i == 3 && j == 3) {
                    assertEquals(pawn, board.getPiece(new Position(i, j)));
                } else {
                    assertEquals(null, board.getPiece(new Position(i, j)));
                }
            }
        }
    }
    @Test
    @DisplayName("Test getPiece(Position)")
    public void testClearBoard() {
        Piece pawn = new Piece(PieceType.PAWN, Color.BLACK, new Position(6, 2));
        Piece rook = new Piece(PieceType.ROOK, Color.BLACK, new Position(7, 7));
        Piece knight = new Piece(PieceType.KNIGHT, Color.BLACK, new Position(7, 1));
        Piece bishop = new Piece(PieceType.BISHOP, Color.BLACK, new Position(7, 5));
        Piece queen = new Piece(PieceType.QUEEN, Color.BLACK, new Position(7, 3));
        Piece king = new Piece(PieceType.KING, Color.BLACK, new Position(7, 4));
        board.addPiece(pawn);
        board.addPiece(rook);
        board.addPiece(knight);
        board.addPiece(bishop);
        board.addPiece(queen);
        board.addPiece(king);
        board.clearBoard();
        for (int i = 0; i<8; i++) {
            for (int j = 0; j<8; j++) {
                assertEquals(null, board.getPiece(new Position(i, j)));
            }
        }
    }
    @Test
    @DisplayName("Test isSquareEmpty(Position)")
    public void testIsSquareEmpty() {
        // tester hva som skjer om du sjekker om en ugyldig posisjon er tom
        assertThrows(IllegalArgumentException.class, () -> board.isSquareEmpty(new Position(-1, -1)));
        // tester at funksjonen returnerer korrekte verdier for brettets posisjoner
        Piece pawn = new Piece(PieceType.PAWN, Color.WHITE, new Position(3, 3));
        board.addPiece(pawn);
        for (int i = 0; i<8; i++) {
            for (int j = 0; j<8; j++) {
                if (i == 3 && j == 3) {
                    assertFalse(board.isSquareEmpty(new Position(i, j)));
                } else {
                    assertTrue(board.isSquareEmpty(new Position(i, j)));
                }
            }
        }
    }
    @Test
    @DisplayName("Test doMove(Move)")
    public void testDoMove() {
        // tries to move a piece respectively to and from invalid positions
        assertThrows(IllegalArgumentException.class, () -> board.doMove(new Move(new Position(-1, -1), new Position(1, 1))));
        assertThrows(IllegalArgumentException.class, () -> board.doMove(new Move(new Position(1, 1), new Position(-1, -1))));
        // tests that the piece is moved to and from the correct positions
        Position pos = new Position(3, 3);
        Position target = new Position(2, 2);
        Piece pawn = new Piece(PieceType.PAWN, Color.WHITE, pos);
        board.addPiece(pawn);
        board.doMove(new Move(pos, target));
        for (int i = 0; i<8; i++) {
            for (int j = 0; j<8; j++) {
                if (i == 2 && j == 2) {
                    assertEquals(pawn, board.getPiece(new Position(i, j)));
                } else {
                    assertEquals(null, board.getPiece(new Position(i, j)));
                }
            }
        }
        // tests that the piece's position stored in the Piece class also is correctly updated
        assertEquals(target, pawn.getPosition());
    }
    @Test
    @DisplayName("Test doCastle(Move, boolean)")
    public void testDoCastle() {
        // tries to move a piece respectively to and from invalid positions
        assertThrows(IllegalArgumentException.class, () -> board.doCastle(new Move(new Position(-1, -1), new Position(1, 1)), true));
        assertThrows(IllegalArgumentException.class, () -> board.doCastle(new Move(new Position(1, 1), new Position(-1, -1)), false));
        // long castling (left = true)
        board.clearBoard();
        // tests that the piece is moved to and from the correct positions
        Position kPos = new Position(7, 4);
        Position rPos = new Position(7, 0);
        Position kTarget = new Position(7, 2);
        Position rTarget = new Position(7, 3);
        Piece king = new Piece(PieceType.KING, Color.WHITE, kPos);
        Piece rook = new Piece(PieceType.ROOK, Color.WHITE, rPos);
        board.addPiece(king);
        board.addPiece(rook);
        board.doCastle(new Move(kPos, kTarget), true);
        for (int i = 0; i<8; i++) {
            for (int j = 0; j<8; j++) {
                if (i == 7 && j == 2) {
                    assertEquals(king, board.getPiece(new Position(i, j)));
                } else if (i == 7 && j == 3) {
                    assertEquals(rook, board.getPiece(new Position(i, j)));
                } else {
                    assertEquals(null, board.getPiece(new Position(i, j)));
                }
            }
        }
        // tests that the piece's position stored in the Piece class also is correctly updated
        assertEquals(kTarget, king.getPosition());
        assertEquals(rTarget, rook.getPosition());
        // short castling (left = false)
        board.clearBoard();
        // tests that the piece is moved to and from the correct positions
        kPos = new Position(7, 4);
        rPos = new Position(7, 7);
        kTarget = new Position(7, 6);
        rTarget = new Position(7, 5);
        king = new Piece(PieceType.KING, Color.WHITE, kPos);
        rook = new Piece(PieceType.ROOK, Color.WHITE, rPos);
        board.addPiece(king);
        board.addPiece(rook);
        board.doCastle(new Move(kPos, kTarget), false);
        for (int i = 0; i<8; i++) {
            for (int j = 0; j<8; j++) {
                if (i == 7 && j == 6) {
                    assertEquals(king, board.getPiece(new Position(i, j)));
                } else if (i == 7 && j == 5) {
                    assertEquals(rook, board.getPiece(new Position(i, j)));
                } else {
                    assertEquals(null, board.getPiece(new Position(i, j)));
                }
            }
        }
        // tests that the piece's position stored in the Piece class also is correctly updated
        assertEquals(kTarget, king.getPosition());
        assertEquals(rTarget, rook.getPosition());
    }
}
