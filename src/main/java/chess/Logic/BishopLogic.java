package chess.logic;

import java.util.ArrayList;
import java.util.Arrays;

import chess.datamodel.ChessBoard;

public class BishopLogic extends OfficerLogic {
    public BishopLogic(ChessBoard chessBoard) {   
        super(chessBoard, new ArrayList<Integer>(Arrays.asList(1,-1,1,-1)) , 
        new ArrayList<Integer>(Arrays.asList(1,1,-1,-1)), true); //Choose what directions to check for moves in
    }
}