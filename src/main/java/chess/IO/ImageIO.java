package chess.io;

import java.net.MalformedURLException;
import java.net.URL;

import chess.datamodel.Piece;
import javafx.scene.image.Image;

public class ImageIO {
    public ImageIO() {
    }
    public Image getImage(Piece p) {
        switch(p.getColor()) {
            case BLACK:
                switch(p.getType()) {
                    case PAWN:
                        return new Image(getClass().getResourceAsStream("pics/BPawn.png"));
                    case ROOK:
                        return new Image(getClass().getResourceAsStream("pics/BRook.png"));
                    case BISHOP:
                        return new Image(getClass().getResourceAsStream("pics/BBishop.png"));
                    case KING:
                        return new Image(getClass().getResourceAsStream("pics/BKing.png"));
                    case KNIGHT:
                        return new Image(getClass().getResourceAsStream("pics/BKnight.png"));
                    case QUEEN:
                        return new Image(getClass().getResourceAsStream("pics/BQueen.png"));
            }
            case WHITE:
                switch(p.getType()) {
                    case PAWN:
                        return new Image(getClass().getResourceAsStream("pics/WPawn.png"));
                    case ROOK:
                        return new Image(getClass().getResourceAsStream("pics/WRook.png"));
                    case BISHOP:
                        return new Image(getClass().getResourceAsStream("pics/WBishop.png"));
                    case KING:
                        return new Image(getClass().getResourceAsStream("pics/WKing.png"));
                    case KNIGHT:
                        return new Image(getClass().getResourceAsStream("pics/WKnight.png"));
                    case QUEEN:
                        return new Image(getClass().getResourceAsStream("pics/WQueen.png"));
            }
        }
        return null;
    }
    public Image getImageTest(Piece p) {
        String imageFilePath = "";
        switch(p.getColor()) 
        {   
            case BLACK:
                switch(p.getType()) {
                    case PAWN:
                        imageFilePath = "BPawn.png";
                        break;
                    case ROOK:
                        imageFilePath = "BRook.png";
                        break;
                    case BISHOP:
                        imageFilePath = "BBishop.png";
                        break;
                    case KING:
                        imageFilePath = "BKing.png";
                        break;
                    case KNIGHT:
                        imageFilePath = "BKnight.png";
                        break;
                    case QUEEN:
                        imageFilePath = "BQueen.png";
                        break;
            }
            case WHITE:
                switch(p.getType()) {
                    case PAWN:
                        imageFilePath = "WPawn.png";
                        break;
                    case ROOK:
                        imageFilePath = "WRook.png";
                        break;
                    case BISHOP:
                        imageFilePath = "WBishop.png";
                        break;
                    case KING:
                        imageFilePath = "WKing.png";
                        break;
                    case KNIGHT:
                        imageFilePath = "WKnight.png";
                        break;
                    case QUEEN:
                        imageFilePath = "WQueen.png";
                        break;
            }
            // URL url = getClass().getResource("/drawIcon.png");
            // Image image = ImageIO.read(url);    
            // return image;
        }
        return null;
    }

    public static String getImageFilePath(String imageFilePath)
    {
        if (imageFilePath.isBlank()) throw new IllegalArgumentException("Can't find a file that is null");
        return ImageIO.class.getResource("/pics").getFile() + imageFilePath;
    }

    public static void main(String[] args) {
        System.out.println(getImageFilePath("WQueen.png"));
        ImageIO io = new ImageIO();
        io.getImage(Piece.createNewPiece("pa2"));
    }

}
