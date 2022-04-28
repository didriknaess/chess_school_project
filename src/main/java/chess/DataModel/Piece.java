package chess.datamodel;

import java.util.regex.Pattern;

public class Piece implements Comparable<Piece>
{
    //it is public to be used by other classes
    public enum Color
    {
        WHITE,
        BLACK
    } 
    //it is public to be used by other classes
    public enum PieceType
    {
        PAWN,
        ROOK,
        KNIGHT,
        BISHOP,
        QUEEN,
        KING
    }

    private Color color;
    private PieceType pieceType;
    private Position position;
    private int firstTurnMoved;

    public Piece(PieceType pieceType, Color color, Position position)
    {
        this.pieceType = pieceType;
        this.color = color;
        this.position = position;
        this.firstTurnMoved = -1;
    }

    public Piece()
    {

    }

    public static Piece createNewPiece(String piece) 
    {
        if (!Pattern.matches("[a-zA-Z]{1}[a-h]{1}[1-8]{1}", piece)) 
        {
            throw new IllegalArgumentException("Illegal string to create piece!");
        }
        String[] splitPiece = piece.split("");
        Piece returnPiece = new Piece();
        Position pos = new Position(splitPiece[1] + splitPiece[2]);

        returnPiece.setTypeAndColor(splitPiece[0]);
        returnPiece.setPosition(pos);
        returnPiece.setFirstTurnMoved(-1);
        return returnPiece;
    }

    public Color getColor()
    {
        return this.color;
    }

    public Position getPosition()
    {
        return this.position;
    }

    public PieceType getType()
    {
        return this.pieceType;
    }

    public int getFirstTurnMoved()
    {
        return this.firstTurnMoved;
    }

    public void setFirstTurnMoved(int n)
    {
        this.firstTurnMoved = n;
    }

    public Position moveTo(Position pos)
    {
        if (!pos.isValid()) throw new IllegalArgumentException("Invalid position on move!");
        this.position = pos;
        return this.position; 
    }

    public void setPosition(Position pos)
    {
        if (!pos.isValid()) throw new IllegalArgumentException("Invalid position to set!");
        this.position = pos;
    }

    private void setTypeAndColor(String typeStr) 
    {
        switch(typeStr.toLowerCase())
        {
            case "p":
                this.pieceType = PieceType.PAWN;
                break;
            case "r":
                this.pieceType = PieceType.ROOK;
                break;
            case "b":
                this.pieceType = PieceType.BISHOP;
                break;
            case "n":
                this.pieceType = PieceType.KNIGHT;
                break;
            case "q":
                this.pieceType = PieceType.QUEEN;
                break;
            case "k":
                this.pieceType = PieceType.KING;
                break;
            default:
                throw new IllegalArgumentException("No piece has this letter!");
        }
        if (typeStr == typeStr.toLowerCase()) this.color = Color.BLACK;
        else this.color = Color.WHITE;                
    }

    @Override
    public int compareTo(Piece piece)
    {
        if (this.color == piece.getColor() 
        && this.position.compareTo(piece.getPosition()) == 0 
        && this.pieceType == piece.getType()) return 0;
        return -1;
    }

    @Override
    public String toString()
    {
        String pieceChar = "";
        switch(this.pieceType)
        {
            case PAWN:
                pieceChar = "p";
                break;
            case ROOK:
                pieceChar = "r";
                break;
            case BISHOP:
                pieceChar = "b";
                break;
            case KNIGHT:
                pieceChar = "n";
                break;
            case QUEEN:
                pieceChar = "q";
                break;
            case KING:
                pieceChar = "k";
                break;
        }

        switch(this.color) 
        {
            case BLACK: return pieceChar.toLowerCase();
            case WHITE: return pieceChar.toUpperCase();
        }
        return "";
    }
}
