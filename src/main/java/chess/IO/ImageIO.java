package chess.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import chess.datamodel.Piece;
import chess.datamodel.Piece.Color;
import javafx.scene.image.Image;

public class ImageIO {
    
    public Image getImage(Piece p) {
        String imageFilePath = "";
        if (p.getColor() == Color.BLACK)
        {
            switch(p.getType()) 
            {
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
        }
        else
        {
            switch(p.getType()) 
            {
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
        }
        InputStream is = null;
        try {
            is = new FileInputStream(getImageFilePath(imageFilePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Image image = new Image(is);
        
        return image;
    }

    public static String getImageFilePath(String imageFilePath)
    {
        if (imageFilePath.isBlank()) throw new IllegalArgumentException("Can't find a file that is null");
        return ImageIO.class.getResource("/pics").getFile() + imageFilePath;
    }

}
