package chess.Logic;

import java.util.ArrayList;
import java.util.List;

import chess.DataModel.ChessBoard;
import chess.DataModel.Move;
import chess.DataModel.Piece;
import chess.DataModel.Position;
import chess.DataModel.Piece.Color;
import chess.DataModel.Piece.PieceType;
import chess.IO.BoardIO;
import chess.Logic.*;

public class GameLogic 
{
    private ArrayList<Piece> pieces;
    private ChessBoard chessBoard;
    private PieceLogic pieceLogic;

    public GameLogic()
    {
        this.chessBoard = new ChessBoard();
        this.pieces = new ArrayList<Piece>();
        this.pieceLogic = new PieceLogic(this.chessBoard);
    }

    private void readInitialPieces()
    {
        BoardIO br = new BoardIO();
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

    public List<Move> getValidMoves(Piece piece)
    {
        return this.pieceLogic.getValidMoves(piece); //delegating the work to the PieceLogic class
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
        Position pos3 = new Position("b1");
        System.out.println(gl.pieceLogic.getValidMoves(gl.chessBoard.getPiece(pos1)));
        System.out.println(gl.pieceLogic.getValidMoves(gl.chessBoard.getPiece(pos2)));
        System.out.println(gl.pieceLogic.getValidMoves(gl.chessBoard.getPiece(pos3)));
    }



}
