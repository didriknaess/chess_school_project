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

    @Override
    public int compareTo(Move other) {
        if (this.from.equals(other.from) && this.to.equals(other.to)) return 0;
        return -1;
    }
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Move)) return false;
        Move m = (Move)o;
        if (this.getFrom().equals(m.getFrom()) && this.getTo().equals(m.getTo())) return true;
        return false;
    }

    @Override
    public String toString() {
        return "Move=[From:" + from + ", To:" + to + "]";
    }

}