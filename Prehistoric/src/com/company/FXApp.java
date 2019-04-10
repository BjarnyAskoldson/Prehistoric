package com.company;

import com.company.Enumerations.Profession;
import com.company.GUI.MainScreen;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javafx.scene.Group;

import java.util.HashSet;

public class FXApp extends Application {

    public Stage primaryStage;
    public MainScreen mainScreenController;
    public static Pane root;
    private HashSet<Profession> professions = new HashSet<>();
    public static void main(String[] args) {
        launch(args);
    }

    public FXApp() {}
    @Override
    public void start(Stage primaryStage) throws Exception {
        //Initiate Profession members, - we need them before we go to commodities, equipments etc.
        for (Profession pro : Profession.values())
            professions.add(pro);

//        WelcomeScreen welcomeScreen = new WelcomeScreen();
//        welcomeScreen.show();
        HexMap.initMap();
        hexgame.initGame();
        this.primaryStage = primaryStage;
        Group mapNode = hexgame.map.getMapPanel();

        ScrollPane mapPane = new ScrollPane(mapNode);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GUI/MainScreen.fxml"));
        root = fxmlLoader.load();
        mainScreenController = fxmlLoader.getController();
        hexgame.mainScreen = mainScreenController;
        mainScreenController.updateControls();
        mapPane.setPannable(true);

        mapPane.setPrefSize(1024, 600);
        //((ScrollPane)((BorderPane)root.getChildren().get(0)).getCenter()).setContent(mapPane);
        ((ScrollPane)((StackPane)((BorderPane)root.getChildren().get(0)).getCenter()).getChildren().get(0)).setContent(mapPane);

        primaryStage.setTitle("Prehistoric 0.1");
//        primaryStage.setScene(new Scene(root, 1024, 600));
        primaryStage.setScene(new Scene(root));
        hexgame.map.primaryStage = primaryStage;

        HexMap.updateMapPanel();
        primaryStage.show();
//        welcomeScreen.stage.requestFocus();




    }
}
