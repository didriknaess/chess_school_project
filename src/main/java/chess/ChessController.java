package chess;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class ChessController 
{
    @FXML
    public Button pressMe;

    @FXML
    public GridPane chessBoard;

    @FXML
    public Text asdf;

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
    @FXML
    public void handleButtonPress(ActionEvent event) {

    }
    public static void main(String[] args) {
        
    }
}
