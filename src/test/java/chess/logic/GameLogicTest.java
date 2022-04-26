package chess.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import chess.datamodel.Move;
import chess.datamodel.Piece;
import chess.datamodel.Position;

public class GameLogicTest {
    private GameLogic logic;
    @BeforeEach
    public void setUp() {
        logic = new GameLogic();
        // we can assume this works as a result of testing the io:
        try {
            logic.loadGame("NormalChess.txt");
        } catch (Exception FileNotFoundException) {
        }
        logic.setTimers(300);
    }
    // We can assume that all functionality delegated to other classes, for example to GameState, ChessBoard og PieceLogic, will work
    // because of their own respetive tests. These delegating methods must therefore not neccesarily be tested here, but there may
    // however still be a need to test that the delegation works as intended. Therefore some tests will overlap, while others won't. 
    @Test
    @DisplayName("Tests whoseTurn() & endTurn()")
    public void testEndTurn() {
        // testing that whoseTurn() gives correct output after standard initialization:
        assertEquals(Piece.Color.WHITE, logic.whoseTurn());
        assertTrue(logic.isWhitePlaying());
        assertEquals(1, logic.getTurnCount());
        // testing that whoseTurn() gives correct output after ending the turn: 
        logic.endTurn();
        assertEquals(Piece.Color.BLACK, logic.whoseTurn());
        assertFalse(logic.isWhitePlaying());
        assertEquals(2, logic.getTurnCount());
    }
    @Test
    @DisplayName("Tests inCheck(Color), and by extension the internal method isThreatened(Position)")
    public void testInCheck() {
        assertFalse(logic.inCheck(Piece.Color.WHITE));
        assertFalse(logic.inCheck(Piece.Color.BLACK));
        logic.move(new Move(new Position(1, 5), new Position(2, 5)));;
        logic.endTurn();
        logic.move(new Move(new Position(6, 4), new Position(4, 4)));;
        logic.endTurn();
        logic.move(new Move(new Position(1, 6), new Position(3, 6)));;
        logic.endTurn();
        logic.move(new Move(new Position(7, 3), new Position(3, 7)));;
        logic.endTurn();
        assertTrue(logic.inCheck(Piece.Color.WHITE));
        assertFalse(logic.inCheck(Piece.Color.BLACK));
    }
    @Test
    @DisplayName("Tests get valid moves")
    public void testGetValidMoves() {
        // regular movement
        Piece wKnight = logic.getPiece(new Position(0, 6)); // right white knight
        List <Move> moves = logic.getValidMoves(wKnight);
        List<Position> expectedDestinations = new ArrayList<>(Arrays.asList(new Position(2, 5), new Position(2, 7)));
        assertEquals(2, moves.size());
        for (Move m : moves) assertTrue(expectedDestinations.contains(m.getTo()));
        // setting up for later tests
        logic.move(new Move(new Position(1, 2), new Position(2, 2)));;
        logic.endTurn();
        logic.move(new Move(new Position(6, 4), new Position(4, 4)));;
        logic.endTurn();
        logic.move(new Move(new Position(0, 3), new Position(3, 1)));;
        logic.endTurn();
        logic.move(new Move(new Position(7, 3), new Position(3, 7)));;
        logic.endTurn();
        // moving into check (in this case black)
        Piece bKing = logic.getPiece(new Position(7, 4));
        moves = logic.getValidMoves(bKing);
        List<Position> destinations = new ArrayList<Position>();
        for (Move m : moves) destinations.add(m.getTo());
        assertFalse(destinations.contains(new Position(6, 4)));
        // moving a piece that puts you into check (in this case white)
        logic.move(new Move(new Position(6, 0), new Position(5, 0)));;
        logic.endTurn();
        Piece wPawn = logic.getPiece(new Position(0, 5));
        moves = logic.getValidMoves(wPawn);
        assertEquals(0, moves.size());

    }
    @Test
    @DisplayName("Tests move()")
    // this method basically just tests that the merging of different delegated methods (to gamestate and chessboard) works correctly
    public void testMove() {
        // normal move (first turn move)
        Piece wPawn = logic.getPiece(new Position(1, 3));
        logic.move(new Move(wPawn.getPosition(), new Position(3, 3)));
        logic.endTurn();
        Piece bPawn = logic.getPiece(new Position(6, 4));
        logic.move(new Move(bPawn.getPosition(), new Position(4, 4)));
        logic.endTurn();
        // ensures that the pieces new and former postion was updated 
        assertEquals(null, logic.getPiece(new Position(1, 3)));
        assertEquals(wPawn, logic.getPiece(new Position(3, 3)));
        assertEquals(null, logic.getPiece(new Position(6, 4)));
        assertEquals(bPawn, logic.getPiece(new Position(4, 4)));
        // ensures that the Piece's position fields was updated
        assertEquals(new Position(3, 3), wPawn.getPosition());
        assertEquals(new Position(4, 4), bPawn.getPosition());
        // ensures that the Piece's firstTurnMoved field was updated
        assertEquals(1, wPawn.getFirstTurnMoved());
        assertEquals(2, bPawn.getFirstTurnMoved());
        // capture
        logic.move(new Move(wPawn.getPosition(), bPawn.getPosition()));
        logic.endTurn();
        assertEquals(null, logic.getPiece(new Position(3, 3)));
        assertEquals(wPawn, logic.getPiece(new Position(4, 4)));
        // setup for castling
        setUp();
        logic.move(new Move(new Position(1, 6), new Position(3, 6)));
        logic.move(new Move(new Position(6, 3), new Position(4, 3)));
        logic.move(new Move(new Position(0, 5), new Position(2, 7)));
        logic.move(new Move(new Position(7, 2), new Position(5, 4)));
        logic.move(new Move(new Position(0, 6), new Position(2, 5)));
        logic.move(new Move(new Position(7, 3), new Position(5, 3)));
        logic.move(new Move(new Position(7, 1), new Position(5, 2)));
        // castling (short, right)
        Piece king = logic.getPiece(new Position(0, 4));
        Piece rook = logic.getPiece(new Position(0, 7));
        logic.move(new Move(king.getPosition(), new Position(0, 6)));
        assertEquals(null, logic.getPiece(new Position(0, 4)));
        assertEquals(null, logic.getPiece(new Position(0, 7)));
        assertEquals(king, logic.getPiece(new Position(0, 6)));
        assertEquals(rook, logic.getPiece(new Position(0, 5)));
        // castling (long, left)
        king = logic.getPiece(new Position(7, 4));
        rook = logic.getPiece(new Position(7, 0));
        logic.move(new Move(king.getPosition(), new Position(7, 2)));
        assertEquals(null, logic.getPiece(new Position(7, 4)));
        assertEquals(null, logic.getPiece(new Position(7, 0)));
        assertEquals(king, logic.getPiece(new Position(7, 2)));
        assertEquals(rook, logic.getPiece(new Position(7, 3)));
    }
    @Test
    @DisplayName("Tests promotion of pawns who has reached the end")
    public void testPromotion() {
        // moving the pawn into a spot where promotion is possible
        logic.move(new Move(new Position(1, 6), new Position(3, 6)));
        logic.move(new Move(new Position(3, 6), new Position(4, 6)));
        logic.move(new Move(new Position(4, 6), new Position(5, 6)));
        logic.move(new Move(new Position(5, 6), new Position(6, 5)));
        Position pos = new Position(7, 6);
        Piece pawn = logic.getPiece(new Position(6, 5));
        Piece kngiht = logic.getPiece(new Position(7, 6));
        logic.move(new Move(new Position(6, 5), pos));
        // actually promoting the piece...
        // ..to a queen
        logic.promote(pos, Piece.PieceType.QUEEN);
        assertEquals(Piece.PieceType.QUEEN, logic.getPiece(pos).getType());
        // testing undoing a promotion
        logic.undo(true);
        assertEquals(kngiht, logic.getPiece(pos));
        assertEquals(pawn, logic.getPiece(new Position(6, 5)));
        // ..to a rook
        logic.move(new Move(new Position(6, 5), pos));
        logic.promote(pos, Piece.PieceType.ROOK);
        assertEquals(Piece.PieceType.ROOK, logic.getPiece(pos).getType());
        logic.undo(true);
        // ..to a bishop
        logic.move(new Move(new Position(6, 5), pos));
        logic.promote(pos, Piece.PieceType.BISHOP);
        assertEquals(Piece.PieceType.BISHOP, logic.getPiece(pos).getType());
        logic.undo(true);
        // ..to a knight
        logic.move(new Move(new Position(6, 5), pos));
        logic.promote(pos, Piece.PieceType.KNIGHT);
        assertEquals(Piece.PieceType.KNIGHT, logic.getPiece(pos).getType());
        logic.undo(true);
        // ..to a king (should not work)
        logic.move(new Move(new Position(6, 5), pos));
        assertThrows(IllegalArgumentException.class, () -> logic.promote(pos, Piece.PieceType.KING));
        // ..to a pawn (should not work)
        assertThrows(IllegalArgumentException.class, () -> logic.promote(pos, Piece.PieceType.PAWN));
        // promoting a tile where there is no pawn (this position contains a rook), this should not work
        assertThrows(IllegalArgumentException.class, () -> logic.promote(new Position(0, 0), Piece.PieceType.QUEEN));
    }
    @Test
    @DisplayName("Tests undo(boolean), with the exception of undoing promotion (which is tested above)")
    public void testUndo() {
        // undoing on turn 1 should do throw an EmptyStackException
        assertThrows(EmptyStackException.class, () -> logic.undo(true));
        assertThrows(EmptyStackException.class, () -> logic.undo(false));
        // this should be bypassed in the controller by deactivating the undo button if the moveHistory is empty

        // setting up for later tests:
        logic.move(new Move(new Position(1, 6), new Position(3, 6)));
        logic.move(new Move(new Position(6, 3), new Position(4, 3)));
        logic.move(new Move(new Position(0, 5), new Position(2, 7)));
        logic.move(new Move(new Position(7, 2), new Position(5, 4)));
        logic.move(new Move(new Position(0, 6), new Position(2, 5)));
        logic.move(new Move(new Position(7, 3), new Position(5, 3)));
        logic.move(new Move(new Position(3, 6), new Position(4, 6)));
        logic.move(new Move(new Position(7, 1), new Position(5, 2)));
        // external undo (for calls from the player in the controller)
        Piece pawn = logic.getPiece(new Position(6, 0));
        logic.move(new Move(new Position(6, 0), new Position(4, 0)));
        logic.endTurn();
        logic.undoTurn();
        assertEquals(pawn, logic.getPiece(new Position(6, 0)));
        assertEquals(null, logic.getPiece(new Position(4, 0)));
        // undoing into starting position, should update piece's firstTurnMoved field to -1
        assertEquals(-1, logic.getPiece(new Position(6, 0)).getFirstTurnMoved());
        // internal undo (for checking if a move puts the player in check)
        pawn = logic.getPiece(new Position(6, 0));
        logic.move(new Move(new Position(6, 0), new Position(5, 0)));
        logic.undo(true);
        assertEquals(pawn, logic.getPiece(new Position(6, 0)));
        assertEquals(null, logic.getPiece(new Position(5, 0)));
        // undoing into starting position, should update piece's firstTurnMoved field to -1
        assertEquals(-1, logic.getPiece(new Position(6, 0)).getFirstTurnMoved());
        // undoing a capture should place the captured piece back on the board (external)
        logic.move(new Move(new Position(2, 5), new Position(3, 3)));
        Piece wKnight = logic.getPiece(new Position(5, 2));
        Piece bKnight = logic.getPiece(new Position(3, 3));
        logic.move(new Move(new Position(5, 2), new Position(3, 3)));
        logic.endTurn();
        logic.undoTurn();
        assertEquals(wKnight, logic.getPiece(new Position(5, 2)));
        assertEquals(bKnight, logic.getPiece(new Position(3, 3)));
        logic.undo(true);
        // undoing a capture should place the captured piece back on the board (internal)
        Piece wBishop = logic.getPiece(new Position(2, 7));
        Piece bBishop = logic.getPiece(new Position(5, 4));
        logic.move(new Move(new Position(2, 7), new Position(5, 4)));
        logic.undo(true);
        assertEquals(wBishop, logic.getPiece(new Position(2, 7)));
        assertEquals(bBishop, logic.getPiece(new Position(5, 4)));
        // undoing a castling (external)
        Piece wKing = logic.getPiece(new Position(0, 4));
        Piece wRook = logic.getPiece(new Position(0, 7));
        logic.move(new Move(new Position(0, 4), new Position(0, 6)));
        logic.endTurn();
        logic.undoTurn();
        assertEquals(wKing, logic.getPiece(new Position(0, 4)));
        assertEquals(wRook, logic.getPiece(new Position(0, 7)));
        assertEquals(null, logic.getPiece(new Position(0, 6)));
        assertEquals(null, logic.getPiece(new Position(0, 5)));
        // undoing a castling (internal)
        Piece bKing = logic.getPiece(new Position(7, 4));
        Piece bRook = logic.getPiece(new Position(7, 0));
        logic.move(new Move(new Position(7, 4), new Position(7, 2)));
        logic.undo(true);
        assertEquals(bKing, logic.getPiece(new Position(7, 4)));
        assertEquals(bRook, logic.getPiece(new Position(7, 0)));
        assertEquals(null, logic.getPiece(new Position(7, 2)));
        assertEquals(null, logic.getPiece(new Position(7, 3)));
    }
    @Test
    @DisplayName("Tests undoTurn() for external undoing of whole turns.")
    public void testUndoTurn() {
        // the actual undoing is tested above, here we only need to test the GameState changes
        assertEquals(Piece.Color.WHITE, logic.whoseTurn());
        boolean isWhitePlaying = logic.isWhitePlaying();
        int turnCount = logic.getTurnCount();
        logic.move(new Move(new Position(1, 4), new Position(3, 4)));
        logic.endTurn();
        logic.undoTurn();
        // update whose turn it is
        assertTrue(isWhitePlaying == logic.isWhitePlaying());
        // decrements turn count
        assertEquals(turnCount, logic.getTurnCount());
    }
    @Test
    @DisplayName("Tests noValidMoves")
    public void testNoValidMoves() {
        assertFalse(logic.noValidMoves(Piece.Color.BLACK));
        assertFalse(logic.noValidMoves(Piece.Color.WHITE));
        // putting white in mate
        logic.move(new Move(new Position(1, 5), new Position(2, 5)));;
        logic.endTurn();
        logic.move(new Move(new Position(6, 4), new Position(4, 4)));;
        logic.endTurn();
        logic.move(new Move(new Position(1, 6), new Position(3, 6)));;
        logic.endTurn();
        logic.move(new Move(new Position(7, 3), new Position(3, 7)));;
        logic.endTurn();

        assertFalse(logic.noValidMoves(Piece.Color.BLACK));
        assertTrue(logic.noValidMoves(Piece.Color.WHITE));
    }
    @Test
    @DisplayName("Tests getScore(Color)")
    public void testGetScore() {
        // makes white take one of each piece
        // for each step, ensures that the correct score for each player was calculated
        // scoring is calculated as follows: pawn: 1, rook: 5, knight: 3, bishop: 3, queen: 9, king: undefined/0 (should never be taken because of checkmate rules)
        logic.move(new Move(new Position(1, 4), new Position(2, 4)));
        logic.endTurn();
        assertEquals(0, logic.getScore(Piece.Color.WHITE));
        logic.move(new Move(new Position(0, 3), new Position(3, 6)));
        logic.endTurn();
        assertEquals(0, logic.getScore(Piece.Color.WHITE));
        logic.move(new Move(new Position(3, 6), new Position(6, 3)));
        logic.endTurn();
        assertEquals(1, logic.getScore(Piece.Color.WHITE));
        logic.move(new Move(new Position(6, 3), new Position(7, 3)));
        logic.endTurn();
        assertEquals(10, logic.getScore(Piece.Color.WHITE));
        logic.move(new Move(new Position(7, 3), new Position(7, 2)));
        logic.endTurn();
        assertEquals(13, logic.getScore(Piece.Color.WHITE));
        logic.move(new Move(new Position(7, 2), new Position(7, 1)));
        logic.endTurn();
        assertEquals(16, logic.getScore(Piece.Color.WHITE));
        logic.move(new Move(new Position(7, 1), new Position(7, 0)));
        logic.endTurn();
        assertEquals(21, logic.getScore(Piece.Color.WHITE));
    }
}
