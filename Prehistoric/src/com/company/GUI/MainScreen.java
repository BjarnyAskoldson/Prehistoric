package com.company.GUI;

import com.company.*;
import com.company.Gameplay.*;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.*;

public class MainScreen {
    @FXML
    private Slider timeSpeedSlider;
    @FXML
    private Button pauseButton;
    @FXML
    private ChoiceBox armySelected = new ChoiceBox();
    @FXML
    private Slider generalTaxSlider;
    @FXML
    private Label generalTaxLabel = new Label();
    @FXML
    private Label treasuryLabel = new Label();
    @FXML
    public Pane highlightedTile;// = new Pane();
    @FXML
    private TreeView<TreeItem> armyTreeView = new TreeView<>();
    @FXML
    //private Label activeSettlementLabel;
    private Label activeObjectLabel;

    private boolean newSettlementMode;
  
//    public void setMapPane() {
//        this.
//    }


    public void setNewSettlementMode(boolean newSettlementMode) {
        this.newSettlementMode = newSettlementMode;
    }
    
    @FXML
    private void initialize() {

        newSettlementMode = false;
        // Handle Slider value change events.
        timeSpeedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            hexgame.getMainTimer().setTimeSpeed((int)timeSpeedSlider.getValue());
        });
        armySelected.getItems().removeAll();
        for (Settlement settlement: hexgame.getPlayer().getSettlements())
            for (Army army: settlement.getArmies())
                armySelected.getItems().add(army.getDescriptiveName());

        generalTaxSlider.valueProperty().addListener((observable, oldValue, newValue) ->  {
                hexgame.getPlayer().setGeneralTax((int)generalTaxSlider.getValue());
                generalTaxLabel.setText("General tax :" + generalTaxSlider.getValue());
                treasuryLabel.setText("Treasury:" + + hexgame.getPlayer().getTreasury() + "("+hexgame.getPlayer().getTaxLastTurn()+")");
        });

        generalTaxSlider.setValue(5);
        generalTaxSlider.setMin(0);
        generalTaxSlider.setMax(50);
    }

    @FXML
    public void saveGame() {
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("saves", "*.sav"));
//        fileChooser.setInitialDirectory(new File("C:\\Temp\\"));
        File file = new File("C:\\Temp\\prehistoric.sav");//fileChooser.showSaveDialog(hexgame.map.primaryStage);
        if (file != null) {
            try {
                //FileWriter fileWriter = new FileWriter(file);
//                fileWriter.write("Test value");
//                fileWriter.flush();
                FileOutputStream fout= new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fout);
                //1) saving map
                oos.writeObject(HexMap.getBoard());
                //2) Saving tribes
                for (Tribe tribe : hexgame.getTribes())
                    oos.writeObject(tribe);
                oos.flush();
                oos.close();

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                try {
                	hexgame.logFile.write(ex.getMessage());
                } catch (IOException ex2) {}
            }

        }
    }

    public boolean isNewSettlementMode() {
        return newSettlementMode;
    }

    @FXML
    public void loadGame() {
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("saves", "*.sav"));
//        fileChooser.setInitialDirectory(new File("C:\\Temp\\"));
        File file = new File("C:\\Temp\\prehistoric.sav");//fileChooser.showOpenDialog(hexgame.map.primaryStage);
        if (file!=null) {
            try {
                FileInputStream fout= new FileInputStream(file);
                ObjectInputStream oos = new ObjectInputStream(fout);
                Tribe currentTribe;
                hexgame.getTribes().clear();
                //1) Load map
                Hex[][] board = (Hex[][])oos.readObject();
                //2) create hex screens for hexes loaded
                for (int i = 0; i < board.length; i++)
                    for (int j = 0; j<board.length; j++)
                        board[i][j].setHexScreen(new HexScreen(board[i][j]));
                HexMap.setBoard(board);
                HexMap.drawMapPanel();
                do {
                    currentTribe = (Tribe) oos.readObject();
                    hexgame.getTribes().add(currentTribe);
                } while (currentTribe!=null);
                System.out.println("OK, all read");
                oos.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            //make first settlement of the human player an active
            //hexgame.activeSettlement = hexgame.getTribes().stream().filter(a->a.isHuman()).map(a->a.getSettlements().get(0)).findFirst().get();
            hexgame.activeObject = hexgame.getTribes().stream().filter(a->a.isHuman()).map(a->a.getSettlements().get(0)).findFirst().get();
            HexMap.updateMapPanel();

        }
    }
    @FXML
    private void pauseButtonAction() {
        if (hexgame.getMainTimer().getPause()) {
            hexgame.getMainTimer().setPause(false);
            //To do: find a proper place for this check (to end game if player run out of people)
            if (hexgame.getPlayer().getSettlements().get(0).getTotalPeople()<0) {
                EndGameScreen endGameScreen = new EndGameScreen();;
                endGameScreen.show();
            }

            pauseButton.setText("||");
        }
        else {
            hexgame.getMainTimer().setPause(true);
            pauseButton.setText(">");

        }

    }

    @FXML
    void activateNewSettlement(){
        newSettlementMode = true;
    }

    public void updateControls(){
        treasuryLabel.setText("Treasury:" + + hexgame.getPlayer().getTreasury() + "("+hexgame.getPlayer().getTaxLastTurn()+")");
        armySelected.getItems().clear();
        if (hexgame.activeObject!=null)
            activeObjectLabel.setText(hexgame.activeObject.toString());//.getName());//textProperty().bind(new SimpleStringProperty(hexgame.activeSettlement.getName()));
        else
            activeObjectLabel.setText("No settlement selected");
        for (Settlement settlement: hexgame.getPlayer().getSettlements())
            for (Army army: settlement.getArmies())
                armySelected.getItems().add(army);
    }

    public Army getActiveArmy() {
        return (Army)armySelected.getValue();
    }
    
    public void setActiveArmy(Army army) {
    	armySelected.setValue(army);
    	hexgame.activeObject = army;
    }

    public void addArmy(Army army) {
        armySelected.getItems().add(army);
//        armyTreeView.setRoot(hexgame.getPlayer().getName());
//        armySelected.
    }


//    public void updateControls
    public void updateMapPanel(Group mapPanel) {
        //((SplitPane) ((Pane) timeSpeedSlider.getScene().getRoot()).getChildren().get(0)).getItems().set(1, mapPanel);
        //((ScrollPane)((BorderPane)((AnchorPane) hexgame.map.primaryStage.getScene().getRoot()).getChildren().get(0)).getCenter()).setContent(mapPanel);
    	((ScrollPane)((StackPane)((BorderPane)((AnchorPane) hexgame.map.primaryStage.getScene().getRoot()).getChildren().get(0)).getCenter()).getChildren().get(0)).setContent(mapPanel);
    }
}
