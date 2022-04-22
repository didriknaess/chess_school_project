package chess.logic;

import java.io.FileNotFoundException;
import java.util.*;

import chess.io.BoardIO;
import chess.datamodel.*;
import chess.datamodel.Piece.Color;
import chess.datamodel.Piece.PieceType;

public class GameLogic {
    private GameState gameState;
    private ChessBoard chessBoard;
    private PieceLogic pieceLogic;
    //private boolean whitesTurn = true;
    private ChessTimer whiteTimer;
    private ChessTimer blackTimer;
    private HashMap<Integer, Piece> promotedPawns = new HashMap<Integer, Piece>();

    public GameLogic() {
        this.chessBoard = new ChessBoard();
        this.gameState = new GameState();
        this.pieceLogic = new PieceLogic(this.chessBoard);
    }
    private void readInitialPieces() throws FileNotFoundException {
        BoardIO br = new BoardIO();
        this.gameState = br.loadFile("NormalChess.txt");
        if (!this.gameState.isValid()) throw new IllegalStateException("Not a valid game"); 
        //Maybe different exception^^
    }
    private void setUpBoard() throws FileNotFoundException {
        this.chessBoard.clearBoard();
        this.gameState.clearPieces();
        //this.gameState.startTurn();
        readInitialPieces();
        for (Piece piece : this.gameState.getPieces()) {
            this.chessBoard.addPiece(piece);
        }
    }
    public void setTimers(int disposableTimeInSeconds) {
        this.whiteTimer = new ChessTimer(disposableTimeInSeconds);
        this.blackTimer = new ChessTimer(disposableTimeInSeconds);
    }
    public Piece getPiece(Position pos) {
        return chessBoard.getPiece(pos);
    }
    // checks if a spesific move is valid
    public boolean isValidMove(Move move) {
        Piece piece = chessBoard.getPiece(move.getFrom());
        List<Move> moves = getValidMoves(piece);
        for (Move m : moves) {
            if (m.isEqual(move)) return true;
        }
        return false;
    }
    // checks if a spesific square is threatened by the opposing color
    private boolean isThreatened(Piece.Color color, Position pos) {
        boolean toReturn = false;
        for (int i = 0; i<8; i++) {
            for (int j = 0; j<8; j++) {
                Piece p = getPiece(new Position(i, j));
                if (p != null && p.getColor() != color) {
                    for (Move m : getLegalMoves(p)) {
                        if (m.getTo().equals(pos)) toReturn = true;
                        // System.out.println(p + ": Comparing " + m + " to " + pos + ": " + toReturn);
                    }
                }
            }
        }
        return toReturn;
    }

    // checks if the designated color is in check
    public boolean inCheck(Piece.Color color) {
        Position kPos = null;
        // finds and saves the kings position
        for (int i = 0; i<8; i++) {
            for (int j = 0; j<8; j++) {
                Piece p = getPiece(new Position(i, j));
                if (p != null && p.getColor() == color && p.getType() == Piece.PieceType.KING) kPos = new Position(i, j);
            }
        }
        return isThreatened(color, kPos);
    }
    // gets all legal (but not neccesarily valid) moves
    public List<Move> getLegalMoves(Piece piece) {
        return this.pieceLogic.getLegalMoves(piece); //delegating the work to the PieceLogic class
    }
    // adjust the 'legal moves'-method to avoid ending a turn in check
    public List<Move> getValidMoves(Piece piece) {
        List<Move> moves = new ArrayList<Move>();
        // investigates if the move will put you in check, and removes the move if it will
        for (Move move : getLegalMoves(piece)) {
            boolean dangerous = false;
            // does the move in logic to test if it puts you in check
            move(move);
            if (inCheck(piece.getColor())) dangerous = true;
            undo(true);
            if (!dangerous) moves.add(move);
        }
        return moves;
    }
    // executes the move on the board (without checks)
    public void move(Move move) {
        Piece p = chessBoard.getPiece(move.getFrom());
        if (chessBoard.getPiece(move.getTo()) != null) {
            this.gameState.addTakenPiece(chessBoard.getPiece(move.getTo()));
        }
        if (p.getType() == Piece.PieceType.KING && p.getPosition().getColumn() - move.getTo().getColumn() == java.lang.Math.abs(2)) {
            if (p.getPosition().getColumn() - move.getTo().getColumn() < 0) {
                chessBoard.doCastle(move, true);
            } else {
                chessBoard.doCastle(move, false);
            }
        } else {
            chessBoard.doMove(move);
        }
        if (p.getFirstTurnMoved() == -1) p.setFirstTurnMoved(this.gameState.getNumberOfTurns());
        this.gameState.addMove(move);
    }
    public void undo(boolean internal) {
        Move lastMove = this.gameState.popMove();
        Piece moved = chessBoard.getPiece(lastMove.getTo());
        if (promotedPawns.containsKey(getTurnCount())) {
            chessBoard.addPiece(promotedPawns.get(getTurnCount()));
            gameState.getTakenPieces().remove(getTurnCount());
        }

        chessBoard.doMove(new Move(lastMove.getTo(), lastMove.getFrom()));
        // for regular undo on at turn to turn basis
        if (!internal && moved.getFirstTurnMoved() == this.gameState.getNumberOfTurns() - 1) moved.setFirstTurnMoved(-1);
        // for undoing a move to test for check
        if (internal && moved.getFirstTurnMoved() == this.gameState.getNumberOfTurns()) moved.setFirstTurnMoved(-1);

        // check if a piece was taken this turn, and potentially restore it to the board
        for (Integer i : gameState.getTakenPieces().keySet()) {
            if ((!internal && i == this.gameState.getNumberOfTurns()-1) || (internal && i == this.gameState.getNumberOfTurns())) {
                chessBoard.addPiece(gameState.getTakenPieces().get(i));
                gameState.getTakenPieces().remove(i);
            }
        }
    }
    public void promote(Position pos, Piece.PieceType type) throws IllegalArgumentException {
        Piece p = chessBoard.getPiece(pos);
        promotedPawns.put(this.getTurnCount(), p);
        // remove piece from 
        if (type == PieceType.PAWN || type == PieceType.KING) throw new IllegalArgumentException("Illegal promotion");
        chessBoard.addPiece(new Piece(type, p.getColor(), pos));
    }
    // Calulate the score of the specified team
    public int getScore(Piece.Color color) {
        int whiteScore = 0;
        int blackScore = 0;
        if (this.gameState.getTakenPieces().keySet().isEmpty()) return 0;
        for (Integer i : this.gameState.getTakenPieces().keySet()) {
            Piece p = this.gameState.getTakenPieces().get(i);
            if (p.getColor() == Piece.Color.WHITE) {
                    blackScore += pieceLogic.getScore(p);
            } else if (p.getColor() == Piece.Color.BLACK) {
                    whiteScore += pieceLogic.getScore(p);
            }
        }
        switch (color) {
            case BLACK:
                return blackScore;
            case WHITE:
                return whiteScore;
            default:
                return 0;
        } 
    }
    public boolean isWhitePlaying() {
        return this.gameState.whitesTurn();
    }
    public Piece.Color whoseTurn() {
        if (isWhitePlaying()) return Piece.Color.WHITE;
        return Piece.Color.BLACK;
    }
    public void newGame() throws FileNotFoundException {
        setUpBoard();
        //setTimers();
    }
    public void endTurn() {
        if (isWhitePlaying()) {
            pauseTimer(Piece.Color.WHITE);
            startTimer(Piece.Color.BLACK);
        } else {
            pauseTimer(Piece.Color.BLACK);
            startTimer(Piece.Color.WHITE);
        }
        setWhoseTurn();
        
        this.gameState.addTurn();
    }

    private void setWhoseTurn()
    {
        switch (this.gameState.getWhoseTurn()) 
        {
            case BLACK:
                this.gameState.setWhoseTurn(Color.WHITE);
                break;
            default:
                this.gameState.setWhoseTurn(Color.BLACK);
        }
    }
    
    public void undoTurn() {
        if (this.gameState.getMoveHistory().isEmpty()) return;
        undo(false);
        if (isWhitePlaying()) {
            pauseTimer(Piece.Color.WHITE);
            startTimer(Piece.Color.BLACK);
        } else {
            pauseTimer(Piece.Color.BLACK);
            startTimer(Piece.Color.WHITE);
        }
        setWhoseTurn();
        this.gameState.removeTurn();
    }
    public int getTurnCount() {
        return this.gameState.getNumberOfTurns();
    }
    public void startTimer(Piece.Color color) {
        if (color == Piece.Color.BLACK) {
            this.blackTimer.startTimer();
        } else if (color == Piece.Color.WHITE) {
            this.whiteTimer.startTimer();
        }
    }
    public void pauseTimer(Piece.Color color) {
        if (color == Piece.Color.BLACK) {
            this.blackTimer.pauseTimer();
        } else if (color == Piece.Color.WHITE) {
            this.whiteTimer.pauseTimer();
        }
    }
    public int getRemainingTime(Piece.Color color) {
        if (color == Piece.Color.BLACK) {
            return this.blackTimer.getRemainingTime();
        } else if (color == Piece.Color.WHITE) {
            return this.whiteTimer.getRemainingTime();
        } else {
            throw new IllegalArgumentException("Invalid timer!");
        }
    }

}
