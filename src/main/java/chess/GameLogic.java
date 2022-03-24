package chess;

import java.util.ArrayList;
import java.util.List;

import chess.Pieces.Piece;

public class GameLogic 
{
    private ArrayList<Piece> pieces;
    private ChessBoard chessBoard;

    public GameLogic()
    {
        this.chessBoard = new ChessBoard();
        this.pieces = new ArrayList<Piece>();
    }

    private void readInitialPieces()
    {
        BoardReader br = new BoardReader();
        br.readFile("NormalChess.txt");
        List<String> pieceLines = br.getPieces();
        for (String pieceStr : pieceLines) 
        {
            pieces.add(Piece.createNewPiece(pieceStr));
        }
    }

    private void setUpBoard() 
    {
        this.chessBoard.clearBoard();
        this.pieces.clear();
        readInitialPieces();

        for (Piece piece : this.pieces) 
        {
            this.chessBoard.addPiece(piece);
        }
    }

    public void newGame()
    {
        setUpBoard();

    }

    public static void main(String[] args) {
        GameLogic gl = new GameLogic();
        gl.newGame();
        System.out.println(gl.chessBoard.toString());
    }



}
