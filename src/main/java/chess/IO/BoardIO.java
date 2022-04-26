package chess.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import chess.datamodel.GameState;
import chess.datamodel.Piece;
import chess.datamodel.Piece.Color;

public class BoardIO implements IBoardIO {

    public GameState readFileOld(String filename)
    {
        GameState game = new GameState();
        List<String> list = new ArrayList<>();
        try 
        {
             list = Files.lines(Paths.get(getClass().getResource(filename).toURI()))
            .skip(4)
            .map(l -> l.split(",")) //split on "," as is done in the .txt file
            .map(n -> toString(n))  // use the private help method to make a string
            .toList(); //make list
        } 
        catch (Exception e)
        {
            System.out.println(e.getStackTrace());
        }
        for (String string : list) {
            game.addPiece(Piece.createNewPiece(string));
        }
        return game;
    }

    @Override
    public GameState loadFile(String filename) throws FileNotFoundException
    {
        GameState game = new GameState();
        try (Scanner scanner = new Scanner(new File(getFilePath(filename))))
        {
            Piece.Color color = Color.WHITE;
            if (scanner.nextLine().equals("black")) color = Color.BLACK;
            int secondsRemainingP1 = Integer.parseInt(scanner.nextLine());
            int secondsRemainingP2 = Integer.parseInt(scanner.nextLine());
            int turns = Integer.parseInt(scanner.nextLine());
            while (scanner.hasNextLine())
            {
                String[] split = scanner.nextLine().split(",");
                Piece p = Piece.createNewPiece(toString(split[0], split[1]));
                p.setFirstTurnMoved(Integer.parseInt(split[2]));
                game.addPiece(p);
            }
            game.setWhoseTurn(color);
            game.setSecondsRemainingWhite(secondsRemainingP1);
            game.setSecondsRemainingBlack(secondsRemainingP2);
            game.setTurns(turns);   
        }
        // catch (Exception e)
        // {
        //     System.out.println(e.getStackTrace());
        // }

        return game;
    }

    @Override
    public void saveFile(String filename, GameState game) throws FileNotFoundException 
    {
        //Logic to save the state of the game
        try (PrintWriter writer = new PrintWriter(new File(getFilePath(filename))))
        {
            writer.println(game.savingGetWhoseTurn());
            writer.println(game.getWhiteSeconds());
            writer.println(game.getBlackSeconds());
            writer.println(game.getNumberOfTurns());
            for (Piece piece : game.getPieces()) 
            {
                writer.printf("%s,%s,%s\n", piece.toString(), piece.getPosition().toString(), piece.getFirstTurnMoved());
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
        GameState gm = br.loadFile("NormalChess.txt");
        for (Piece piece : gm.getPieces()) {
            System.out.println(piece.getFirstTurnMoved());
        }
    }  
}
