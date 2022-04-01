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
        if (this.from == other.from && this.to == other.to) return 0;
        return -1;
    }

    @Override
    public String toString() {
        return "Move [to=" + to + "]";
    }

    
    
    
}
