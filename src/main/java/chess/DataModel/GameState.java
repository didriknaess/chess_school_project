package chess.datamodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import chess.datamodel.Piece.Color;
import chess.datamodel.Piece.PieceType;

public class GameState {

    private Piece.Color whoseTurn = Color.WHITE; //Yes
    private int secondsLeftWhite; //Yes
    private int secondsLeftBlack; //Yes
    private int turn = 1; //Yes
    private ArrayList<Piece> pieces = new ArrayList<>(); //Yes
    private Stack<Move> moveHistory = new Stack<Move>(); //Yes
    private HashMap<Integer, Piece> capturedPieces = new HashMap<Integer, Piece>(); //Yes
    private HashMap<Integer, Piece> promotedPawns = new HashMap<Integer, Piece>();

    public GameState()
    {

    }

    //Set
    public void setTurns(int turns) {
        this.turn = turns;
    }

    public void setSecondsRemainingBlack(int secondsRemainingBlack) {
        if (secondsRemainingBlack < 0) throw new IllegalArgumentException("Can't have negative time reamining!");
        this.secondsLeftBlack = secondsRemainingBlack;
    }

    public void setSecondsRemainingWhite(int secondsRemainingWhite) {
        if (secondsRemainingWhite < 0) throw new IllegalArgumentException("Can't have negative time reamining!");
        this.secondsLeftWhite = secondsRemainingWhite;
    }

    public void setWhoseTurn(Color color) {
        this.whoseTurn = color;
    }

    //Add
    public void addPromotedPawn(Integer move, Piece piece)
    {
        if (piece.getType() != PieceType.PAWN) throw new IllegalArgumentException("Only pawns can get a promotion");
        this.promotedPawns.put(move, piece);
    }

    public void addMove(Move move)
    {
        this.moveHistory.add(move);
    }

    public void addCapturedPiece(Piece piece)
    {
        this.capturedPieces.put(this.getNumberOfTurns(), piece);
    }

    public void addPiece(Piece piece)
    {
        if (this.pieces.contains(piece)) throw new IllegalArgumentException("Can't add a piece thats already there");
        this.pieces.add(piece);
    }

    public void addTurn()
    {
        this.turn ++;
    }

    //Remove, pop and clear
    public Move popMove()
    {
        return this.moveHistory.pop();
    }

    public void clearPieces()
    {
        this.pieces.clear();
    }

    public void removeTurn()
    {
        this.turn --;
    }

    public void removeCapturedPiece(Integer i)
    {
        if (!this.capturedPieces.containsKey(i) && i != null) throw new IllegalArgumentException("No pieces was captured this turn");
        this.capturedPieces.remove(i);
    }

    public void removePromotedPawn(int turn)
    {
        this.promotedPawns.remove(turn);
    }

    //Boolean
    public boolean whitesTurn()
    {
        return this.whoseTurn.equals(Color.WHITE);
    }

    public boolean isValid() {
        return this.pieces.size() > 0;
    }

    //Get
    public ArrayList<Piece> getPieces()
    {
        return this.pieces;
    }

    public HashMap<Integer, Piece> getCapturedPieces()
    {
        return this.capturedPieces;
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
}
