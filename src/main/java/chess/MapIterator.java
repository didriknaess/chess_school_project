package chess;
import java.util.*;
import java.util.Iterator;

public class MapIterator implements Iterator<Piece> {
    int x = 0;
    int y = 0;
    ArrayList<ArrayList<Piece>> map;
    public MapIterator(Map map) {
        this.map = map.getMap();
    }
    public void increment() {
        if (this.y < this.map.get(0).size()-1) {
            this.y++;
        } else {
            this.y = 0;
            this.x++;
        }
    }
    public Integer[] getCoords(Map map) {
        Integer[] toReturn = {x, y};
        return toReturn;
    }
    @Override
    public boolean hasNext() {
        try {
            if (this.y < this.map.get(0).size()-1) {
                this.map.get(x).get(y+1);
            } else {
                this.map.get(x+1).get(0);
            }
            return true;
        } catch (Exception ArrayIndexOutOfBoundsException) {
            return false;
        }
    }
    @Override
    public Piece next() {
        try {
            if (this.y < this.map.get(0).size()-1) {
                return this.map.get(x).get(y+1);
            } else {
                return this.map.get(x+1).get(0);
            }
        } finally {
            this.increment();
        }
    }
}