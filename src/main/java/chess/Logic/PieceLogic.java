package chess.logic;

import java.util.List;

import chess.datamodel.ChessBoard;
import chess.datamodel.Move;
import chess.datamodel.Piece;

public class PieceLogic //This class is to help prevent having to make new instances of ...logic in GameLogic
//Want to delegate most of GameLogics tasks to other helping functions.
//Makes the GameLogic easier to read, for details you move into the other subclasses
{
    private ChessBoard chessBoard;
    private RookLogic rookLogic;
    private BishopLogic bishopLogic;
    private KnightLogic knightLogic;
    private QueenLogic queenLogic;
    private KingLogic kingLogic;

    public PieceLogic(ChessBoard chessBoard)
    {
        this.chessBoard = chessBoard;
        this.rookLogic = new RookLogic(chessBoard);
        this.bishopLogic = new BishopLogic(chessBoard);
        this.knightLogic = new KnightLogic(chessBoard);
        this.queenLogic = new QueenLogic(chessBoard);
        this.kingLogic = new KingLogic(chessBoard);
    }

    public List<Move> getLegalMoves(Piece piece) {
        switch (piece.getType()) {
            case PAWN:
                return PawnLogic.getLegalMoves(piece, this.chessBoard);
            case ROOK:
                return this.rookLogic.getLegalMoves(piece);
            case BISHOP:
                return this.bishopLogic.getLegalMoves(piece);
            case KING:
                return this.kingLogic.getLegalMoves(piece);
            case KNIGHT:
                return this.knightLogic.getLegalMoves(piece);
            case QUEEN:
                return this.queenLogic.getLegalMoves(piece);
            default:
                throw new IllegalArgumentException("What is this piece? Never seen before.");
        }
    }
    public int getScore(Piece p) {
        switch (p.getType()) {
            case PAWN:
                return 1;
            case ROOK:
                return 5;
            case KNIGHT:
                return 3;
            case BISHOP:
                return 3;
            case KING:
                return 0;
            case QUEEN:
                return 9;
            default:
                return 0;
        }
    }
}
