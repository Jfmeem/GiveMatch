package com.spl2.givematch;

import com.spl2.givematch.dao.DatabaseManager;
import com.spl2.givematch.db.DBConnection;
import com.spl2.givematch.ui.AppContext;
import com.spl2.givematch.ui.SceneManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        DatabaseManager.initialize();
        AppContext.getInstance().setPrimaryStage(primaryStage);
        SceneManager.switchTo("/fxml/login.fxml", "GiveMatch — Login");
    }

    public static void main(String[] args) {
        launch(args);
    }
}