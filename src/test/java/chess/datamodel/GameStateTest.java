package chess.datamodel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import chess.datamodel.Piece.Color;

public class GameStateTest {
    private GameState game;

    @BeforeEach
    public void init() {
        this.game = new GameState();
    }
    @Test
    @DisplayName("Test that a valid game contains some pieces")
    public void testValidGameState() {
        Piece piece1 = Piece.createNewPiece("pa1");
        assertFalse(game.isValid());
        this.game.addPiece(piece1);
        assertTrue(game.isValid(),
        "Only a valid GameState if it contains some pieces (after design)");
    }
    @Test
    @DisplayName("Test whose turn it is")
    public void testWhoseTurn() {
        this.game.setWhoseTurn(Color.BLACK);
        assertEquals(Color.BLACK, this.game.getWhoseTurn(),
        "This should return an enum of the type 'BLACK'");
        assertEquals("black", this.game.savingGetWhoseTurn(),
        "To help make it easier to save the game, should get a String 'black'");
    }
    @Test
    @DisplayName("Test number of total turns, starting at 1")
    public void testTurns() {
        assertEquals(1, this.game.getNumberOfTurns(),
        "Number of turns start at 1 (because of controller and gamelogic)");
        this.game.setTurns(3);
        assertEquals(3, this.game.getNumberOfTurns());
        this.game.addTurn();
        assertEquals(4, this.game.getNumberOfTurns());
        this.game.removeTurn();
        assertEquals(3, this.game.getNumberOfTurns());
    }
    @Test
    @DisplayName("Test how many seconds each has got left")
    public void testSecondsRemaining() {
        this.game.setSecondsRemainingBlack(100);
        this.game.setSecondsRemainingWhite(90);
        assertEquals(100, this.game.getBlackSeconds());
        assertEquals(90, this.game.getWhiteSeconds());
        assertThrows(
            IllegalArgumentException.class, () -> {
                this.game.setSecondsRemainingWhite(-1);
            }, "Test that you can't have negative time"
        );
    }
    @Test
    @DisplayName("Test the functionality of the list of active pieces")
    public void testPieces() {
        Piece piece1 = Piece.createNewPiece("pa1");
        Piece piece2 = Piece.createNewPiece("pa2");
        Piece piece3 = Piece.createNewPiece("pa3");
        Piece piece4 = Piece.createNewPiece("pa4");
        Piece piece5 = Piece.createNewPiece("pa5");
        ArrayList<Piece> expectedPieces = new ArrayList<>(
            Arrays.asList(piece1, piece2, piece3, piece4, piece5));
        assertEquals(new ArrayList<>(), this.game.getPieces(),
        "Should be an empty ArrayList, not null");
        this.game.addPiece(piece1);
        this.game.addPiece(piece2);
        this.game.addPiece(piece3);
        this.game.addPiece(piece4);
        this.game.addPiece(piece5);
        assertEquals(5, this.game.getPieces().size());
        assertEquals(expectedPieces, this.game.getPieces());
        this.game.clearPieces();
        assertEquals(new ArrayList<>(), this.game.getPieces(),
        "Should be an empty ArrayList, not null");
    }
    @Test
    @DisplayName("Test the history of moves (used for undo)")
    public void testMoveHistory() {
        Stack<Move> actualMoveHistory = new Stack<>();
        Move move1 = new Move(new Position("a1"), new Position("a2"));
        Move move2 = new Move(new Position("e4"), new Position("e1"));
        Move move3 = new Move(new Position("b5"), new Position("c7"));
        actualMoveHistory.add(move1);
        actualMoveHistory.add(move2);
        actualMoveHistory.add(move3);
        this.game.addMove(move1);
        this.game.addMove(move2);
        this.game.addMove(move3);
        assertEquals(actualMoveHistory, this.game.getMoveHistory());
        actualMoveHistory.pop();
        this.game.popMove();
        assertEquals(actualMoveHistory, this.game.getMoveHistory());
    }
    @Test
    @DisplayName("Test the map of pieces that has been captured")
    public void testCapturedPieces() {
        HashMap<Integer, Piece> expectedCapturedPieces = new HashMap<>();
        Piece piece1 = Piece.createNewPiece("pa1");
        Piece piece2 = Piece.createNewPiece("pa2");
        Piece piece3 = Piece.createNewPiece("pa3");

        expectedCapturedPieces.put(1, piece1);
        this.game.addCapturedPiece(piece1);
        this.game.setTurns(4);
        expectedCapturedPieces.put(4, piece2);
        this.game.addCapturedPiece(piece2);
        this.game.addTurn();
        expectedCapturedPieces.put(5, piece3);
        this.game.addCapturedPiece(piece3);
        assertEquals(expectedCapturedPieces, this.game.getCapturedPieces());
        expectedCapturedPieces.remove(5);
        this.game.removeCapturedPiece(5);
        assertEquals(expectedCapturedPieces, this.game.getCapturedPieces());
        assertThrows(IllegalArgumentException.class, () -> {
            this.game.removeCapturedPiece(2);
        }, "No piece was removed this turn, should throw IllegalArgumentException"
        );
    }
    @Test
    @DisplayName("Test the list of promoted pawns (for undo)")
    public void testPromotedPawns() {
        HashMap<Integer, Piece> expectedPromotedPawns = new HashMap<>();
        Piece pawn1 = Piece.createNewPiece("pa1");
        Piece pawn2 = Piece.createNewPiece("pa2");
        Piece pawn3 = Piece.createNewPiece("pa3");
        Piece king = Piece.createNewPiece("ka4");

        expectedPromotedPawns.put(1, pawn1);
        this.game.addPromotedPawn(1, pawn1);
        expectedPromotedPawns.put(2, pawn2);
        this.game.addPromotedPawn(2, pawn2);
        expectedPromotedPawns.put(3, pawn3);
        this.game.addPromotedPawn(3, pawn3);
        assertEquals(expectedPromotedPawns, this.game.getPromotedPawns());
        expectedPromotedPawns.remove(1);
        this.game.removePromotedPawn(1);
        assertEquals(expectedPromotedPawns, this.game.getPromotedPawns());
        assertEquals(pawn2, this.game.getPromotedPawn(2));
        assertThrows(IllegalArgumentException.class, () -> {
            this.game.addPromotedPawn(4, king);
        }, "Should throw an exception when trying to promote a piece that is not a pawn"
        );
    }
}
