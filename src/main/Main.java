package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main java file.
 */
//JavaDoc folder is located in:
//Task 1 Software I -> javadoc
public class Main extends Application {

    /**
     * Load the primary stage.
     *
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(new Scene(root, 1500, 600));
        primaryStage.show();
    }

    /**
     * Main code.
     *
     * @param args
     */
    public static void main(String[] args){

        launch(args);
    }
}
