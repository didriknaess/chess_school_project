package chess.datamodel;

public class Move
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
