package chess.logic;

import java.util.ArrayList;
import java.util.List;

import chess.io.BoardIO;
import chess.logic.*;
import javafx.scene.image.Image;
import chess.datamodel.ChessBoard;
import chess.datamodel.Move;
import chess.datamodel.Piece;
import chess.datamodel.Position;
import chess.datamodel.Piece.Color;
import chess.datamodel.Piece.PieceType;

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

    public Piece getPiece(Position pos) {
        return chessBoard.getPiece(pos);
    }

    public Image getImage(Piece p) {
        String path = "main/resources/chess/piece_icons/";
        switch(p.getColor()) {
            case BLACK:
                path += "B_";
                switch(p.getType()) {
                    case PAWN:
                        path += "Pawn";
                    case ROOK:
                        path += "Rook";
                    case BISHOP:
                        path += "Bishop";
                    case KING:
                        path += "King";
                    case KNIGHT:
                        path += "Knight";
                    case QUEEN:
                        path += "Queen";
                    default:
                        return null;
                        //throw new IllegalArgumentException("Unknown piece!");
                }
            case WHITE:
                path += "W_";
                switch(p.getType()) {
                    case PAWN:
                        path += "Pawn";
                    case ROOK:
                        path += "Rook";
                    case BISHOP:
                        path += "Bishop";
                    case KING:
                        path += "King";
                    case KNIGHT:
                        path += "Knight";
                    case QUEEN:
                        path += "Queen";
                    default:
                        return null;
                        //throw new IllegalArgumentException("Unknown piece!");
                }
        }
        return new Image(path);
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
