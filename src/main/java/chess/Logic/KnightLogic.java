package chess.logic;

import java.util.ArrayList;
import java.util.Arrays;

import chess.datamodel.ChessBoard;

public class KnightLogic extends OfficerLogic 
{
    public KnightLogic(ChessBoard chessBoard) 
    {   
        super(chessBoard, new ArrayList<Integer>(Arrays.asList(1,2,2,1,-1,-2,-2,-1)) , 
        new ArrayList<Integer>(Arrays.asList(2,1,-1,-2,-2,-1,1,2)), false); //Choose what directions to check for moves in
    }
}
