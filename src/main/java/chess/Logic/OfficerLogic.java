package chess.Logic;

import java.util.ArrayList;
import java.util.List;

import chess.DataModel.ChessBoard;
import chess.DataModel.Move;
import chess.DataModel.Piece;
import chess.DataModel.Position;
import chess.DataModel.Piece.PieceType;

public class OfficerLogic 
{
    protected ChessBoard chessBoard;
    protected ArrayList<Integer> rowDirections;
    protected ArrayList<Integer> colDirections;
    protected boolean repeatMoves;
    
    public OfficerLogic(ChessBoard chessBoard, ArrayList<Integer> rowDirections, 
    ArrayList<Integer> colDirections, boolean repeatMoves)
    {
        this.chessBoard = chessBoard;
        this.rowDirections = rowDirections;
        this.colDirections = colDirections;
        this.repeatMoves = repeatMoves;
    }
    
    public List<Move> getValidMoves(Piece piece) 
    {
        List<Move> returnList = new ArrayList<>();  

        Position startPos = piece.getPosition(); //Save the startposition

        for (int i = 0; i < this.rowDirections.size(); i++) 
        {
            Position newPos = startPos;
            do //do this either way at least once
            {
                newPos = newPos.getNewPosition(this.rowDirections.get(i), this.colDirections.get(i)); //Increment
                if (newPos.isValid()) //Needs to be valid
                {
                    if (this.chessBoard.isSquareEmpty(newPos)) //if empty 
                    {
                        returnList.add(new Move(piece.getPosition(), newPos)); //new move added
                        continue; //break this iteration of the while loop
                    }
                    Piece opponent = chessBoard.getPiece(newPos); //Know that there is a piece on this square
                    if (!opponent.getColor().equals(piece.getColor())) //Check if there is an opponent
                    {
                        returnList.add(new Move(piece.getPosition(), newPos)); //Can take opponent
                        break; //break out of the while loop
                    }
                }
                break;
            } 
            while (repeatMoves); //repeat if the piece can move more than one square
        }
        return returnList;
    } 
}
