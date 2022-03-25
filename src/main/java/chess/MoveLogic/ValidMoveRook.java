package chess.MoveLogic;

import java.util.List;

import chess.ChessBoard;
import chess.Move;

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
        
        return null;
    }
    
}
