package chess.logic;

import java.util.*;

import chess.io.BoardIO;
import chess.datamodel.*;

public class GameLogic {
    private ArrayList<Piece> pieces;
    private ChessBoard chessBoard;
    private PieceLogic pieceLogic;
    private Stack<Move> moveHistory = new Stack<Move>();
    private HashMap<Integer, Piece> takenPieces = new HashMap<Integer, Piece>();
    private int turn;
    private boolean whitesTurn = true;
    private ChessTimer whiteTimer;
    private ChessTimer blackTimer;

    public GameLogic() {
        this.chessBoard = new ChessBoard();
        this.pieces = new ArrayList<Piece>();
        this.pieceLogic = new PieceLogic(this.chessBoard);
    }
    private void readInitialPieces() {
        BoardIO br = new BoardIO();
        //br.readFile("NormalChess.txt");
        br.readFile("TestChess1.txt");
        List<String> pieceLines = br.getPieces();
        for (String pieceStr : pieceLines) 
        {
            pieces.add(Piece.createNewPiece(pieceStr));
        }
    }
    private void setUpBoard() {
        this.chessBoard.clearBoard();
        this.pieces.clear();
        this.takenPieces = new HashMap<Integer, Piece>();
        this.turn = 1;
        readInitialPieces();
        for (Piece piece : this.pieces) {
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
        List<Move> moves = getLegalMoves(piece);
        for (Move m : moves) {
            if (m.isEqual(move)) return true;
        }
        return false;
    }
    // checks if a spesific square is threatened by the opposing color
    private boolean isThreatened(Piece.Color color, Position pos) {
        boolean toReturn = false;
        System.out.println(pieces);
        for (Piece p : pieces) {
            if (p != null && !p.getColor().equals(color)) {
                for (Move m : getLegalMoves(p)) {
                    if (m.getTo().equals(pos)) toReturn = true;
                    System.out.println(p + ": Comparing " + m + " to " + pos + ": " + toReturn);
                }  
            }
        }
        // for (int i = 0; i<8; i++) {
        //     for (int j = 0; j<8; j++) {
        //         Piece p = getPiece(new Position(i, j));
        //         if (p == null || p.getColor() == color) break;
        //         for (Move m : getLegalMoves(p)) {
        //             if (m.getTo().equals(pos)) toReturn = true;
        //             System.out.println("Comparing " + m.getTo() + " to " + pos + ": " + toReturn);
        //         }
        //     }
        // }
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
        List<Move> moves = getLegalMoves(piece);
        // investigates if the move will put you in check, and removes the move if it will
        for (Move move : moves) {
            boolean dangerous = false;
            // does the move in logic to test if it puts you in check
            move(move);
            if (inCheck(piece.getColor())) dangerous = true;
            undo(true);
            if (dangerous) moves.remove(move);
        }
        return moves;
    }
    // executes the move on the board (without checks)
    public void move(Move move) {
        if (chessBoard.getPiece(move.getTo()) != null) {
            takenPieces.put(this.turn, chessBoard.getPiece(move.getTo()));
        }
        chessBoard.doMove(move);
        Piece p = chessBoard.getPiece(move.getTo());
        if (p.getFirstTurnMoved() == -1) p.setFirstTurnMoved(turn);
        this.moveHistory.add(move);
    }
    public void undo(boolean internal) {
        Move lastMove = moveHistory.pop();
        Piece moved = chessBoard.getPiece(lastMove.getTo());
        chessBoard.doMove(new Move(lastMove.getTo(), lastMove.getFrom()));
        // for regular undo on at turn to turn basis
        if (!internal && moved.getFirstTurnMoved() == this.turn - 1) moved.setFirstTurnMoved(-1);
        // for undoing a move to test for check
        if (internal && moved.getFirstTurnMoved() == this.turn) moved.setFirstTurnMoved(-1);

        // check if a piece was taken this turn, and potentially restore it to the board
        for (Integer i : takenPieces.keySet()) {
            if ((!internal && i == this.turn-1) || (internal && i == this.turn)) {
                chessBoard.addPiece(takenPieces.get(i));
                takenPieces.remove(i);
            }
        }
    }
    // Calulate the score of the specified team
    public int getScore(Piece.Color color) {
        int whiteScore = 0;
        int blackScore = 0;
        if (takenPieces.keySet().isEmpty()) return 0;
        for (Integer i : takenPieces.keySet()) {
            Piece p = takenPieces.get(i);
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
        return this.whitesTurn;
    }
    public Piece.Color whoseTurn() {
        if (whitesTurn) return Piece.Color.WHITE;
        return Piece.Color.BLACK;
    }
    public void newGame() {
        setUpBoard();
    }
    public void endTurn() {
        if (isWhitePlaying()) {
            pauseTimer(Piece.Color.WHITE);
            startTimer(Piece.Color.BLACK);
        } else {
            pauseTimer(Piece.Color.BLACK);
            startTimer(Piece.Color.WHITE);
        }
        this.whitesTurn = !whitesTurn;
        this.turn++;
    }
    public void undoTurn() {
        if (moveHistory.isEmpty()) return;
        undo(false);
        if (isWhitePlaying()) {
            pauseTimer(Piece.Color.WHITE);
            startTimer(Piece.Color.BLACK);
        } else {
            pauseTimer(Piece.Color.BLACK);
            startTimer(Piece.Color.WHITE);
        }
        this.whitesTurn = !whitesTurn;
        this.turn--;
    }
    public int getTurnCount() {
        return this.turn;
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