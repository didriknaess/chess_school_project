package chess;

import java.util.ArrayList;
import java.util.List;
import chess.MoveLogic.*;

import chess.Pieces.Piece;
import chess.Pieces.Piece.Color;
import chess.Pieces.Piece.PieceType;

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
        //br.readFile("NormalChess.txt");
        br.readFile("TestChess1.txt");
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
        Position pos1 = new Position("a2");
        Position pos2 = new Position("h5");
        ValidMovePawn vmp = new ValidMovePawn(gl.chessBoard);
        ValidMoveRook vmr = new ValidMoveRook(gl.chessBoard);
        System.out.println(vmp.getValidMoves(gl.chessBoard.getPiece(pos1)));
        System.out.println(vmr.getValidMoves(gl.chessBoard.getPiece(pos2)));
    }



}
