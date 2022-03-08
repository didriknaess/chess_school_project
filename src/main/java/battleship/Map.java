package battleship;

public class Map {
    // 'h' = hit ship, 's' = ship, 'm' = missed shot, null = empty
    private char[][] map = new char[10][10];
    public Map() {
    }
    public static void checkValidCoords(int[] coords) throws Error {
        if (coords.length != 2) throw new IllegalArgumentException("coords must consist of 2 values");
        if (coords[0] < 0 || coords[0] >= 10) throw new IllegalArgumentException("coords must be in range (0, 10)");
        if (coords[1] < 0 || coords[1] >= 10) throw new IllegalArgumentException("coords must be in range (0, 10)");
    }
    public void place(int[] coords, char object) {
        checkValidCoords(coords);
        this.map[coords[0]][coords[1]] = object;
    }
    public void shoot(int[] coords) {
        checkValidCoords(coords);
        if (this.getInfo(coords) == '\u0000') {
            this.map[coords[0]][coords[1]] = 'm';
        } else if (this.getInfo(coords) == 's') {
            this.map[coords[0]][coords[1]] = 'h';
        } else {
            throw new IllegalArgumentException("cant shoot where one has shot before");
        }
    }
    public void place(int[] coords) {
        checkValidCoords(coords);
        if (this.getInfo(coords) == '\u0000') {
            this.map[coords[0]][coords[1]] = 's';
        } else {
            throw new IllegalArgumentException("cant shoot where one has shot before");
        }
    }
    public char getInfo(int[] coords) {
        return this.map[coords[0]][coords[1]];
    }
}
