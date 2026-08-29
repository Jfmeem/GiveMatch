package com.spl2.givematch;

import com.spl2.givematch.db.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Touch DBConnection once at startup so schema/seed run before any screen loads.
        DBConnection.getInstance();

        Parent root = FXMLLoader.load(
                getClass().getResource("/com/spl2/givematch/fxml/login.fxml"));
        primaryStage.setTitle("GiveMatch");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
