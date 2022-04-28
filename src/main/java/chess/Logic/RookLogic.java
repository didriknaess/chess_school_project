package chess.logic;

import java.util.ArrayList;
import java.util.Arrays;

import chess.datamodel.ChessBoard;

public class RookLogic extends OfficerLogic {
    public RookLogic(ChessBoard chessBoard) {
        super(chessBoard, new ArrayList<Integer>(Arrays.asList(1,-1,0,0)) , 
        new ArrayList<Integer>(Arrays.asList(0,0,-1,1)), true); //Choose what directions to check for moves in
    }
}
