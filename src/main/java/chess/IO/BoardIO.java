package chess.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

import chess.datamodel.GameState;
import chess.datamodel.Move;
import chess.datamodel.Piece;
import chess.datamodel.Position;
import chess.datamodel.Piece.Color;

public class BoardIO implements IBoardIO {

    @Override
    public GameState loadFile(String filename) throws FileNotFoundException
    {
        GameState game = new GameState();
        try (Scanner scanner = new Scanner(new File(getFilePath(filename))))
        {
            //First 4 lines must always be of this format: 
            //whoseTurn, whiteSeconds, blackSeconds, number of turns
            Piece.Color color = Color.WHITE;
            if (scanner.nextLine().equals("black")) color = Color.BLACK;
            int secondsRemainingP1 = Integer.parseInt(scanner.nextLine());
            int secondsRemainingP2 = Integer.parseInt(scanner.nextLine());
            int turns = Integer.parseInt(scanner.nextLine());
            while (scanner.hasNextLine())
            {
                try {
                    String nextLine = scanner.nextLine();
                    if (nextLine.startsWith("#")) break; //Break when no more pieces left on the "board"
                    String[] split = nextLine.split(",");
                    Piece p = Piece.createNewPiece(toString(split[0], split[1]));
                    p.setFirstTurnMoved(Integer.parseInt(split[2]));
                    game.addPiece(p);    
                } catch (Exception e) {
                    break;
                }  
            }
            while (scanner.hasNextLine()) 
            {
                try {
                    String nextLine = scanner.nextLine();
                    if (nextLine.startsWith("#")) break;
                    String[] split = nextLine.split(",");
                    Piece p = Piece.createNewPiece(toString(split[0], split[1]));
                    p.setFirstTurnMoved(Integer.parseInt(split[2]));
                    game.addCapturedPiece(Integer.parseInt(split[3]), p);   
                } catch (Exception e) {
                    break;
                }
            }
            while (scanner.hasNextLine()) 
            {
                try {
                    String nextLine = scanner.nextLine();
                    if (nextLine.startsWith("#")) break;
                    String[] split = nextLine.split(",");
                    Piece p = Piece.createNewPiece(toString(split[0], split[1]));
                    p.setFirstTurnMoved(Integer.parseInt(split[2]));
                    game.addPromotedPawn(Integer.parseInt(split[3]), p);   
                } catch (Exception e) {
                    break;
                }
            }
            while (scanner.hasNextLine()) 
            {
                try {
                    String[] split = scanner.nextLine().split(",");
                    Position pos1 = new Position(split[0]);
                    Position pos2 = new Position(split[1]);
                    Move move = new Move(pos1, pos2);
                    game.addMove(move);
                } catch (Exception e) {
                    break;
                }
            }
            game.setWhoseTurn(color);
            game.setSecondsRemainingWhite(secondsRemainingP1);
            game.setSecondsRemainingBlack(secondsRemainingP2);
            game.setTurns(turns);   
        }
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
                writer.printf("%s,%s,%s\n", piece.toString(), piece.getPosition().toString(), 
                piece.getFirstTurnMoved());
            }
            writer.println("#capturedPieces");
            for (Piece piece : game.getCapturedPieces().values())
            {
                writer.printf("%s,%s,%s,%s\n", piece.toString(), piece.getPosition().toString(), 
                piece.getFirstTurnMoved(), getKeyByValue(game.getCapturedPieces(), piece) );
            }
            writer.println("#promotedPawns");
            for (Piece pawn : game.getPromotedPawns().values())
            {
                writer.printf("%s,%s,%s,%s\n", pawn.toString(), pawn.getPosition().toString(), 
                pawn.getFirstTurnMoved(), getKeyByValue(game.getPromotedPawns(), pawn));
            }
            writer.println("#moveHistory");
            for (Move move : game.getMoveHistory()) 
            {
                writer.printf("%s,%s\n", move.getFrom().toString(), move.getTo().toString());
            }
        }
    
    }

    public static String getFilePath(String filename)
    {
        return BoardIO.class.getResource("/saves").getFile() + filename;
    }

    //Method for acquiring the key from a value in a HashMap
    //This is not our code. The method is taken from this link:
    //https://stackoverflow.com/questions/1383797/java-hashmap-how-to-get-key-from-value
    private static <T, E> T getKeyByValue(HashMap<T, E> map, E value) {
        for (java.util.Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
    
    private String toString(String...lines)
    {
        return lines[0] + lines[1];
    } 
}
