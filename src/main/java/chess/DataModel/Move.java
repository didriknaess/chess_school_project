package chess.datamodel;

public class Move implements Comparable<Move> 
{
    private Position from;
    private Position to;
    
    public Move(Position from, Position to)
    {
        this.from = from;
        this.to = to;
    }

    public Position getFrom() {
        return from;
    }

    public Position getTo() {
        return to;
    }
    public boolean isEqual(Move move) {
        if (this.getFrom().getRow() == move.getFrom().getRow()
        && this.getFrom().getColumn() == move.getFrom().getColumn()
        && this.getTo().getRow() == move.getTo().getRow()
        && this.getTo().getColumn() == move.getTo().getColumn()) {
            return true;
        }
        return false;
    }

    @Override
    public int compareTo(Move other) {
        if (this.from == other.from && this.to == other.to) return 0;
        return -1;
    }

    @Override
    public String toString() {
        return "Move=[From:" + from + ", To:" + to + "]";
    }

}
