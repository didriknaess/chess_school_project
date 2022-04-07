package chess.logic;

import java.util.ArrayList;
import java.util.List;

import chess.io.BoardIO;
import chess.datamodel.*;

public class GameLogic 
{
    private ArrayList<Piece> pieces;
    private ChessBoard chessBoard;
    private PieceLogic pieceLogic;
    private ArrayList<Piece> takenByBlack = new ArrayList<Piece>();
    private ArrayList<Piece> takenByWhite = new ArrayList<Piece>();
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
        readInitialPieces();

        for (Piece piece : this.pieces) 
        {
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
            if (whitesTurn) {
                takenByWhite.add(chessBoard.getPiece(move.getTo()));
            } else {
                takenByBlack.add(chessBoard.getPiece(move.getTo()));
            }
        }
        chessBoard.doMove(move);
        piece.moveTo(move.getTo());
    }
    // Calulate the score of the specified team
    public int getScore(Piece.Color color) {
        switch(color) {
            case BLACK:
                return calculateScore(takenByBlack);
            case WHITE:
                return calculateScore(takenByWhite);
            default:
                return 0;
        }
    }
    public int calculateScore(ArrayList<Piece> pieces) {
        int score = 0;
        if (pieces.isEmpty()) return score;
        for (Piece p : pieces) {
            score += pieceLogic.getScore(p);
        }
        return score;
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
    }
    @Override
    public String toString() {
        return chessBoard.toString();
    }
    // public static void main(String[] args) {
    //     GameLogic gl = new GameLogic();
    //     gl.newGame();
    //     System.out.println(gl.chessBoard.toString());
    //     Position pos1 = new Position("a2");
    //     Position pos2 = new Position("h5");
    //     Position pos3 = new Position("b1");
    //     System.out.println(gl.pieceLogic.getValidMoves(gl.chessBoard.getPiece(pos1)));
    //     System.out.println(gl.pieceLogic.getValidMoves(gl.chessBoard.getPiece(pos2)));
    //     System.out.println(gl.pieceLogic.getValidMoves(gl.chessBoard.getPiece(pos3)));
    // }
}
