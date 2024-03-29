package prashna;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/login.fxml"));
        primaryStage.setTitle("Prashna");
        primaryStage.setScene(new Scene(root, 1200, 700));
        primaryStage.getIcons().add(
                new Image(getClass().getResourceAsStream("/resources/logo32.png"))
        );
        primaryStage.getIcons().add(
                new Image(getClass().getResourceAsStream("/resources/logo64.png"))
        );
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
