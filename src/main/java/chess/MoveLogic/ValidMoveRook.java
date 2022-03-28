package chess.MoveLogic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import chess.ChessBoard;
import chess.Move;
import chess.Position;
import chess.Pieces.Piece;
import chess.Pieces.Piece.PieceType;

public class ValidMoveRook implements IValidMove
{
    ChessBoard chessBoard;

    public ValidMoveRook(ChessBoard chessBoard)
    {
        this.chessBoard = chessBoard;
    }

    @Override
    public List<Move> getValidMoves(Piece piece) 
    {
        List<Move> returnList = new ArrayList<>();
        if (!piece.getType().equals(PieceType.ROOK)) throw new IllegalArgumentException("Only rooks!");   

        //Directions
        List<Integer> rowDirections = Arrays.asList(1,-1,0,0); 
        List<Integer> colDirections = Arrays.asList(0,0,-1,1); 

        Position startPos = piece.getPosition(); //Needed?

        for (int i = 0; i < rowDirections.size(); i++) 
        {
            int howMany = 1;
            while (true)
            {
                Position newPos = piece.getPosition().getNewPosition(howMany*rowDirections.get(i), howMany*colDirections.get(i));
                if (newPos.isValid()) //Needs to be valid
                {
                    if (this.chessBoard.isSquareEmpty(newPos)) //if empty 
                    {
                        returnList.add(new Move(piece.getPosition(), newPos)); //new move added
                        howMany ++; //check next square in order
                        continue; //break this iteration of the while loop
                    }
                    Piece opponent = chessBoard.getPiece(newPos);
                    if (!opponent.equals(null) && !opponent.getColor().equals(piece.getColor())) //Check if there is an opponent
                    {
                        returnList.add(new Move(piece.getPosition(), newPos)); //Can take opponent
                        break; //break out of the while loop
                    }
                    if (!opponent.equals(null)) //Teammate
                    {
                        break; //break out of the while loop
                    }
                }
                break;
            }  
        }
        return returnList;
    } 
}
