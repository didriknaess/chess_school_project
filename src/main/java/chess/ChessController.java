package chess;

import java.util.*;
import chess.datamodel.*;
import chess.io.ImageIO;
import chess.logic.GameLogic;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
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
    private List<Move> validMoves;
    private boolean hasSelected = false;
    private ImageIO imageLoader = new ImageIO();

    public ChessController() {
    }
    @FXML
    public void initialize() {
        logic.newGame();
        if (chessBoardGraphic == null) throw new Error("GridPane is empty!");
        // adds all panes to an array to make access and modification easier
        for (Node node : chessBoardGraphic.getChildren()) {
            if (node instanceof Pane) {
                Pane pane = (Pane)node;
                int x = (int)pane.getId().charAt(4) - '0';
                int y = (int)pane.getId().charAt(5) - '0';
                board[x][y] = pane;
            }
        }
        // gets images corresponding to the pieces and loads them in the designated places
        for (int i = 0; i<8; i++) {
            for (int j = 0; j<8; j++) {
                Piece p = logic.getPiece(new Position(i, j));
                if (p != null) {
                    Image img = imageLoader.getImage(p);
                    ImageView view = new ImageView(img);
                    view.fitWidthProperty().bind(board[i][j].widthProperty()); 
                    view.fitHeightProperty().bind(board[i][j].heightProperty()); 
                    board[i][j].getChildren().add(view);
                }
            }
        }
        // assigns images to the 
        updateText();
    }
    @FXML
    public void unselectBoard() {
        for (int i = 0; i<8; i++) {
            for (int j = 0; j<8; j++) {
                if ((i+j)%2==0) {
                    board[i][j].setStyle("-fx-background-color:wheat;");
                } else {
                    board[i][j].setStyle("-fx-background-color:goldenrod;");
                }
            }
        }
    }
    @FXML
    public void handleMouseClick(MouseEvent e) {
        double width = chessBoardGraphic.getWidth();
        double height = chessBoardGraphic.getHeight();
        Position pos = new Position((int)(e.getY()/width*8), (int)(e.getX()/height*8));
        System.out.println("Clicked at: ("+pos.getRow()+", "+pos.getColumn()+")");
        if (!hasSelected) {
            this.currentPiece = logic.getPiece(pos);
            if (currentPiece == null) return;
            if (currentPiece.getColor() != logic.whoseTurn()) return;
            Pane pane = board[pos.getRow()][pos.getColumn()];

            this.validMoves = logic.getValidMoves(currentPiece);

            if (validMoves == null || validMoves.isEmpty()) {
                System.out.println("No valid moves");
                return;
            }

            if ((pos.getRow()+pos.getColumn())%2==0) {
                pane.setStyle("-fx-background-color:chartreuse;");
            } else { //else if (pane.getStyle().equals("wheat"))
                pane.setStyle("-fx-background-color:lime;");
            }
            for (Move move : validMoves) {
                pane = board[move.getTo().getRow()][move.getTo().getColumn()];
                if ((move.getTo().getRow()+move.getTo().getColumn())%2==0) {
                    pane.setStyle("-fx-background-color:lightcoral;");
                } else { 
                    pane.setStyle("-fx-background-color:firebrick;");
                }
            }
            hasSelected = true;
        } else {
            // deselect current piece
            if (pos.getRow() == this.currentPiece.getPosition().getRow() && pos.getColumn() == this.currentPiece.getPosition().getColumn()) {
                unselectBoard();
                hasSelected = false;
                return;
            }
            if (logic.getPiece(pos) != null && logic.getPiece(pos).getColor() == currentPiece.getColor()) {
                System.out.println("Invalid move");
                return;
            }

            Pane selectedPane = board[pos.getRow()][pos.getColumn()];
            Move move = new Move(currentPiece.getPosition(), pos);
            if (logic.isValidMove(move)) {
                Pane oldPane = board[currentPiece.getPosition().getRow()][currentPiece.getPosition().getColumn()];
                Image img = null;
                for (Node node : oldPane.getChildren()) {
                    if (node instanceof ImageView) {
                        img = ((ImageView)node).getImage();
                        ((ImageView)node).setImage(null);
                        
                    }
                }
                boolean isOccupied = false;
                for (Node node : selectedPane.getChildren()) {
                    if (node instanceof ImageView) {
                        isOccupied = true;
                        ((ImageView)node).setImage(img);
                    }
                }
                if (!isOccupied) {
                    ImageView view = new ImageView(img);
                    view.fitWidthProperty().bind(selectedPane.widthProperty()); 
                    view.fitHeightProperty().bind(selectedPane.heightProperty()); 
                    selectedPane.getChildren().add(view);
                }
                logic.move(move, true);
                logic.endTurn();
                updateText();
                hasSelected = false;
                unselectBoard();
            } else {
                System.out.println("Invalid move");
            }
        }
    }
    //Changing Text corresponding to Score and which players Turn it is
    public void updateText() {
        blackScore.setText("Score: "+logic.getScore(Piece.Color.BLACK));
        whiteScore.setText("Score: "+logic.getScore(Piece.Color.WHITE));
        String foo = "";
        if (logic.isWhitePlaying()) {
            foo = "Turn " + logic.getTurnCount() + ": White";
            if (logic.inCheck(Piece.Color.WHITE)) foo += "(in check)";
        } else {
            foo = "Turn " + logic.getTurnCount() + ": Black";
            if (logic.inCheck(Piece.Color.BLACK)) foo += "(in check)";
        }
        whoseTurn.setText(foo);
    }
    //Handling of buttons in the JavaFX Application:
    @FXML
    public void handlePause() {
        Alert alert = new Alert(AlertType.INFORMATION);    
    }
    @FXML
    public void handleUndo() {
        logic.undoTurn();
        for (int i = 0; i<8; i++) {
            for (int j = 0; j<8; j++) {
                Piece p = logic.getPiece(new Position(i, j));
                if (p != null) {
                    Image img = imageLoader.getImage(p);
                    ImageView view = new ImageView(img);
                    view.fitWidthProperty().bind(board[i][j].widthProperty()); 
                    view.fitHeightProperty().bind(board[i][j].heightProperty()); 
                    board[i][j].getChildren().add(view);
                } else {
                    for (Node node : board[i][j].getChildren()) {
                        if (node instanceof ImageView) ((ImageView)node).setImage(null);
                    }
                }
            }
        }
        unselectBoard();
        updateText();
    }
    @FXML
    public void handleForfeit() {
        Alert alert = new Alert(AlertType.INFORMATION);    
    }
    @FXML
    public void handleRestart() {
        // asks for confimation to restart the game
        Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to restart the game?", ButtonType.OK, ButtonType.CANCEL);
        alert.setTitle("Confirmation");
        alert.showAndWait();
        if (!(alert.getResult() == ButtonType.OK)) return;
        
        // removes any ImageView children of panes from the previous game
        for (int i = 0; i<8; i++) {
            for (int j = 0; j<8; j++) {
                board[i][j].getChildren().clear();
            }
        }
        // ensures the turn is white (if not changes it), then re-initializes the game
        switch(logic.whoseTurn()) {
            case BLACK:
                logic.endTurn();
                this.initialize();
            case WHITE:
                this.initialize();
        }
        // corrects scores and taken pieces, then updates the display
        updateText();
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
