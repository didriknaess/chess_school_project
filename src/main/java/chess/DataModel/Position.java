package chess.datamodel;

import java.util.regex.Pattern;

public class Position implements Comparable<Position>
{
    private int row;
    private int column;

    public Position(int row, int column)
    {
        if (!isValid()) throw new IllegalArgumentException("Illegal position");
        this.row = row;
        this.column = column;
    }

    public Position(String pos)
    {
        if (pos.length() != 2 || !Pattern.matches("[a-h]{1}[1-8]{1}", pos)) 
        {
            throw new IllegalArgumentException("Illegal position (string)");
        }
        char colChar = pos.charAt(0);
        char rowChar = pos.charAt(1);
        this.column = colChar - 'a';
        this.row = rowChar - '1';
    }
    
    public int getRow()
    {
        return this.row;
    }

    public int getColumn()
    {
        return this.column;
    }

    public String getPosString()
    {
        return toAlphabetic(this.column).toLowerCase() + (this.row + 1);
    }

    public Position getNewPosition(int verticalMove, int horizontalMove)
    {
        Position returnPosition = new Position(this.row + verticalMove, this.column + horizontalMove);
        return returnPosition;
    }

    public void moveTo(Position pos)
    {
        if (!this.isValid()) throw new IllegalArgumentException("Can't move a piece that is not in play");
        if (!pos.isValid()) throw new IllegalArgumentException("Illegal position on move");
        this.row = pos.row;
        this.column = pos.column;
    }

    public boolean isValid()
    {
        return (!(this.row < 0 || this.row > 7 || this.column < 0 || this.column > 7));
    }

    public void setCaptured()
    {
        this.column = -1;
        this.row = -1;
    }

    // https://stackoverflow.com/questions/10813154/how-do-i-convert-a-number-to-a-letter-in-java
    // method to help convert an int to a letter
    private static String toAlphabetic(int i) {
        if( i<0 ) {
            return "-"+toAlphabetic(-i-1);
        }
        int quot = i/26;
        int rem = i%26;
        char letter = (char)((int)'A' + rem);
        if( quot == 0 ) {
            return ""+letter;
        } else {
            return toAlphabetic(quot-1) + letter;
        }
    }

    @Override
    public int compareTo(Position pos) {
        if (this.getColumn() == pos.getColumn())
        {
            if (this.row > pos.row) return 1;
            if (this.row < pos.row) return -1;
            return 0;
        }
        if (this.column < pos.column)
        {
            return -1;
        }
        return 1;
    }

    @Override
    public String toString() {
        return this.getPosString();
    }

    public static void main(String[] args)
    {
        Position position = new Position("h3");
        System.out.println(position.getColumn());
        System.out.println(position.getRow());
        System.out.println(position.getPosString());
    }
}
