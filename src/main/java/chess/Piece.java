package chess;
import java.util.*;

public class Piece {
    protected Team team;
    protected char type;
    public Piece(Team team, char type) {
        this.type = type;
        this.team = team;
    }
    public boolean isWhite() {
        return this.team.isWhite();
    }
    public class Pawn extends Piece {
        public Pawn(Team team) {
            super(team, 'p');
        }
        public ArrayList<Integer[]> validMoves(Map map, int x, int y) {
            ArrayList<Integer[]> validCoords = new ArrayList<Integer[]>();
            Integer[] coords = map.getCoords(this);
            // Checks for Pawn
            if (!Team.inCheck()) {
                if (this.isWhite()) {
                    // First move can be double
                    if (coords[0] == 6 && map.get(coords[0]-1, coords[1]) == null && map.get(coords[0]-2, coords[1]) == null) {

                    // If not obstructed pawns can move forward
                    } else if (map.get(coords[0]-1, coords[1]) == null) {
                        
                    // If diagoally positioned against enemy, pawns can take
                    } else if (true) {

                    // An passant
                    } else if (true) {

                    }
                } else {

                }
            }
            return validCoords;
        }
        public boolean validMove(Map map, int x, int y) {
            return true;
        }
        public boolean prmotionPossible(Map map) {
            if (team.isWhite()) {
                if (map.getCoords(this)[0] == 0) return true;
                return false;
            } else {
                if (map.getCoords(this)[0] == 7) return true;
                return false;
            }
        }
    }
    public class King extends Piece {
        public King(Team team) {
            super(team, 'K');
        }
        public boolean validMove(Map map, int x, int y) {
            // checks for Pawn
            return true;
        }
    }
    public class Queen extends Piece {
        public Queen(Team team) {
            super(team, 'Q');
        }
        public boolean validMove() {
            // checks for Pawn
            return true;
        }
    }
    public class Rook extends Piece {
        public Rook(Team team) {
            super(team, 'R');
        }
        public boolean validMove() {
            // checks for Pawn
            return true;
        }
    }
    public class Bishop extends Piece {
        public Bishop(Team team) {
            super(team, 'B');
        }
        public boolean validMove() {
            // checks for Pawn
            return true;
        }
    }
    public class Knight extends Piece {
        public Knight(Team team) {
            super(team, 'k');
        }
        public boolean validMove() {
            // checks for Pawn
            return true;
        }
    }
}
