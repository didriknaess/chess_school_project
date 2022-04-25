package chess.datamodel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ChessTimerTest {
    private ChessTimer timer; 

    private int time = 600;
    @BeforeEach
    public void setUp() {
        this.timer = new ChessTimer(this.time);
    }
    @Test
    @DisplayName("Testing that getting the remaining time is correct")
    public void testGetRemainingTime() {
        // checks that the method returns correct values after different initializations of the object
        assertEquals(this.time*10, timer.getRemainingTime());
        this.timer = new ChessTimer(400);
        assertEquals(400*10, timer.getRemainingTime());
        this.timer = new ChessTimer(800);
        assertEquals(800*10, timer.getRemainingTime());
        // checks if the timer returns the correct amount after 1 second:
        timer.startTimer();
        try {
            Thread.sleep(1000);
        } catch (Exception InterruptedException) {
        }
        timer.pauseTimer();
        assertEquals(8000-10, timer.getRemainingTime());
    }
    @Test
    @DisplayName("Testing that the main thread returns correct information about whether the timer is running or not")
    public void testIsRunning() {
        assertFalse(timer.isRunning());
        timer.startTimer();
        assertTrue(timer.isRunning());
        try {
            Thread.sleep(1000);
        } catch (Exception InterruptedException) {
        }
        assertTrue(timer.isRunning());
        timer.pauseTimer();
        assertFalse(timer.isRunning());
    }
}