package chess.MoveLogic;

import java.util.ArrayList;
import java.util.List;

import chess.ChessBoard;
import chess.Move;
import chess.Position;
import chess.Pieces.*;
import chess.Pieces.Piece.Color;
import chess.Pieces.Piece.PieceType;

public class ValidMovePawn implements IValidMove
{
    ChessBoard chessBoard;
    
    public ValidMovePawn(ChessBoard chessBoard)
    {
        this.chessBoard = chessBoard;
    }

    public List<Move> getValidMoves(Piece piece)
    {
        List<Move> returnList = new ArrayList<>();
        if (!piece.getType().equals(PieceType.PAWN)) throw new IllegalArgumentException("Only pawns!");
        
        int direction = 1;
        if (piece.getColor().equals(Color.BLACK)) direction = -1;
        
        //check square above
        Position newPosition = piece.getPosition().getNewPosition(direction, 0);
        boolean oneStepPossible = false;
        if (newPosition.isValid() && this.chessBoard.isSquareEmpty(newPosition))
        {
            oneStepPossible = true;
            returnList.add(new Move(piece.getPosition(), newPosition));
        }
        //Check diagonally right
        newPosition = piece.getPosition().getNewPosition(direction, 1);
        if (newPosition.isValid())
        {
            Piece opponent = chessBoard.getPiece(newPosition);
            if (opponent != null && opponent.getColor() != piece.getColor()) 
            {
                returnList.add(new Move(piece.getPosition(), newPosition));
            }
        }
        //Check diagonally left
        newPosition = piece.getPosition().getNewPosition(direction, -1);
        if (newPosition.isValid())
        {
            Piece opponent = chessBoard.getPiece(newPosition);
            if (!opponent.equals(null) & !opponent.getColor().equals(piece.getColor())) 
            {
                returnList.add(new Move(piece.getPosition(), newPosition));
            }
        }
        //Check two steps forward
        if (!piece.hasBeenMoved() && oneStepPossible)
        {
            newPosition = piece.getPosition().getNewPosition(2*direction, 0);
            if (!newPosition.isValid()) throw new IllegalArgumentException("I think the board is broken");
            if (chessBoard.isSquareEmpty(newPosition))
            {
                returnList.add(new Move(piece.getPosition(), newPosition));
            }

        }
        
        return returnList;
    }


}
