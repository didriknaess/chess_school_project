package chess.Pieces;

import java.util.ArrayList;

import chess.Move;
import chess.Position;

public class Pawn extends Piece
{

    public Pawn(Color color)
    {
        super(color, "p");
    }

    public ArrayList<Move> validMoves(Position from)
    {
        //insert logic for finding valid moves here.
        ArrayList<Move> returnList = new ArrayList<>();
        return returnList;
    }

}
