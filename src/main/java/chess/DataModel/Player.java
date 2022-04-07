package chess.datamodel;

public class Player 
{
    private int secondsRemaining; //Make a timer when other things start to work
    private String name;
    private Piece.Color color;  
    private int score = 0;

    public Player(Piece.Color color) {
        this.color = color;
    }

    public Piece.Color getColor() {
        return this.color;
    }
}
