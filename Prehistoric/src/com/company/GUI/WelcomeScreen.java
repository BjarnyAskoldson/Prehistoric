package com.company.GUI;

import com.company.Gameplay.Tribe;
import com.company.hexgame;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class WelcomeScreen// extends JPanel
{
    @FXML
    private TextField tribeTextField = new TextField("Mighty Donuts");
    @FXML
    private TextField villageTextField = new TextField("Donutville");

    public Stage stage;

//    public String getTribeName() {
//        return tribeTextField.getText();
//    }
//    public String getFirstVillageName () {
//        return  villageTextField.getText();
//    }

    @FXML
    private void startGame() {
        hexgame.getTribes().add(new Tribe(tribeTextField.getText(), villageTextField.getText(),true));
//        tribeTextField.getScene().getS
        stage.close();
    }
    public void show() {
        try {
            FXMLLoader fxmlLoader;
            fxmlLoader = new FXMLLoader(getClass().getResource("WelcomeScreen.fxml"));
            fxmlLoader.setController(this);
            Parent root = fxmlLoader.load();
            hexgame.getMainTimer().getScreensToRefresh().add(fxmlLoader.getController());
            stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            System.out.print("oopps");
        }
    }
//    public WelcomeScreen () {
//        this.setLayout( new GridBagLayout());
//        GridBagConstraints c = new GridBagConstraints();
//        c.gridx = 0;//set the x location of the grid for the next component
//        c.gridy = 0;//set the y location of the grid for the next component
//        c.anchor=GridBagConstraints.WEST;//left align components after this point
//        add (new JLabel("How will we call our tribe, chief?"),c);
//        tribeTextField.setSize(1000,20);
//        villageTextField.setSize(1000,20);
//        c.gridx = 1;
//        add (tribeTextField,c);
//        c.gridy = 1;
//        c.gridx = 0;
//        add (new JLabel("...and our first village, chief?"),c);
//        c.gridx = 1;
//        add (villageTextField,c);
//    }
}
