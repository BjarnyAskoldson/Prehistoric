package com.company.GUI;

import com.company.Enumerations.Resource;
import com.company.Gameplay.Hex;
import com.company.Interfaces.Initialisable;
import com.company.hexgame;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.Serializable;
import java.util.Map;

public class HexScreen extends AnchorPane implements Initialisable, Serializable
{
    private Hex location;
    private Stage stage;

    @FXML
    private ListView<String> resourceListView = new ListView<>();

    @FXML
    private Label hexLabel = new Label();


    private void updateControls(){
        if (location!=null ) {
            hexLabel.setText("(" + location.getX() + "," + location.getY() + ") Terrain type: " + location.GetTerrain().name());
            resourceListView.getItems().clear();
            for (Map.Entry<Resource, Integer> res : location.getResources().entrySet())
                resourceListView.getItems().add(res.getKey().getName() + ": " + res.getValue());
        }
    }

    @Override
    public Object getEntity() {
        return location;
    }

    @Override
    public void initData(Object object, Object caller) {
        this.location = (Hex) object;
        updateControls();
    }

    public void show () {
        try {
            FXMLLoader fxmlLoader;
            fxmlLoader = new FXMLLoader(getClass().getResource("HexScreen.fxml"));
            Parent root = fxmlLoader.load();
            initData(location, null);
            ((Initialisable)(fxmlLoader.getController())).initData(location, null);
            ((HBox)((BorderPane)((AnchorPane) hexgame.map.primaryStage.getScene().getRoot()).getChildren().get(0)).getBottom()).getChildren().set(0,root);
        } catch (Exception e) {
            System.out.print("oopps");
        }

    }

    public void close() {
//        stage.hide();
    }
    public HexScreen(){

    }
    public HexScreen(Hex location) {
        this.location = location;

    }
}
