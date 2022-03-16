package chess.Pieces;

public class Piece {
    boolean isWhite;
    char type;
    String coordinates;
    
    public Piece(boolean isWhite, char type, String startCoordinates){
        this.isWhite = isWhite;
        this.type = type;
        this.coordinates = startCoordinates;
    }
    
    //getter for the color of the piece
    public boolean isWhite() {
        return this.isWhite;
    }

    //getter for the type of the piece
    public char getType() {
        return this.type;
    }

    //getter for the coords of the piece
    public String getCoordinates() {
        return this.coordinates;
    }

    public class Pawn extends Piece{
        public Pawn(boolean isWhite, char type, String startCoordinate){
            super(isWhite, type, startCoordinate);
        }

        //Could be void, but maybe we need to check if it was moved...
        public boolean move(String newCoords){
            if (isValidMove(newCoords)){
                //do something to make the piece move
                return true;
            }
            return false;
        }

        private boolean isValidMove(String cord) {
            //check if this is a valid move
            return true;
        }

    }
}

