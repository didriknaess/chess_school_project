package chess.datamodel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import chess.datamodel.Piece.Color;
import chess.datamodel.Piece.PieceType;

public class PieceTest {

    Piece piece;

    @BeforeEach
    public void setup() {
        piece = new Piece();
    }


    @Test
    @DisplayName("Test the static method createNewPiece")
    public void testCreateNewPiece() {
        Piece expectedPiece = new Piece(PieceType.PAWN, Color.WHITE, new Position("a1"));
        Piece actualPiece = Piece.createNewPiece("Pa1");
        assertEquals(expectedPiece.getColor(), actualPiece.getColor());
        assertEquals(expectedPiece.getPosition(), actualPiece.getPosition());
        assertEquals(expectedPiece.getType(), actualPiece.getType());
        assertEquals(-1, actualPiece.getFirstTurnMoved(),
        "-1 indicates the piece is yet to move");
        assertThrows( IllegalArgumentException.class, () -> {
            Piece.createNewPiece("Px5");
        }, "Giving illegal coords should throw an exception, illegal piece is handled by private method setTypeAndColor()"
        );
        assertThrows( IllegalArgumentException.class, () -> {
            Piece.createNewPiece("Xp5");
        }, "Illegal piece is handled by private method setTypeAndColor()"
        );
    }

    @Test
    @DisplayName("Test setters and getters")
    public void testSetTypeAndColor() {
        Position pos = new Position("a5");
        this.piece.setFirstTurnMoved(6);
        this.piece.setPosition(pos);
        assertEquals(pos, this.piece.getPosition());
        assertEquals(6, this.piece.getFirstTurnMoved());
        assertEquals(null, this.piece.getColor(),
        "Piece has no color, no way to set a color either by design");
        this.piece = Piece.createNewPiece("Pa4");
        assertEquals(Color.WHITE, this.piece.getColor());
        assertThrows(IllegalArgumentException.class, () -> {
            this.piece.setPosition(new Position("x3"));
        }
        );
    }

    @Test
    @DisplayName("Test moveTo() method")
    public void testMoveTo() {
        this.piece.setPosition(new Position("a4"));
        Position expectedPosition = new Position("c5");
        this.piece.moveTo(new Position("c5"));
        assertEquals(expectedPosition, this.piece.getPosition());
        assertThrows(IllegalArgumentException.class, () -> {
            this.piece.moveTo(new Position("x4"));
        }, "When trying to move to an illegal position, IllegalArgumentException should be thrown"
        );
    }

}
