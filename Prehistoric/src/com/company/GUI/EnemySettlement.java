package com.company.GUI;

import com.company.Gameplay.Army;
import com.company.Interfaces.Initialisable;
import com.company.Gameplay.Settlement;
import com.company.hexgame;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class EnemySettlement implements Initialisable {
    @FXML
    private Button attackButton;
    private Settlement settlement;

    @FXML
    private Label settlementLabel;
    @FXML
    private void attack() {
    	
        Army armySelected = null;
        if (hexgame.activeObject.getClass().getName().equals("Army"))
        	armySelected= (Army)hexgame.activeObject;//mainScreen.getActiveArmy();
        if (armySelected!=null)
            armySelected.launchAttack(settlement.getLocation());
    }

    public void setSettlement(Settlement settlement) {
        this.settlement = settlement;
    }

    @Override
    public Object getEntity() {
        return settlement;
    }

    public void initData(Object settlement, Object caller){
        this.settlement = (Settlement) settlement;
        if (hexgame.activeObject.getClass().getName().equals("Army"))
            attackButton.setDisable(true);
        else
            attackButton.setDisable(false);
        if (settlement!=null)
            settlementLabel.setText(this.settlement.getName() + "(" + this.settlement.getTotalPeople() + " people)");
    }

    @FXML
    private void initialize(){
        if (settlement!=null)
            settlementLabel.setText(settlement.getName() + "(" + settlement.getTotalPeople() + " people)");
    }
}
