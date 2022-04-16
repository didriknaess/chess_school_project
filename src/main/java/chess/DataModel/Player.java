package chess.datamodel;


// this whole class is obsolete
public class Player
{
    private Piece.Color color;  

    public Player(Piece.Color color, int disposableTimeInSeconds) {
        this.color = color;
    }

    public Piece.Color getColor() {
        return this.color;
    }
}
