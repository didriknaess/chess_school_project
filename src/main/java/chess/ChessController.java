package chess;

import chess.io.BoardReader;
import chess.datamodel.*;
import chess.logic.GameLogic;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class ChessController {
    @FXML private GridPane chessBoardGraphic;
    @FXML public Button pressMe;
    @FXML public GridPane paneBoard;

    private Pane[][] board = new Pane[8][8];
    private Player currentPlayer;
    private GameLogic logic = new GameLogic();
    private BoardReader reader = new BoardReader();
    private Piece currentPiece;

    public ChessController() {
    }

    public void initialize() {
        for (Node node : chessBoardGraphic.getChildren()) {
            if (node instanceof Pane) {
                Pane pane = (Pane)node;
                board[(int)pane.getId().charAt(4)][(int)pane.getId().charAt(5)] = pane;
            }
        }
        logic.newGame();
        
    }
    public void handleMouseClick(MouseEvent e) {
        double width = chessBoardGraphic.getWidth();
        double height = chessBoardGraphic.getHeight();
        Position pos = new Position((int)(e.getX()/width*8), (int)(e.getY()/height*8));
        currentPiece = logic.getPiece(pos);
        if (logic.getPiece(pos) != null) {
            if (logic.getPiece(pos).getColor() == currentPlayer.getColor()) {
                Pane selectedPane = board[pos.getRow()][pos.getColumn()];
                if (logic.getValidMoves(currentPiece).contains(new Move(currentPiece.getPosition(), pos))) {
                    Pane oldPane = board[currentPiece.getPosition().getRow()][currentPiece.getPosition().getColumn()];
                    Image image = null;
                    Rectangle rect = null;
                }
            } else {
                return;
            }
        }
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
