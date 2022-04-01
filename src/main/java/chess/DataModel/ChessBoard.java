package chess.datamodel;

import java.util.ArrayList;

public class ChessBoard 
{
    private ArrayList<ArrayList<Piece>> chessBoard;

    public ChessBoard()
    {
        this.chessBoard = new ArrayList<ArrayList<Piece>>();
        
        for (int i = 0; i < 8; i++) 
        {
            ArrayList<Piece> foo = new ArrayList<>();
            for (int j = 0; j < 8; j++) 
            {
                foo.add(null);
            }
            chessBoard.add(foo);
        }
    }

    public void clearBoard()
    {
        for (int i = 0; i < 8; i++) 
        {
            for (int j = 0; j < 8; j++) 
            {
                chessBoard.get(i).set(j, null);
            }
        }   
    }

    public Piece getPiece(Position pos)
    {
        return this.chessBoard.get(pos.getRow()).get(pos.getColumn());
    }

    public void addPiece(Piece piece)
    {
        Position position = piece.getPosition();
        chessBoard.get(position.getRow()).set(position.getColumn(), piece);
    }

    public boolean isSquareEmpty(Position pos)
    {
        return (this.getPiece(pos) == null);
    }

    public void doMove(Move move)
    {

    }

    @Override
    public String toString()
    {
        String returnString = "";
        for (int r = 7; r >= 0; r--) 
        {
            returnString += r+1 + " ";
            for (int c = 0; c < 8; c++) 
            {
                if (chessBoard.get(r).get(c) == null) returnString += "  ";
                else returnString += chessBoard.get(r).get(c).toString() + " "; 
            }
            returnString += "\n";
        }
        returnString += "  A B C D E F G H";
        return returnString;
    }

}
