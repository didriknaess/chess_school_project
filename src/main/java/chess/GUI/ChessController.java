package chess.gui;

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
    public void handlePressMe() {
        Alert alert = new Alert(AlertType.INFORMATION);    
    }
    @FXML
    public void handleButtonPress(ActionEvent event) {

    }
}
