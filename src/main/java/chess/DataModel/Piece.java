package chess.datamodel;

public class Piece 
{
    public enum Color
    {
        WHITE,
        BLACK
    } 
    
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
        this.position = pos;
        return this.position;
        // this.position.moveTo(pos);
        // return this.position;
        
    }

    public void setPosition(Position pos)
    {
        this.position = pos;
    }

    public boolean onBoard()
    {
        return this.position.isValid();
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
        }

        if (typeStr == typeStr.toLowerCase()) this.color = Color.BLACK;
        else this.color = Color.WHITE;
                    
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
