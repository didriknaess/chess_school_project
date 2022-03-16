package chess;

public class Piece {
    boolean isWhite;
    char type;
    public Piece(boolean isWhite, char type) {
        this.type = type;
        this.isWhite = isWhite;
    }
    public class Pawn extends Piece {
        public Pawn(boolean isWhite) {
            super(isWhite, 'p');
        }
        public boolean isWhite() {
            return this.isWhite;
        }
        public boolean validMove() {
            // checks for Pawn
            return true;
        }
    }
}
