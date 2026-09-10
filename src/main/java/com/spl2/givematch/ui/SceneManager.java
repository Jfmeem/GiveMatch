package com.spl2.givematch.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public final class SceneManager {

    private SceneManager() {
    }

    public static void switchTo(String fxmlPath, String title) {
        try {
            Stage stage = AppContext.getInstance().getPrimaryStage();
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load screen: " + fxmlPath, e);
        }
    }
}
