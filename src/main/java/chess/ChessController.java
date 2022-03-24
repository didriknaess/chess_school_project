package chess;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;

public class ChessController {
    
    @FXML
    public Button pressMe;

    @FXML
    public GridPane chessBoard;

    @FXML
    public void handlePressMe() {
        Alert alert = new Alert(AlertType.INFORMATION);
        
    }
    
}
