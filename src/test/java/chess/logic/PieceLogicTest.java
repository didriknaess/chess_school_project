package chess.logic;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import chess.datamodel.ChessBoard;
import chess.datamodel.Move;
import chess.datamodel.Piece;
import chess.datamodel.Position;

// tests all the pieces, instead of creating a test for each OfficerLogic child class individually
public class PieceLogicTest {
    private ChessBoard board;
    private PieceLogic logic;
    @BeforeEach
    public void setUp() {
        board = new ChessBoard();
        logic = new PieceLogic(board);
    }
    private List<Position> toDestinations(List<Move> moves) {
        List<Position> destinations = new ArrayList<Position>();
        for (Move m : moves) {
            destinations.add(m.getTo());
        }
        return destinations;
    }
    @Test
    @DisplayName("Tests pawns legal moves")
    public void testPawn() {
        Piece pawn = new Piece(Piece.PieceType.PAWN, Piece.Color.WHITE, new Position(1, 4));
        board.addPiece(pawn);
        // when the pawn has not moved
        List<Move> moves = logic.getLegalMoves(pawn);
        assertEquals(moves.size(), 2);
        assertTrue(moves.contains(new Move(new Position(1, 4), new Position(2, 4))));
        assertTrue(moves.contains(new Move(new Position(1, 4), new Position(3, 4))));
        // when the pawn has moved
        pawn.setFirstTurnMoved(1);
        moves = logic.getLegalMoves(pawn);
        assertEquals(moves.size(), 1);
        assertTrue(moves.contains(new Move(new Position(1, 4), new Position(2, 4))));
        // take diagonal left
        Piece opponent1 = new Piece(Piece.PieceType.ROOK, Piece.Color.BLACK, new Position(2, 3));
        board.addPiece(opponent1);
        moves = logic.getLegalMoves(pawn);
        assertEquals(moves.size(), 2);
        assertTrue(moves.contains(new Move(new Position(1, 4), new Position(2, 3))));
        // take diagonal right
        Piece opponent2 = new Piece(Piece.PieceType.ROOK, Piece.Color.BLACK, new Position(2, 5));
        board.addPiece(opponent2);
        moves = logic.getLegalMoves(pawn);
        assertEquals(moves.size(), 3);
        assertTrue(moves.contains(new Move(new Position(1, 4), new Position(2, 5))));
        // obstruction by friendly piece
        board.clearBoard();
        board.addPiece(pawn);
        Piece friendly1 = new Piece(Piece.PieceType.ROOK, Piece.Color.WHITE, new Position(2, 4));
        board.addPiece(friendly1);
        moves = logic.getLegalMoves(pawn);
        assertTrue(moves.isEmpty());
        // obstruction by friendly piece
        board.clearBoard();
        board.addPiece(pawn);
        Piece opponent3 = new Piece(Piece.PieceType.ROOK, Piece.Color.BLACK, new Position(2, 4));
        board.addPiece(opponent3);
        moves = logic.getLegalMoves(pawn);
        assertTrue(moves.isEmpty());
        // check that you cant take diagonally if diagonal is piece of same color
        Piece friendly2 = new Piece(Piece.PieceType.ROOK, Piece.Color.WHITE, new Position(2, 3));
        board.addPiece(friendly2);
        Piece friendly3 = new Piece(Piece.PieceType.BISHOP, Piece.Color.WHITE, new Position(2, 5));
        board.addPiece(friendly3);
        moves = logic.getLegalMoves(pawn);
        assertTrue(moves.isEmpty());

    }
    @Test
    @DisplayName("Test rooks legal moves")
    public void testRook() {
        // regular movement
        Piece rook = new Piece(Piece.PieceType.ROOK, Piece.Color.WHITE, new Position(4, 4));
        board.addPiece(rook);
        List<Move> moves = logic.getLegalMoves(rook);
        for (Move m : moves) assertEquals(m.getFrom(), rook.getPosition()); // tests that origin is the piece's position
        List<Position> destinations = toDestinations(moves);
        assertEquals(14, moves.size()); // rowLength + columnLength - overlap (1) - occupied (1)
        // horisontal movement
        for (int i = 0; i < 8; i++) {
            if (i != 4) assertTrue(destinations.contains(new Position(4, i)));
            else assertFalse(destinations.contains(new Position(4, i)));
        }
        // vertical movement
        for (int i = 0; i < 8; i++) {
            if (i != 4) assertTrue(destinations.contains(new Position(i, 4)));
            else assertFalse(destinations.contains(new Position(i, 4)));
        }
        // obstruction by enemy piece
        Piece opponent = new Piece(Piece.PieceType.QUEEN, Piece.Color.BLACK, new Position(4, 5));
        board.addPiece(opponent);
        moves = logic.getLegalMoves(rook);
        destinations = toDestinations(moves);
        assertTrue(destinations.contains(new Position(4, 5)));
        assertFalse(destinations.contains(new Position(4, 6)));

        // obstruction by friendly piece
        Piece friendly = new Piece(Piece.PieceType.QUEEN, Piece.Color.WHITE, new Position(3, 4));
        board.addPiece(friendly);
        moves = logic.getLegalMoves(rook);
        destinations = toDestinations(moves);
        assertFalse(destinations.contains(new Position(3, 4)));
        assertFalse(destinations.contains(new Position(2, 4)));
    }
    @Test
    @DisplayName("Test knights legal moves")
    public void testKnight() {
        Piece knight = new Piece(Piece.PieceType.KNIGHT, Piece.Color.WHITE, new Position(3, 4));
        board.addPiece(knight);
        List<Move> moves = logic.getLegalMoves(knight);
        for (Move m : moves) assertEquals(m.getFrom(), knight.getPosition()); // tests that origin is the piece's position
        List<Position> destinations = toDestinations(moves);
        List<Position> expectedDestinations = new ArrayList<Position>(Arrays.asList(new Position(1, 3), new Position(1, 5), new Position(2, 2), new Position(2, 6), new Position(4, 2), new Position(4, 6), new Position(5, 3), new Position(5, 5)));
        for (Position p : expectedDestinations) assertTrue(destinations.contains(p));
        assertEquals(expectedDestinations.size(), moves.size()); // since no moves is invalidated
        // ability to capture enemy piece
        Piece opponent = new Piece(Piece.PieceType.QUEEN, Piece.Color.BLACK, new Position(1, 3));
        board.addPiece(opponent);
        moves = logic.getLegalMoves(knight);
        destinations = toDestinations(moves);
        assertTrue(destinations.contains(new Position(1, 3)));
        // obstruction by friendly piece
        Piece friendly = new Piece(Piece.PieceType.QUEEN, Piece.Color.WHITE, new Position(2, 6));
        board.addPiece(friendly);
        moves = logic.getLegalMoves(knight);
        destinations = toDestinations(moves);
        assertEquals(7, moves.size());
        assertFalse(destinations.contains(new Position(2, 6)));
    }
    @Test
    @DisplayName("Test bishops legal moves")
    public void testBishop() {
        // regular movement
        Piece bishop = new Piece(Piece.PieceType.BISHOP, Piece.Color.WHITE, new Position(4, 2));
        board.addPiece(bishop);
        List<Move> moves = logic.getLegalMoves(bishop);
        for (Move m : moves) assertEquals(m.getFrom(), bishop.getPosition()); // tests that origin is the piece's position
        List<Position> destinations = toDestinations(moves);
        assertEquals(11, moves.size()); // amount of destinations tested below
        // up-diagonal movement
        for (int i = -2; i < 4; i++) {
            if (i != 0) assertTrue(destinations.contains(new Position(4+i, 2+i)));
            else assertFalse(destinations.contains(new Position(4+i, 2+i)));
        }
        // down-diagonal movement
        for (int i = -2; i < 5; i++) {
            if (i != 0) assertTrue(destinations.contains(new Position(4-i, 2+i)));
            else assertFalse(destinations.contains(new Position(4-i, 2+i)));
        }
        // obstruction by enemy piece
        Piece opponent = new Piece(Piece.PieceType.QUEEN, Piece.Color.BLACK, new Position(5, 3));
        board.addPiece(opponent);
        moves = logic.getLegalMoves(bishop);
        destinations = toDestinations(moves);
        assertTrue(destinations.contains(new Position(5, 3)));
        assertFalse(destinations.contains(new Position(6, 4)));
        // obstruction by friendly piece
        Piece friendly = new Piece(Piece.PieceType.QUEEN, Piece.Color.WHITE, new Position(5, 1));
        board.addPiece(friendly);
        moves = logic.getLegalMoves(bishop);
        destinations = toDestinations(moves);
        assertFalse(destinations.contains(new Position(5, 1)));
        assertFalse(destinations.contains(new Position(6, 0)));
    }
    @Test
    @DisplayName("Test queens legal moves")
    public void testQueen() {
        // regular movement
        Piece queen = new Piece(Piece.PieceType.QUEEN, Piece.Color.WHITE, new Position(3, 3));
        board.addPiece(queen);
        List<Move> moves = logic.getLegalMoves(queen);
        for (Move m : moves) assertEquals(m.getFrom(), queen.getPosition()); // tests that origin is the piece's position
        List<Position> destinations = toDestinations(moves);
        assertEquals(27, moves.size()); // size of rook destinations + size of bishop destinations (would be 13 in center positon), since queen effectively is both at once
        // horisontal movement
        for (int i = 0; i < 8; i++) {
            if (i != 3) assertTrue(destinations.contains(new Position(3, i)));
            else assertFalse(destinations.contains(new Position(3, i)));
        }
        // vertical movement
        for (int i = 0; i < 8; i++) {
            if (i != 3) assertTrue(destinations.contains(new Position(i, 3)));
            else assertFalse(destinations.contains(new Position(i, 3)));
        }
        // up-diagonal movement
        for (int i = -3; i < 5; i++) {
            if (i != 0) assertTrue(destinations.contains(new Position(3+i, 3+i)));
            else assertFalse(destinations.contains(new Position(3+i, 3+i)));
        }
        // down-diagonal movement
        for (int i = -3; i < 4; i++) {
            if (i != 0) assertTrue(destinations.contains(new Position(3-i, 3+i)));
            else assertFalse(destinations.contains(new Position(3-i, 3+i)));
        }
        // obstruction by enemy piece
        Piece opponent = new Piece(Piece.PieceType.PAWN, Piece.Color.BLACK, new Position(2, 2));
        board.addPiece(opponent);
        moves = logic.getLegalMoves(queen);
        destinations = toDestinations(moves);
        assertTrue(destinations.contains(new Position(2, 2)));
        assertFalse(destinations.contains(new Position(1, 1)));
        // obstruction by friendly piece
        Piece friendly = new Piece(Piece.PieceType.PAWN, Piece.Color.WHITE, new Position(3, 4));
        board.addPiece(friendly);
        moves = logic.getLegalMoves(queen);
        destinations = toDestinations(moves);
        assertFalse(destinations.contains(new Position(3, 4)));
        assertFalse(destinations.contains(new Position(3, 5)));
    }
    @Test
    @DisplayName("Test kings legal moves")
    public void testKing() {
        // regular movement
        Piece king = new Piece(Piece.PieceType.KING, Piece.Color.WHITE, new Position(3, 3));
        board.addPiece(king);
        List<Move> moves = logic.getLegalMoves(king);
        for (Move m : moves) assertEquals(m.getFrom(), king.getPosition()); // tests that origin is the piece's position
        List<Position> destinations = toDestinations(moves);
        assertEquals(8, moves.size()); // all surrounding positions
        assertTrue(destinations.contains(new Position(2, 2)));
        assertTrue(destinations.contains(new Position(2, 3)));
        assertTrue(destinations.contains(new Position(2, 4)));
        assertTrue(destinations.contains(new Position(3, 2)));
        assertTrue(destinations.contains(new Position(3, 4)));
        assertTrue(destinations.contains(new Position(4, 2)));
        assertTrue(destinations.contains(new Position(4, 3)));
        assertTrue(destinations.contains(new Position(4, 4)));
        // ability to capture enemy piece
        Piece opponent = new Piece(Piece.PieceType.QUEEN, Piece.Color.BLACK, new Position(2, 3));
        board.addPiece(opponent);
        moves = logic.getLegalMoves(king);
        destinations = toDestinations(moves);
        assertTrue(destinations.contains(new Position(2, 3)));
        // obstruction by friendly piece
        Piece friendly = new Piece(Piece.PieceType.QUEEN, Piece.Color.WHITE, new Position(4, 3));
        board.addPiece(friendly);
        moves = logic.getLegalMoves(king);
        destinations = toDestinations(moves);
        assertEquals(7, moves.size());
        assertFalse(destinations.contains(new Position(4, 3)));
    }
    @Test
    @DisplayName("Testing that castling is displayed with valid prerequisites")
    public void testCastling() {
        Piece king = new Piece(Piece.PieceType.KING, Piece.Color.WHITE, new Position(0, 4));
        board.addPiece(king);
        Piece rook1 = new Piece(Piece.PieceType.ROOK, Piece.Color.WHITE, new Position(0, 0));
        board.addPiece(rook1);
        // castling to the left
        List<Move> moves = logic.getLegalMoves(king);
        for (Move m : moves) assertEquals(m.getFrom(), king.getPosition()); // tests that origin is the piece's position
        List<Position> destinations = toDestinations(moves);
        assertEquals(6, moves.size()); // all surrounding positions (5) + the castling
        // checks that the move is valid with valid prerequistes
        assertTrue(destinations.contains(new Position(0, 2)));
        // asserts that the move is invalid if either King or Rook has moved before
        king.setFirstTurnMoved(1);
        moves = logic.getLegalMoves(king);
        destinations = toDestinations(moves);
        assertFalse(destinations.contains(new Position(0, 2)));
        rook1.setFirstTurnMoved(1);
        moves = logic.getLegalMoves(king);
        destinations = toDestinations(moves);
        assertFalse(destinations.contains(new Position(0, 2)));
        king.setFirstTurnMoved(-1);
        moves = logic.getLegalMoves(king);
        destinations = toDestinations(moves);
        assertFalse(destinations.contains(new Position(0, 2)));
        rook1.setFirstTurnMoved(-1);
        // castling to the right
        Piece rook2 = new Piece(Piece.PieceType.ROOK, Piece.Color.WHITE, new Position(0, 7));
        board.addPiece(rook2);
        moves = logic.getLegalMoves(king);
        for (Move m : moves) assertEquals(m.getFrom(), king.getPosition()); // tests that origin is the piece's position
        destinations = toDestinations(moves);
        assertEquals(7, moves.size()); // both castles + surronding moves (5)
        assertTrue(destinations.contains(new Position(0, 6)));
        // asserts that the move is invalid if the path between King and Rook is obstructed
            // * by opposing piece
            Piece opponent = new Piece(Piece.PieceType.ROOK, Piece.Color.BLACK, new Position(0, 6));
            board.addPiece(opponent);
            moves = logic.getLegalMoves(king);
            destinations = toDestinations(moves);
            assertEquals(6, moves.size());
            assertFalse(destinations.contains(new Position(0, 6)));
            // * by friendly piece
            Piece friendly = new Piece(Piece.PieceType.BISHOP, Piece.Color.WHITE, new Position(0, 1));
            board.addPiece(friendly);
            moves = logic.getLegalMoves(king);
            destinations = toDestinations(moves);
            assertEquals(5, moves.size());
            assertFalse(destinations.contains(new Position(0, 2)));
    }
}