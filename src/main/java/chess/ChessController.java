package chess;

import java.util.*;
import chess.datamodel.*;
import chess.datamodel.Piece.PieceType;
import chess.io.ImageIO;
import chess.logic.GameLogic;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class ChessController {
    @FXML public GridPane chessBoardGraphic;
    @FXML public Button pause;
    @FXML public Button undo;
    @FXML public Button forfeit;
    @FXML public Button save;
    @FXML public Button load;
    @FXML public Text whoseTurn;
    @FXML public Text blackScore;
    @FXML public Text whiteScore;

    private Pane[][] board = new Pane[8][8];
    private GameLogic logic = new GameLogic();
    private Piece currentPiece;
    private boolean hasSelected = false;
    private ImageIO imageLoader = new ImageIO();

    public ChessController() {
    }
    @FXML
    public void initialize() {
        updateText();
        logic.newGame();
        System.out.println(logic);
        if (chessBoardGraphic == null) throw new Error("GridPane is empty!");
        for (Node node : chessBoardGraphic.getChildren()) {
            if (node instanceof Pane) {
                Pane pane = (Pane)node;
                int x = (int)pane.getId().charAt(4) - '0';
                int y = (int)pane.getId().charAt(5) - '0';
                board[x][y] = pane;
            }
        }

        for (int i = 0; i<8; i++) {
            for (int j = 0; j<8; j++) {
                Piece p = logic.getPiece(new Position(i, j));
                if (p != null) {
                    Image img = imageLoader.getImage(p);
                    ImageView view = new ImageView(img);
                    board[i][j].getChildren().add(view);
                    view.fitHeightProperty();
                    view.fitWidthProperty();
                }
            }
        }
    }
    @FXML
    public void unselectBoard() {
        for (int i = 0; i<8; i++) {
            for (int j = 0; j<8; j++) {
                if ((i+j)%2==0) {
                    board[i][j].setStyle("-fx-background-color:wheat;");
                } else {
                    board[i][j].setStyle("-fx-background-color:golderod;");
                }
            }
        }
    }
    @FXML
    public void handleMouseClick(MouseEvent e) {
        double width = chessBoardGraphic.getWidth();
        double height = chessBoardGraphic.getHeight();
        Position pos = new Position((int)(e.getX()/width*8), (int)(e.getY()/height*8));
        System.out.println("Clicked at: ("+pos.getRow()+", "+pos.getColumn()+")");
        if (!hasSelected) {
            System.out.println("Accessed select");
            currentPiece = logic.getPiece(pos);
            if (currentPiece == null || currentPiece.getColor() == logic.whoseTurn()) return;
            Pane pane = board[pos.getRow()][pos.getColumn()];
            if ((pos.getRow()+pos.getColumn())%2==0) {
                pane.setStyle("-fx-background-color:chartreuse;");
            } else { //else if (pane.getStyle().equals("wheat"))
                pane.setStyle("-fx-background-color:lime;");
            }

            List<Move> moves = logic.getValidMoves(currentPiece);
            if (moves.isEmpty() || moves == null) {
                System.out.println("No valid moves");
                return;
            } else {
                for (Move move : moves) {
                    pane = board[move.getTo().getRow()][move.getTo().getColumn()];
                    if ((move.getTo().getRow()+move.getTo().getColumn())%2==0) {
                        pane.setStyle("-fx-background-color:lightcoral;");
                    } else { 
                        pane.setStyle("-fx-background-color:firebrick;");
                    }
                }
                hasSelected = true;
            }
        } else {
            System.out.println("Accessed hasSelected");
            if (currentPiece.getPosition().equals(pos)) {
                unselectBoard();
                hasSelected = false;
                return;
            }
            if (logic.getPiece(pos) != null && logic.getPiece(pos).getColor() == currentPiece.getColor()) return;
            
            Pane selectedPane = board[pos.getRow()][pos.getColumn()];
            if (logic.getValidMoves(currentPiece).contains(new Move(currentPiece.getPosition(), pos))) {
                Pane oldPane = board[currentPiece.getPosition().getRow()][currentPiece.getPosition().getColumn()];
                Image img = null;
                for (Node node : oldPane.getChildren()) {
                    if (node instanceof ImageView) {
                        img = ((ImageView)node).getImage();
                        ((ImageView)node).setImage(null);
                    }
                }
                for (Node node : selectedPane.getChildren()) {
                    if (node instanceof ImageView) {
                        ((ImageView)node).setImage(img);
                    }
                }
                // actually move the piece in the logic/game class
                logic.endTurn();
                updateText();
            // deselection of currentPiece
            } else {
                System.out.println("Invalid move");
            }
            hasSelected = false;
        }
    }
    //Changing Text corresponding to Score and which players Turn it is
    public void updateText() {
        blackScore.setText("Score: "+0);
        whiteScore.setText("Score: "+0);
        whoseTurn.setText("Placeholder text!");
        if(logic.isWhitePlaying()) whoseTurn.setText("Turn: WHITE");
        else whoseTurn.setText("Turn: BLACK");
    }
    //Handling of buttons in the JavaFX Application:
    @FXML
    public void handlePause() {
        Alert alert = new Alert(AlertType.INFORMATION);    
    }
    @FXML
    public void handleUndo() {
        Alert alert = new Alert(AlertType.INFORMATION);    
    }
    @FXML
    public void handleForfeit() {
        Alert alert = new Alert(AlertType.INFORMATION);    
    }
    @FXML
    public void handleSave() {
        Alert alert = new Alert(AlertType.INFORMATION);    
    }
    @FXML
    public void handleLoad() {
        Alert alert = new Alert(AlertType.INFORMATION);    
    }
}
