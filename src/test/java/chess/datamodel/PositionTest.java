package chess.datamodel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PositionTest {

    private Position position;
    
    @BeforeEach
    public void setup() {
        this.position = new Position(0,0);
    }
    @Test
    public void createNewValidPositionString() {
        //Test creating new position using string, and comparing it to one using integers
        Position stringPosition = new Position("a1");
        assertEquals(stringPosition.compareTo(this.position), 0, 
        "When comparing these two, the result should be 0 (equal)");
    }
    @Test
    public void testGetters() {
        assertEquals(this.position.getColumn(), 0);
        assertEquals(this.position.getRow(), 0);
        assertEquals(this.position.getPosString(), "a1");
        assertEquals(this.position.getNewPosition(3, 4), new Position(3,4));
        assertEquals(this.position, new Position(0,0), 
        "The previous test should not affect the original position");
    }
    @Test
    public void createNewInvalidPosition() {
        assertThrows(
            IllegalArgumentException.class, () -> {
                new Position("j3");
            }, "Invalid row in string, IllegalArugmentException should be thrown"
        );
        assertThrows(
            IllegalArgumentException.class, () -> {
                new Position("a9");
            }, "Invalid column in string, IllegalArugmentException should be thrown"
        );
        assertThrows(
            IllegalArgumentException.class, () -> {
                new Position("aa1");
            }, "Invalid length of string, IllegalArugmentException should be thrown"
        );
    }
}