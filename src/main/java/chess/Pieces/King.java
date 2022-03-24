package chess.Pieces;

import java.util.ArrayList;

import chess.Move;
import chess.Position;

public class King extends Piece
{
    public King(Color color)
    {
        super(color, "k");
    }

    public ArrayList<Move> validMoves(Position from)
    {
        //insert logic for finding valid moves here.
        ArrayList<Move> returnList = new ArrayList<>();
        return returnList;
    }
}
