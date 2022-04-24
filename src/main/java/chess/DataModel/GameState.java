package chess.datamodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import chess.datamodel.Piece.Color;

public class GameState {

    private Piece.Color whoseTurn = Color.WHITE;
    private int secondsLeftWhite;
    private int secondsLeftBlack; // use white/black instead of p1/p2, easier to understand + im using that in the controller
    private int turn = 1;
    private ArrayList<Piece> pieces = new ArrayList<>();
    private Stack<Move> moveHistory = new Stack<Move>();
    private HashMap<Integer, Piece> takenPieces = new HashMap<Integer, Piece>();
    private HashMap<Integer, Piece> promotedPawns = new HashMap<Integer, Piece>();

    public GameState()
    {

    }

    public boolean whitesTurn()
    {
        return this.whoseTurn.equals(Color.WHITE);
    }

    public void addPromotedPawn(Integer move, Piece piece)
    {
        this.promotedPawns.put(move, piece);
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

    public void setSecondsRemainingBlack(int secondsRemainingBlack) {
        this.secondsLeftBlack = secondsRemainingBlack;
    }

    public void setSecondsRemainingWhite(int secondsRemainingWhite) {
        this.secondsLeftWhite = secondsRemainingWhite;
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

    public void removePromotedPawn(int turn)
    {
        this.promotedPawns.remove(turn);
    }

    public ArrayList<Piece> getPieces()
    {
        return this.pieces;
    }

    public HashMap<Integer, Piece> getTakenPieces()
    {
        return this.takenPieces;
    }

    public HashMap<Integer, Piece> getPromotedPawns()
    {
        return this.promotedPawns;
    }

    public Piece getPromotedPawn(int turn)
    {
        return this.promotedPawns.get(turn);
    }

    public Stack<Move> getMoveHistory()
    {
        return this.moveHistory;
    }

    public int getNumberOfTurns()
    {
        return this.turn;
    }

    public int getBlackSeconds() {
        return this.secondsLeftBlack;
    }

    public int getWhiteSeconds() {
        return this.secondsLeftWhite;
    }

    public Piece.Color getWhoseTurn() {
        return this.whoseTurn;
    }
    
    public String savingGetWhoseTurn() {
        if (this.whoseTurn == Color.WHITE) return "white";
        return "black";
    }

    public boolean isValid() {
        return this.pieces.size() > 0;
    }
}
