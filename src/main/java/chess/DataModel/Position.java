package chess.datamodel;

import java.util.regex.Pattern;

public class Position implements Comparable<Position>
{
    private int row;
    private int column;

    public Position(int row, int column)
    {
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

    public boolean isValid()
    {
        return (!(this.row < 0 || this.row > 7 || this.column < 0 || this.column > 7));
    }

    public boolean isValid(int row, int column)
    {
        return (!(row < 0 || row > 7 || column < 0 || column > 7));
    }

    //The method below is copied from stackoverflow via this url:
    // https://stackoverflow.com/questions/10813154/how-do-i-convert-a-number-to-a-letter-in-java
    // method to help convert an int to a letter, made it alot easier for us when saving and making a board
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
    public boolean equals(Object o) {
        boolean equal = true;
        if (!(o instanceof Position)) equal = false;
        Position pos = (Position)o;
        if (pos.getRow() != this.getRow()) equal = false;
        if (pos.getColumn() != this.getColumn()) equal = false;
        return equal;
    }

    @Override
    public String toString() {
        return this.getPosString();
    }

    public static void main(String[] args) {
        Position pos = new Position(-2, 2);
        System.out.println(pos);
    }
}
