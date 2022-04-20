package chess.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import chess.datamodel.GameState;
import chess.datamodel.Piece;
import chess.datamodel.Piece.Color;


public class BoardIO implements IBoardIO {

    private List<String> pieces = new ArrayList<>();
    private Piece.Color color = Color.WHITE;
    private String p1Name;
    private String p2Name; // names of players are not neccesary!
    private int secondsRemainingP1;
    private int secondsRemainingP2; // use white/black instead of p1/p2? would be clearer
    
    public List<String> getPieces()
    {
        return this.pieces;
    }
    
    public void readFileOld(String filename)
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

    @Override
    public void readFile(String filename) throws FileNotFoundException
    {
        try (Scanner scanner = new Scanner(new File(getFilePath(filename))))
        {
            if (scanner.nextLine().equals("black")) this.color = Color.BLACK;
            this.p1Name = scanner.nextLine();
            this.p2Name = scanner.nextLine();
            this.secondsRemainingP1 = Integer.parseInt(scanner.nextLine());
            this.secondsRemainingP1 = Integer.parseInt(scanner.nextLine());
            while (scanner.hasNextLine())
            {
                this.pieces.add(toString(scanner.nextLine().split(",")));
            }
        }
    //     // try (BufferedReader br = new BufferedReader(new FileReader(new File(getFilePath(filename)))))
    //     // {
    //     //     if (br.readLine() == "black") this.color = Color.BLACK;
    //     //     this.p1Name = br.readLine();
    //     //     this.p2Name = br.readLine();
    //     //     this.secondsRemainingP1 = Integer.parseInt(br.readLine());
    //     //     this.secondsRemainingP2 = Integer.parseInt(br.readLine()); 
    //     //     this.pieces = Files.lines(Paths.get(getClass().getResource(filename).toURI()))
    //     //     .skip(5)
    //     //     .map(l -> l.split(",")) //split on "," as is done in the .txt file
    //     //     .map(n -> toString(n))  // use the private help method to make a string
    //     //     .toList(); //make list
    //     // }
    //     // try (BufferedReader br = new BufferedReader(new FileReader
    //     // ("C:/src/objekt_project/src/main/resources/saves/NormalChess.txt")))
    //     // {
    //     //     if (br.readLine() == "black") this.color = Color.BLACK;
    //     //     this.p1Name = br.readLine();
    //     //     this.p2Name = br.readLine();
    //     //     this.secondsRemainingP1 = Integer.parseInt(br.readLine());
    //     //     this.secondsRemainingP2 = Integer.parseInt(br.readLine()); 
    //     //     this.pieces = Files.lines(Paths.get(getClass().getResource(filename).toURI()))
    //     //     .skip(5)
    //     //     .map(l -> l.split(",")) //split on "," as is done in the .txt file
    //     //     .map(n -> toString(n))  // use the private help method to make a string
    //     //     .toList(); //make list
    //     // }
        catch (Exception e)
        {
            System.out.println(e.getStackTrace());
        }

    }

    @Override
    public void saveFile(String filename, GameState game) throws FileNotFoundException 
    {
        //Logic to save the state of the game
        try (PrintWriter writer = new PrintWriter(new File(getFilePath(filename))))
        {
            writer.println(game.getNumberOfTurns());
            for (Piece piece : game.getPieces()) 
            {
                writer.printf("%s,%s\n", piece.toString(), piece.getPosition().toString());
            }
        }
    }

    public static String getFilePath(String filename)
    {
        return BoardIO.class.getResource("/saves").getFile() + filename;
    }
    
    private String toString(String...lines)
    {
        return lines[0] + lines[1];
    }
    
    public static void main(String[] args) throws FileNotFoundException 
    {
        BoardIO br = new BoardIO();
        br.readFile("NormalChess.txt");
        // System.out.println(br.pieces);
        // System.out.println(br.p1Name);
        GameState game = new GameState();
        for (String string : br.pieces) {
            game.addPiece(Piece.createNewPiece(string));
        }
        br.saveFile("test.txt", game);
        System.out.println(game.getPieces());
    }
}
