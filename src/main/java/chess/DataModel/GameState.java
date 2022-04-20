package chess.datamodel;

import java.util.ArrayList;

public class GameState {

    private Piece.Color whoseTurn;
    private String p1Name;
    private String p2Name; // names of players are not necessary!
    private int secondsLeftP1;
    private int secondsLeftP2; // use white/black instead of p1/p2, easier to understand + im using that in the controller
    private int turn;
    private ArrayList<Piece> pieces = new ArrayList<>();

    public GameState()
    {

    }

    public void addPiece(Piece piece)
    {
        this.pieces.add(piece);
    }

    public void clearPieces()
    {
        this.pieces.clear();
    }

    public ArrayList<Piece> getPieces()
    {
        return this.pieces;
    }

    public void addTurn()
    {
        this.turn ++;
    }

    public void removeTurn()
    {
        this.turn --;
    }
    
    public void startTurn()
    {
        this.turn = 1;
    }

    public int getNumberOfTurns()
    {
        return this.turn;
    }
}
