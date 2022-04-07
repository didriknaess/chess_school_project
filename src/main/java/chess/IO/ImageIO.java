package chess.io;

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
}
