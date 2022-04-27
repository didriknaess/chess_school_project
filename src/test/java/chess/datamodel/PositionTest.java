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
        // assertThrows(
        //     IllegalArgumentException.class, () -> {
        //     this.position.getNewPosition(9, 1);
        // }, "Test when trying to get a new illegal position, an IllegalArgumentException is thrown"
        // );
    }

    @Test
    public void createNewInvalidPosition() {
        //Test creating invalid position using row and column, makes the game not launch
        // assertThrows(
        //     IllegalArgumentException.class, () -> {
        //         new Position(-2, -2);
        //     }, "IllegalArgumentException should be thrown when creating an invalid position"
        // );
        
        //Test creating invalid position using string
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

    // @Test
    // public void setCaptured() {
    //     this.position.setCaptured();
    //     assertEquals(this.position.getColumn(), -1);
    //     assertEquals(this.position.getRow(), -1);
    // }

    // @Test
    // public void testMoveTo() {
    //     this.position.moveTo(this.position.getNewPosition(4, 4));
    //     assertEquals(this.position, new Position(4,4));
    //     assertThrows(
    //         IllegalArgumentException.class, () -> {
    //             this.position.moveTo(new Position(9,1));
    //         }, "Cannot move to an invalid position"
    //         );
    //     this.position.setCaptured();
    //     assertThrows(
    //         IllegalArgumentException.class, () -> {
    //             this.position.moveTo(new Position(2,1));
    //         }, "Cannot move a piece that is not in play"
    //         );
    // }
}
