package chess;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class ChessApp extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Chess");
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(640);
        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("ChessGUI.fxml"))));
        primaryStage.show();
    }
}
