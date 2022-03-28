package chess.Unused;
import java.util.*;

public class Map {
    private ArrayList<ArrayList<OldPiece>> map;
    public Map(int dim) {
        this.map = new ArrayList<ArrayList<OldPiece>>(dim);
        for (ArrayList<OldPiece> l : this.map) {
            this.map.add(new ArrayList<OldPiece>(dim));
        }
    }
    public OldPiece get(int x, int y) {
        return this.map.get(x).get(y);
    }
    public Integer[] getCoords(OldPiece p) {
        MapIterator iterator = new MapIterator(this);
            while (iterator.hasNext()) {
                if (iterator.next().equals(p)) {
                    return iterator.getCoords(this);
                }
            }
            return null;
    }
    public ArrayList<ArrayList<OldPiece>> getMap() {
        return this.map;
    }
    public int getRowCount() {
        return this.map.size();
    }
    public int getColumnCount() {
        return this.map.get(0).size();
    }
}