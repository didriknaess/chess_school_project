package chess.logic;

import java.util.ArrayList;
import java.util.List;

import chess.datamodel.ChessBoard;
import chess.datamodel.Move;
import chess.datamodel.Piece;
import chess.datamodel.Position;
import chess.datamodel.Piece.PieceType;

public class PawnLogic {
    static public List<Move> getLegalMoves(Piece piece, ChessBoard chessBoard) {
        List<Move> returnList = new ArrayList<>();
        if (!piece.getType().equals(PieceType.PAWN)) throw new IllegalArgumentException("Only pawns!");
        int direction = 1;
        if (piece.getColor().equals(Piece.Color.BLACK)) direction = -1;
        //Check one step forward
        Position newPosition = piece.getPosition().getNewPosition(direction, 0);
        boolean oneStepPossible = false;
        if (newPosition.isValid() && chessBoard.isSquareEmpty(newPosition)) {
            oneStepPossible = true;
            returnList.add(new Move(piece.getPosition(), newPosition));
        }
        //Check two steps forward
        if (oneStepPossible && piece.getFirstTurnMoved() == -1) {
            newPosition = piece.getPosition().getNewPosition(2*direction, 0);
            if (!newPosition.isValid()) throw new IllegalArgumentException("I think the board is broken");
            if (chessBoard.isSquareEmpty(newPosition)) {
                returnList.add(new Move(piece.getPosition(), newPosition));
            }
        }
        //Check diagonally right
        newPosition = piece.getPosition().getNewPosition(direction, 1);
        if (newPosition.isValid()) {
            Piece opponent = chessBoard.getPiece(newPosition);
            if (opponent != null) {
                if (opponent.getColor() != piece.getColor()) {
                    returnList.add(new Move(piece.getPosition(), newPosition));
                }
            }
        }
        //Check diagonally left
        newPosition = piece.getPosition().getNewPosition(direction, -1);
        if (newPosition.isValid()) {
            Piece opponent = chessBoard.getPiece(newPosition);
            if (opponent != null) {
                if (!opponent.getColor().equals(piece.getColor())) {
                    returnList.add(new Move(piece.getPosition(), newPosition));
                }
            }
        }
        return returnList;
    }
}