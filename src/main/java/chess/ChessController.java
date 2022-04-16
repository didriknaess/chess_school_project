package chess;

import java.util.*;
import chess.datamodel.*;
import chess.io.BoardIO;
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
    @FXML public Text blackRemainingTime;
    @FXML public Text whiteRemainingTime;

    private Pane[][] board = new Pane[8][8];
    private GameLogic logic = new GameLogic();
    private Piece currentPiece;
    private List<Move> validMoves;
    private boolean hasSelected = false;
    private ImageIO imageLoader = new ImageIO();
    private BoardIO boardLoader = new BoardIO();
    private boolean paused = true;

    public ChessController() {
    }

    @FXML
    public void initialize() {
        logic.newGame();

        // sets up the multithreading for the class visually maintaining the player's remaining time
        logic.setTimers(600);
        TimeUpdater timeUpdater = new TimeUpdater();
        Thread timeThread = new Thread(timeUpdater);
        timeThread.setDaemon(true);
        timeThread.start();

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
        // updates
        updateText();
        pause.setWrapText(true);
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
            if (logic.getTurnCount() == 1 || paused) {
                logic.startTimer(logic.whoseTurn());
                this.paused = false;
            }
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
                logic.move(move);
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
    // implements runnable for multithreading so we can update the players respective timers every 1/10th of a second
    private class TimeUpdater implements Runnable {
        public TimeUpdater() {
        }
        @Override @FXML
        public void run() {
            while (true) {
                int blackTime = logic.getRemainingTime(Piece.Color.BLACK);
                int whiteTime = logic.getRemainingTime(Piece.Color.WHITE);
                if (blackTime <= 0 || whiteTime <= 0) break;
                String txt = "Remaining time: ";
                if (blackTime / 600 < 10) txt += "0";
                txt += (int)(blackTime / 600) + ":";
                blackTime = blackTime % 600;
                if (blackTime / 10 < 10) txt += "0";
                txt += (int)(blackTime / 10);
                blackTime = blackTime % 10;
                txt += "." + blackTime;
                blackRemainingTime.setText(txt);
                txt = "Remaining time: ";
                if (whiteTime / 600 < 10) txt += "0";
                txt += (int)(whiteTime / 600) + ":";
                whiteTime = whiteTime % 600;
                if (whiteTime / 10 < 10) txt += "0";
                txt += (int)(whiteTime / 10);
                whiteTime = whiteTime % 10;
                txt += "." + whiteTime;
                whiteRemainingTime.setText(txt);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
            // cannot do this on a different thread, must find a way to transfer to main thread
            boolean whiteWon = true;
            if (logic.getRemainingTime(Piece.Color.WHITE) <= 0) whiteWon = false;
            displayWinnerAndRestart(whiteWon, "won by opponent running out of time.");
        }

    }
    public void restart() {
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
    public void displayWinnerAndRestart(boolean whiteWon, String context) {
        restart();
        String toDisplay = "Congratulations! ";
        if (whiteWon) {
            toDisplay += "White ";
        } else {
            toDisplay += "Black ";
        }
        toDisplay += context;
        Alert alert = new Alert(AlertType.INFORMATION, toDisplay, ButtonType.OK);
        alert.setTitle("Game over");
        alert.showAndWait();
    }
    //Handling of buttons in the JavaFX Application:
    @FXML
    public void handlePause() {
        if (this.paused) {
            logic.startTimer(logic.whoseTurn());
            this.pause.setText("Pause");
            this.paused = false;
        } else {
            logic.pauseTimer(logic.whoseTurn());
            this.pause.setText("Resume");
            this.paused = true;
        }
        
    }
    @FXML
    public void handleUndo() {
        logic.undoTurn();

        for (int i = 0; i<8; i++) {
            for (int j = 0; j<8; j++) {
                Piece p = logic.getPiece(new Position(i, j));
                if (!board[i][j].getChildren().isEmpty()) {
                    board[i][j].getChildren().clear();
                }
                if (p != null) {
                    Image img = imageLoader.getImage(p);
                    ImageView view = new ImageView(img);
                    view.fitWidthProperty().bind(board[i][j].widthProperty()); 
                    view.fitHeightProperty().bind(board[i][j].heightProperty()); 
                    board[i][j].getChildren().add(view);
                }
            }
        }
        unselectBoard();
        updateText();
    }
    @FXML
    public void handleForfeit() {
        boolean whiteWon = true;
        if (logic.getTurnCount() % 2 != 0) whiteWon = false;
        displayWinnerAndRestart(whiteWon, "won by opponent forfeiting.");
    }
    @FXML
    public void handleRestart() {
        // asks for confimation to restart the game
        Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to restart the game?", ButtonType.OK, ButtonType.CANCEL);
        alert.setTitle("Confirmation");
        alert.showAndWait();
        if (alert.getResult() != ButtonType.OK) return;
        restart();
    }
    @FXML
    public void handleSave() {
        Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to save the game? This will overwrite your previous save.", ButtonType.OK, ButtonType.CANCEL);
        alert.setTitle("Confirmation");
        alert.showAndWait();
        if (!(alert.getResult() == ButtonType.OK)) return; 

        // save the game, overwriting the previous saved game
    }
    @FXML
    public void handleLoad() {
        Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to load a previous file? Your current game will be lost.", ButtonType.OK, ButtonType.CANCEL);
        alert.setTitle("Confirmation");
        alert.showAndWait();
        if (!(alert.getResult() == ButtonType.OK)) return;

        // load the saved game
    }
}
