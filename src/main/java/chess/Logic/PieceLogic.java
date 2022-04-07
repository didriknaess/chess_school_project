package chess.logic;

import java.util.List;

import chess.datamodel.ChessBoard;
import chess.datamodel.Move;
import chess.datamodel.Piece;

public class PieceLogic //This class is to help prevent having to make new instances of ...logic in GameLogic
//Want to delegate most of GameLogics tasks to other helping functions.
//Makes the GameLogic easier to read, for details you move into the other subclasses
{
    private PawnLogic pawnLogic;
    private RookLogic rookLogic;
    private BishopLogic bishopLogic;
    private KnightLogic knightLogic;
    private QueenLogic queenLogic;
    private KingLogic kingLogic;

    public PieceLogic(ChessBoard chessBoard)
    {
        this.pawnLogic = new PawnLogic(chessBoard);
        this.rookLogic = new RookLogic(chessBoard);
        this.bishopLogic = new BishopLogic(chessBoard);
        this.knightLogic = new KnightLogic(chessBoard);
        this.queenLogic = new QueenLogic(chessBoard);
        this.kingLogic = new KingLogic(chessBoard);
    }

    public List<Move> getValidMoves(Piece piece)
    {
        switch (piece.getType())
        {
            case PAWN:
                return this.pawnLogic.getValidMoves(piece);
            case ROOK:
                return this.rookLogic.getValidMoves(piece);
            case BISHOP:
                return this.bishopLogic.getValidMoves(piece);
            case KING:
                return this.kingLogic.getValidMoves(piece);
            case KNIGHT:
                return this.knightLogic.getValidMoves(piece);
            case QUEEN:
                return this.queenLogic.getValidMoves(piece);
            default:
                throw new IllegalArgumentException("What is this piece? Never seen before.");
        }
    }
    public int getScore(Piece p) {
        switch (p.getType()) {
            case PAWN:
                return 1;
            case ROOK:
                return 3;
            case BISHOP:
                return 3;
            case KING:
                return 0;
            case KNIGHT:
                return 5;
            case QUEEN:
                return 9;
            default:
                return 0;
        }
    }
}
