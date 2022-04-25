package chess.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import chess.datamodel.ChessBoard;

// tests all the pieces, instead of creating a test for each OfficerLogic child class individually
public class PieceLogicTest {
    private ChessBoard board;
    private PieceLogic logic;
    @BeforeEach
    public void setUp() {
        board = new ChessBoard();
        logic = new PieceLogic(board);
    }
    @Test
    @DisplayName("Tester pawns basic movement")
    public void testPawnForward() {

    }
    @Test
    @DisplayName("Tester pawns basic movement")
    public void testPawnDiagonal() {

    }
    @Test
    @DisplayName("Tester")
    public void testRook() {

    }
    @Test
    @DisplayName("Tester")
    public void testKnight() {
        
    }
    @Test
    @DisplayName("Tester")
    public void testBishop() {
        
    }
    @Test
    @DisplayName("Tester")
    public void testQueen() {
        
    }
    @Test
    @DisplayName("Tester")
    public void testKing() {
        
    }
    @Test
    @DisplayName("Testing that castling is displayed with valid prerequisites")
    public void testCastling() {
        // checks that the move is valid with valid prerequistes

        // asserts that the move is invalid if either King or Rook has moved before

        // asserts that the move is invalid if the path between King and Rook is obstructed

    }
}
