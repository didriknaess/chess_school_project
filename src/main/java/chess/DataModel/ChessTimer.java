package chess.datamodel;

import java.util.Timer;
import java.util.TimerTask;

public class ChessTimer {
    private int timeRemaining; // entity: 1/10th of a second
    private Timer timer;
    private boolean isRunning;
    public ChessTimer(int disposableTimeInSeconds) {
        this.timeRemaining = disposableTimeInSeconds * 10;
        this.timer = new Timer(true);
        isRunning = false;
    }
    
    public int getRemainingTime() {
        return this.timeRemaining;
    }

    public void startTimer() {
        if (isRunning == true) return;
        if (timeRemaining <= 0) return;
        this.isRunning = true;
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                timeRemaining--;
            }
        }, 100, 100);
    }
    public void pauseTimer() {
        if (isRunning == false) return;
        timer.cancel();
        this.isRunning = false;
        this.timer = new Timer(true);
    }
    public boolean isRunning() {
        return this.isRunning;
    }
}
