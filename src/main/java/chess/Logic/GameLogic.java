package chess.logic;

import java.util.*;

import chess.io.BoardIO;
import chess.datamodel.*;

public class GameLogic {
    private ArrayList<Piece> pieces;
    private ChessBoard chessBoard;
    private PieceLogic pieceLogic;
    private HashMap<Piece, Integer> takenPieces = new HashMap<Piece, Integer>();
    private int turn;
    private boolean whitesTurn = true;

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
        this.takenPieces = new HashMap<Piece, Integer>();
        this.turn = 1;
        readInitialPieces();
        for (Piece piece : this.pieces) {
            this.chessBoard.addPiece(piece);
        }
    }
    public Piece getPiece(Position pos) {
        return chessBoard.getPiece(pos);
    }
    public boolean isValidMove(Move move) {
        Piece piece = chessBoard.getPiece(move.getFrom());
        List<Move> moves = getValidMoves(piece);
        for (Move m : moves) {
            if (m.isEqual(move)) return true;
        }
        return false;
    }
    public List<Move> getValidMoves(Piece piece) {
        return this.pieceLogic.getValidMoves(piece); //delegating the work to the PieceLogic class
    }
    public void move(Move move) {
        if (!isValidMove(move)) return;
        Piece piece = chessBoard.getPiece(move.getFrom());
        if (chessBoard.getPiece(move.getTo()) != null) {
            takenPieces.put(piece, this.turn);
        }
        chessBoard.doMove(move);
        piece.moveTo(move.getTo());
    }
    // Calulate the score of the specified team
    public int getScore(Piece.Color color) {
        int whiteScore = 0;
        int blackScore = 0;
        if (takenPieces.keySet().isEmpty()) return 0;
        for (Piece piece : takenPieces.keySet()) {
            switch(piece.getColor()) {
                case BLACK:
                    blackScore += pieceLogic.getScore(piece);
                case WHITE:
                    whiteScore += pieceLogic.getScore(piece);
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
        this.whitesTurn = !whitesTurn;
        this.turn += 1;
    }
    public int getTurnCount() {
        return this.turn;
    }
}