package chess;
import java.util.*;

public class Map {
    private ArrayList<ArrayList<Piece>> map;
    public Map(int dim) {
        this.map = new ArrayList<ArrayList<Piece>>(dim);
        for (ArrayList<Piece> l : this.map) {
            this.map.add(new ArrayList<Piece>(dim));
        }
    }
    public Piece get(int x, int y) {
        return this.map.get(x).get(y);
    }
    public Integer[] getCoords(Piece p) {
        MapIterator iterator = new MapIterator(this);
            while (iterator.hasNext()) {
                if (iterator.next().equals(p)) {
                    return iterator.getCoords(this);
                }
            }
            return null;
    }
    public ArrayList<ArrayList<Piece>> getMap() {
        return this.map;
    }
    public int getRowCount() {
        return this.map.size();
    }
    public int getColumnCount() {
        return this.map.get(0).size();
    }
}