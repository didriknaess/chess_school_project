package chess.datamodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import chess.datamodel.Piece.Color;

public class GameState {

    private Piece.Color whoseTurn;
    private int secondsLeftP1;
    private int secondsLeftP2; // use white/black instead of p1/p2, easier to understand + im using that in the controller
    private int turn;
    private ArrayList<Piece> pieces = new ArrayList<>();
    private Stack<Move> moveHistory = new Stack<Move>();
    private HashMap<Integer, Piece> takenPieces = new HashMap<Integer, Piece>();

    public GameState()
    {

    }

    public void addMove(Move move)
    {
        this.moveHistory.add(move);
    }

    public Move popMove()
    {
        return this.moveHistory.pop();
    }
    
    public void addTakenPiece(Piece piece)
    {
        this.takenPieces.put(this.getNumberOfTurns(), piece);
    }

    public void addPiece(Piece piece)
    {
        this.pieces.add(piece);
    }

    public void clearPieces()
    {
        this.pieces.clear();
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

    public void setWhoseTurn(Color color) {
        this.whoseTurn = color;
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

    public ArrayList<Piece> getPieces()
    {
        return this.pieces;
    }

    public HashMap<Integer, Piece> getTakenPieces()
    {
        return this.takenPieces;
    }

    public Stack<Move> getMoveHistory()
    {
        return this.moveHistory;
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

    public Piece.Color getWhoseTurn() {
        return this.whoseTurn;
    }

    public boolean isValid() {
        return this.pieces.size() > 0;
    }
}
