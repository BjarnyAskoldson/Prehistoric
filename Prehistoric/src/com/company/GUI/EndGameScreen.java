package com.company.GUI;

import com.company.hexgame;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EndGameScreen{
    public void show() {
        try {
            FXMLLoader fxmlLoader;
            fxmlLoader = new FXMLLoader(getClass().getResource("EndGameScreen.fxml"));
            Parent root = fxmlLoader.load();
            hexgame.getMainTimer().getScreensToRefresh().add(fxmlLoader.getController());
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            System.out.print("oopps");
        }
    }
}
