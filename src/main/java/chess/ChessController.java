package chess;

import java.io.FileNotFoundException;
import java.util.*;
import chess.datamodel.*;
import chess.io.ImageIO;
import chess.logic.GameLogic;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class ChessController {
    @FXML public AnchorPane canvas;
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
    private boolean paused = true;
    private boolean outOfTime = false;

    public ChessController() {
    }

    @FXML
    public void initialize() throws FileNotFoundException {
        logic.loadGame("NormalChess.txt");
        ButtonType three = new ButtonType("03:00");
        ButtonType ten = new ButtonType("10:00");
        ButtonType thirty = new ButtonType("30:00");
        // ButtonType none = new ButtonType("None");
        Alert alert = new Alert(AlertType.NONE, "Choose the amount of time each player should have at their disposal. Worth to note is that executing moves do not increace your remaining time. This has to be chosen for the game to initalize. ", three, ten, thirty);
        alert.setTitle("Time limit selection");
        alert.showAndWait();
        if (alert.getResult() == three) {
            logic.setTimers(180);
        } else if (alert.getResult() == ten) {
            logic.setTimers(600);
        } else if (alert.getResult() == thirty) {
            logic.setTimers(1800);
        } else {
            initialize();
        }
        // sets up the multithreading for the class visually maintaining the player's remaining time
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
                board[7-x][y] = pane;
            }
        }
        // updates the visuals, meaning the display of icons corresponding to pieces and the text such as score and remaining time.
        updateBoard();
        updateText();
        pause.setWrapText(true);
    }
    @FXML
    public void unselectBoard() {
        for (int i = 0; i<8; i++) {
            for (int j = 0; j<8; j++) {
                if ((i+j)%2==0) {
                    board[7-i][j].setStyle("-fx-background-color:wheat;");
                } else {
                    board[7-i][j].setStyle("-fx-background-color:goldenrod;");
                }
            }
        }
    }
    @FXML
    public void handleMouseClick(MouseEvent e) throws FileNotFoundException {
        double width = chessBoardGraphic.getWidth();
        double height = chessBoardGraphic.getHeight();
        System.out.println("Resolution: " + width + " x " + height);
        Position pos = new Position((int)(8-e.getY()/height*8), (int)(e.getX()/width*8));
        System.out.println("Clicked at: ("+pos.getRow()+", "+pos.getColumn()+")");
        // checks if the time has run out, and terminates the game if true
        if (logic.getTurnCount() == 1 || paused) {
            logic.startTimer(logic.whoseTurn());
            this.pause.setText("Pause");
            this.paused = false;
        }
        if (outOfTime) {
            if (logic.getRemainingTime(Piece.Color.BLACK) <= 0) {
                displayWinnerAndRestart(false, "by the opponent running out of time."); 
            } else {
                displayWinnerAndRestart(true, "by the opponent running out of time."); 
            }
            return;
        }

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
            // deselect current piece upon clicking the same pane
            if (pos.getRow() == this.currentPiece.getPosition().getRow() && pos.getColumn() == this.currentPiece.getPosition().getColumn()) {
                unselectBoard();
                hasSelected = false;
                return;
            }
            if (logic.getPiece(pos) != null && logic.getPiece(pos).getColor() == currentPiece.getColor()) {
                System.out.println("Invalid move");
                return;
            }
            Move move = new Move(currentPiece.getPosition(), pos);
            if (logic.isValidMove(move)) {
                logic.move(move);
                unselectBoard();
                if (logic.getPiece(pos).getType() == Piece.PieceType.PAWN) {
                    if ((logic.getPiece(pos).getColor() == Piece.Color.WHITE && pos.getRow() == 7) 
                    || (logic.getPiece(pos).getColor() == Piece.Color.BLACK && pos.getRow() == 0)) {
                        Piece.PieceType type = promotionAlert(pos);
                        logic.promote(pos, type);
                    }
                }
                logic.endTurn();
                updateBoard();
                updateText();
                hasSelected = false;
                
            } else {
                System.out.println("Invalid move");
            }
            // testing for check- and starmate, displays winner and restarts if true
            if (logic.noValidMoves(logic.whoseTurn())) {
                if (logic.inCheck(logic.whoseTurn())) {
                    boolean whiteWon = !logic.isWhitePlaying();
                    displayWinnerAndRestart(whiteWon, "by putting the opponent in checkmate. ");
                } else {
                    String context = "The game ended in a starmate. ";
                    if (logic.isWhitePlaying()) {
                        context += "White ";
                    } else {
                        context += "Black ";
                    }
                    context += "has no more valid moves, while not being in check. ";
                    Alert alert = new Alert(AlertType.INFORMATION, context, ButtonType.OK);
                    alert.setTitle("Game over");
                    alert.showAndWait();
                    restart();
                }
            }
        }
    }
    private Piece.PieceType promotionAlert(Position pos) {
        ButtonType queen = new ButtonType("Queen");
        ButtonType rook = new ButtonType("Rook");
        ButtonType bishop = new ButtonType("Bishop");
        ButtonType knight = new ButtonType("Knight");
        Alert alert = new Alert(AlertType.NONE, "Choose which piece you would like to promote your pawn to:", queen, rook, bishop, knight);
        alert.setTitle("Promotion");
        alert.showAndWait();
        if (alert.getResult() == queen) {
            return Piece.PieceType.QUEEN;
        } else if (alert.getResult() == rook) {
            return Piece.PieceType.ROOK;
        } else if (alert.getResult() == bishop) {
            return Piece.PieceType.BISHOP;
        } else if (alert.getResult() == knight) {
            return Piece.PieceType.KNIGHT;
        } else {
            // either undo or just redisplay the dialog window
            return promotionAlert(pos);
        }
    }
    
    //Changing Text corresponding to Score and which players Turn it is
    public void updateText() {
        blackScore.setText("Score: "+logic.getScore(Piece.Color.BLACK));
        whiteScore.setText("Score: "+logic.getScore(Piece.Color.WHITE));
        if (paused) pause.setText("Resume");
        else pause.setText("Pause");
        String foo = "";
        if (logic.isWhitePlaying()) {
            foo = "Turn " + logic.getTurnCount() + ": White";
            if (logic.inCheck(Piece.Color.WHITE)) foo += " (check)";
        } else {
            foo = "Turn " + logic.getTurnCount() + ": Black";
            if (logic.inCheck(Piece.Color.BLACK)) foo += " (check)";
        }
        whoseTurn.setText(foo);
    }
    public void updateBoard() {
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
                
                blackRemainingTime.setText(formatTimerText(blackTime));
                whiteRemainingTime.setText(formatTimerText(whiteTime));

                if (blackTime <= 0 || whiteTime <= 0) break;

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
            // cannot do this on a different thread, must find a way to transfer to main thread
            outOfTime = true;
        }
        private String formatTimerText(int time) {
            String txt = "Remaining time: ";
            if (time / 600 < 10) txt += "0";
            txt += (int)(time / 600) + ":";
            time = time % 600;
            if (time / 10 < 10) txt += "0";
            txt += (int)(time / 10);
            time = time % 10;
            txt += "." + time;
            return txt;
        }

    }
    public void restart() throws FileNotFoundException {
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
            default: 
                this.initialize();
        }
        // corrects scores and taken pieces, then updates the display
        updateText();
        unselectBoard();
        this.hasSelected = false;
    }
    public void displayWinnerAndRestart(boolean whiteWon, String context) throws FileNotFoundException {
        handlePause();
        String toDisplay = "Congratulations! ";
        if (whiteWon) {
            toDisplay += "White won ";
        } else {
            toDisplay += "Black won ";
        }
        toDisplay += context;
        Alert alert = new Alert(AlertType.INFORMATION, toDisplay, ButtonType.OK);
        alert.setTitle("Game over");
        alert.showAndWait();
        restart();
    }
    private String askForFilename(String defaultValue, boolean saving) {
        TextInputDialog inputFilename = new TextInputDialog(defaultValue);
        inputFilename.setHeaderText("Write the name of the file you would like to load from.\nThis will delete any unsaved data in your current game.\nMake sure to input a valid filename.\n\n");
        if (saving) inputFilename.setHeaderText("Write the name of the file you would like to print to.\nIf an existing save has the same name it will be overwritten.\nIf not, a new save will be created.\n\n");
        inputFilename.showAndWait();
        String filename = inputFilename.getResult();
        System.out.println(filename);

        if (filename == null) return "";
        if(!filename.endsWith(".txt")) filename += ".txt";
        
        return filename;
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
        updateText();
    }
    @FXML
    public void handleUndo() {
        logic.undoTurn();
        unselectBoard();
        updateBoard();
        updateText();
    }
    @FXML
    public void handleForfeit() throws FileNotFoundException {
        Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to forfeit?", ButtonType.OK, ButtonType.CANCEL);
        alert.setTitle("Confirmation");
        alert.showAndWait();
        if (alert.getResult() != ButtonType.OK) return;
        
        boolean whiteWon = true;
        if (logic.getTurnCount() % 2 != 0) whiteWon = false;
        displayWinnerAndRestart(whiteWon, "by opponent forfeiting.");
    }
    @FXML
    public void handleRestart() throws FileNotFoundException {
        if (!paused) {
            logic.pauseTimer(logic.whoseTurn());
            this.pause.setText("Resume");
        }
        // asks for confimation to restart the game
        Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to restart the game?", ButtonType.OK, ButtonType.CANCEL);
        alert.setTitle("Confirmation");
        alert.showAndWait();
        if (alert.getResult() != ButtonType.OK) return;
        restart();
    }
    @FXML
    public void handleSave() throws FileNotFoundException {
        if (!paused) {
            logic.pauseTimer(logic.whoseTurn());
            this.pause.setText("Resume");
        }
        // asks for name of file to write the savegame to
        String filename = askForFilename("save.txt", true);
        while (filename.equals("NormalChess.txt")) {
            filename = askForFilename("NormalChess.txt", true);
        }
        if (filename.equals("")) return;
        // saves to the spesified file
        logic.saveGame(filename);
    }
    @FXML
    public void handleLoad() {
        if (!paused) {
            logic.pauseTimer(logic.whoseTurn());
            this.pause.setText("Resume");
        }
        // asks for file to load from
        String filename = askForFilename("NormalChess.txt", false);
        if (filename.equals("")) return;

        // load the saved game
        try {
            logic.loadGame(filename);
        } catch (Exception FileNotFoundException) {
            handleLoad();
        }
        
        // sets up the multithreading for the class visually maintaining the player's remaining time
        TimeUpdater timeUpdater = new TimeUpdater();
        Thread timeThread = new Thread(timeUpdater);
        timeThread.setDaemon(true);
        timeThread.start();
        updateBoard();
        updateText();
        pause.setWrapText(true);
    }
}