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

    public List<Move> getValidMoves(Piece piece) {
        return this.pieceLogic.getValidMoves(piece); //delegating the work to the PieceLogic class
    }
    public void move(Piece piece, Move move) {
        if (!getValidMoves(piece).contains(move)) return;
        if (chessBoard.getPiece(move.getTo()) != null) {
            if (whitesTurn) {
                takenByWhite.add(chessBoard.getPiece(move.getTo()));
            } else {
                takenByBlack.add(chessBoard.getPiece(move.getTo()));
            }
        }
        piece.moveTo(move.getTo());
        chessBoard.doMove(move);
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
    public static int calculateScore(ArrayList<Piece> pieces) {
        int score = 0;
        if (pieces.isEmpty()) return score;
        for (Piece p : pieces) {
            switch (p.getType()) {
                case PAWN:
                    score++;
                case ROOK:
                    score += 5;
                case BISHOP:
                    score += 3;
                case KING:
                    score += 0;
                case KNIGHT:
                    score += 5;
                case QUEEN:
                    score += 9;
            }
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
