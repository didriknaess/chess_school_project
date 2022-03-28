package chess.IO;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class BoardReader 
{
    private List<String> pieces = new ArrayList<>();

    public List<String> getPieces()
    {
        return this.pieces;
    }

    public void readFile(String filename)
    {
        try 
        {
            this.pieces = Files.lines(Paths.get(getClass().getResource(filename).toURI()))
            .map(l -> l.split(",")) //split on "," as is done in the .txt file
            .map(n -> toString(n))  // use the private help method to make a string
            .toList(); //make list
        } 
        catch (Exception e)
        {
            System.out.println(e.getStackTrace());
        }
    }

    private String toString(String...lines)
    {
        return lines[0] + lines[1];
    }

    public static void main(String[] args) {
        BoardReader br = new BoardReader();
        br.readFile("NormalChess.txt");
        System.out.println(br.pieces);
    }

}
