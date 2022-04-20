package chess.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import chess.datamodel.ChessBoard;
import chess.datamodel.Move;
import chess.datamodel.Piece;
import chess.datamodel.Position;

public class KingLogic extends OfficerLogic
{
    public KingLogic(ChessBoard chessBoard) 
    {   
        super(chessBoard, new ArrayList<Integer>(Arrays.asList(1,-1,1,-1,1,-1,0,0)) , 
        new ArrayList<Integer>(Arrays.asList(1,1,-1,-1,0,0,-1,1)), false); //Choose what directions to check for moves in
    }
    @Override
    public List<Move> getLegalMoves(Piece p) {
        List<Move> toReturn = super.getLegalMoves(p);
        if (p.getFirstTurnMoved() == -1) {
            // checks for castling to the left
            for (int i = 0; i<4; i++) {
                Position toEvaluate = new Position(p.getPosition().getRow(), p.getPosition().getColumn() - i);
                if (!toEvaluate.isValid()) break;
                if (!this.chessBoard.isSquareEmpty(toEvaluate)) {
                    if (this.chessBoard.getPiece(toEvaluate).getType() != Piece.PieceType.ROOK) break;
                    if (this.chessBoard.getPiece(toEvaluate).getColor() == p.getColor() && this.chessBoard.getPiece(toEvaluate).getFirstTurnMoved() == -1) {
                        toReturn.add(new Move(p.getPosition(), new Position(p.getPosition().getRow(), toEvaluate.getColumn()+2)));
                        break;
                    }
                } 
            }
            // checks for castling to the right
            for (int j = 0; j<4; j++) {
                Position toEvaluate = new Position(p.getPosition().getRow(), p.getPosition().getColumn() + j);
                if (!toEvaluate.isValid()) break;
                if (!this.chessBoard.isSquareEmpty(toEvaluate)) {
                    if (this.chessBoard.getPiece(toEvaluate).getType() != Piece.PieceType.ROOK) break;
                    if (this.chessBoard.getPiece(toEvaluate).getColor() == p.getColor() && this.chessBoard.getPiece(toEvaluate).getFirstTurnMoved() == -1) {
                        toReturn.add(new Move(p.getPosition(), new Position(p.getPosition().getRow(), toEvaluate.getColumn()-2)));
                        break;
                    }
                } 
            }
        }
        return toReturn;
    }
}
