package chess.MoveLogic;

import java.util.List;

import chess.Move;
import chess.Pieces.Piece;

public interface IValidMove 
{
    public List<Move> getValidMoves(Piece piece);    
}
