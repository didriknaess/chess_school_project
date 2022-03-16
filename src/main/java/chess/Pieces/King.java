package chess.Pieces;

public class King extends Piece {
    //Check if King is moved, check if king is eligble for castle
    boolean isMoved;

    public King(boolean isWhite, char type, String startCoordinates) {
        super(isWhite, type, startCoordinates);
        this.isMoved = false; //not moved at the start
    }

}
