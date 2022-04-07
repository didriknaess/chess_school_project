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
    private Player currentPlayer;
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
    public void handleMouseClick(MouseEvent e) {
        double width = chessBoardGraphic.getWidth();
        double height = chessBoardGraphic.getHeight();
        Position pos = new Position((int)(e.getX()/width*8), (int)(e.getY()/height*8));
        if (!hasSelected) {
            currentPiece = logic.getPiece(pos);
            if (currentPiece.getColor() == currentPlayer.getColor() || currentPiece == null) return;
            Pane pane = board[pos.getRow()][pos.getColumn()];
            Rectangle rect = new Rectangle(0, 0, pane.getWidth(), pane.getHeight());
            rect.setFill(Color.TRANSPARENT);
            rect.setStroke(Color.GREEN);
            pane.getChildren().add(rect);

            List<Move> moves = logic.getValidMoves(currentPiece);
            if (moves.isEmpty() || moves == null) {
                System.out.println("No valid moves");
            } else {
                for (Move move : moves) {
                    pane = board[move.getTo().getRow()][move.getTo().getColumn()];
                    rect = new Rectangle(0, 0, pane.getWidth(), pane.getHeight());
                    rect.setFill(Color.TRANSPARENT);
                    rect.setStroke(Color.RED);
                    pane.getChildren().add(rect);
                }
            }
            hasSelected = true;
        } else {
            if (logic.getPiece(pos) != null && logic.getPiece(pos).getColor() == currentPlayer.getColor()) return;
            
            Pane selectedPane = board[pos.getRow()][pos.getColumn()];
            if (logic.getValidMoves(currentPiece).contains(new Move(currentPiece.getPosition(), pos))) {
                Pane oldPane = board[currentPiece.getPosition().getRow()][currentPiece.getPosition().getColumn()];
                Image img = null;
                Rectangle rect = null;
                for (Node node : oldPane.getChildren()) {
                    if (node instanceof ImageView) {
                        img = ((ImageView)node).getImage();
                        ((ImageView)node).setImage(null);
                    }
                    if (node instanceof Rectangle) {
                        rect = (Rectangle)node;
                    }
                }
                oldPane.getChildren().removeAll(rect);
                for (Node node : selectedPane.getChildren()) {
                    if (node instanceof ImageView) {
                        ((ImageView)node).setImage(img);
                    }
                }
                // actually move the piece in the logic/game class
                hasSelected = false;
                // switch whose turn it is
                updateText();
            }
        }
    }
    //Changing Text corresponding to Score and which players Turn it is
    public void updateText() {
        blackScore.setText("Score: "+10);
        whiteScore.setText("Score: "+99);
        whoseTurn.setText("Placeholder text!");
        // switch (currentPlayer.getColor()) {
        //     case BLACK:
        //         whoseTurn.setText("Black's turn to move!");
        //     case WHITE:
        //         whoseTurn.setText("White's turn to move!");
        //     default:
        //         whoseTurn.setText("Undefined case!");
        // }
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
