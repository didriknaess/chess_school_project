package chess.datamodel;

import java.util.ArrayList;

import chess.datamodel.Piece.Color;

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

    public int getP2Seconds() {
        return this.secondsLeftP2;
    }

    public int getP1Seconds() {
        return this.secondsLeftP1;
    }

    public String getP2Name() {
        return this.p2Name;
    }

    public String getP1Name() {
        return this.p1Name;
    }

    public Piece.Color getWhoseTurn() {
        return this.whoseTurn;
    }

    public void setTurns(int turns) {
        this.turn = turns;
    }

    public void setSecondsRemainingP2(int secondsRemainingP2) {
        this.secondsLeftP2 = secondsRemainingP2;
    }

    public void setSecondsRemainingP1(int secondsRemainingP1) {
        this.secondsLeftP1 = secondsRemainingP1;
    }

    public void setP2Name(String p2Name) {
        this.p2Name = p2Name;
    }

    public void setP1Name(String p1Name) {
        this.p1Name = p1Name;
    }

    public void setWhoseTurn(Color color) {
        this.whoseTurn = color;
    }

    public boolean isValid() {
        return this.pieces.size() > 0;
    }
}
