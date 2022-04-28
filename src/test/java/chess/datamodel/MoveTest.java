package chess.datamodel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MoveTest {
    private Position from;
    private Position to;
    private Move move;
    // the Move class does not have any exceptions of its own, rather it utilizes the ones from Piece and Position
    @BeforeEach
    public void setup() {
        from = new Position(0, 0);
        to = new Position(1,1);
        move = new Move(from, to);
    }
    @Test
    public void testGetters() {
        assertEquals(new Position(0,0), move.getFrom());
        assertEquals(new Position(1,1), move.getTo());
    }
    @Test
    public void testEquals() {
        assertTrue(move.equals(new Move(new Position(0,0), new Position(1,1))));
        assertFalse(move.equals(new Move(new Position(1,0), new Position(1,1))));
    }
    @Test
    public void testCompareTo() {
        assertEquals(0, move.compareTo(new Move(new Position(0,0), new Position(1,1))));
        assertEquals(-1, move.compareTo(new Move(new Position(1,0), new Position(1,1))));
    } 
}
