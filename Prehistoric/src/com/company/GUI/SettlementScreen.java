package com.company.GUI;

import com.company.Gameplay.Army;
import com.company.Enumerations.Commodity;
import com.company.Enumerations.Equipment;
import com.company.Enumerations.WealthLevel;
import com.company.Interfaces.IProducible;
import com.company.Interfaces.Initialisable;
import com.company.Gameplay.Settlement;
import com.company.Gameplay.WorkingGroup;
import com.company.hexgame;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

/**
 *  Panel that shows the status of the selected settlement
 */
public class SettlementScreen implements Initialisable {

    private Settlement settlement;

    @FXML
    private Label foodReservesLabel;

    @FXML
    private Label foodProducedLabel;
    
    @FXML
    private Label wealthLevels;

    @FXML
    private Label peopleLabel;

    @FXML
    private Label starvationLabel;
    @FXML
    private Button createArmyButton;

    @FXML
    private ListView<String> assetsListView;

    @FXML
    private ListView<Army> armyListView;

    @FXML
    private TextField nameTextField;

    @FXML
    private ListView<WorkingGroup> workingGroupListView;

    @FXML
    private ListView<String> equipmentListView;

    @FXML
    private Button orderEquipmentButton;

    @FXML
    private ComboBox equipmentToOrderCombobox;

    @FXML
    private Slider amountToOrderSlider;

    @FXML
    private Label amountToOrderLabel;

    @FXML
    private Label idlePeopleLabel;

    @FXML
    private void createArmy() {
        Army armySelected;
        if (armyListView.getSelectionModel().getSelectedItem()!=null)
            armySelected = armyListView.getSelectionModel().getSelectedItem();
        else
            armySelected = new Army(settlement,0, new HashMap<>());

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ArmyScreen.fxml"));
            Parent root = fxmlLoader.load();
            ((Initialisable)(fxmlLoader.getController())).initData(armySelected, this);
//            hexgame.getMainTimer().getScreensToRefresh().add(fxmlLoader.getController());
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setOnHidden(e->((ArmyScreen)fxmlLoader.getController()).shutdown());
            stage.show();
        } catch (IOException e) {
            e.getLocalizedMessage();
        };
        //ArmyScreen armyScreen = new ArmyScreen(armySelected);
//        settlement.createArmy();
        if (!settlement.getArmies().contains(armySelected))
            settlement.getArmies().add(armySelected);

        hexgame.mainScreen.updateControls();
        this.updateControls();
    }

    @FXML
    private void orderEquipment() {
        if (equipmentToOrderCombobox.getValue() != null && (int) amountToOrderSlider.getValue() != 0) {
            settlement.orderEquipment((Equipment) equipmentToOrderCombobox.getValue(), (int) amountToOrderSlider.getValue());
            this.updateControls();
        }
    }

    @Override
    public Object getEntity() {
        return settlement;
    }

    @FXML
    private void renameVillage() {
        settlement.setName(nameTextField.getText());
    }

    /**
     * This method is called to update the status of settlement after economical tick
     */
    public void updateControls() {
//        for (Army army : settlement.getArmies())
//        armyListView = new ListView<>();
        armyListView.getItems().setAll(settlement.getArmies());

        nameTextField.setText(settlement.getName());

        //Show the list of the working groups
        workingGroupListView.getItems().clear();
        HashSet<WorkingGroup> workingGroups = settlement.getWorkingGroups();
        for (WorkingGroup workingGroup : workingGroups) {
            if (workingGroup.getPeople() > 0 && !workingGroupListView.getItems().contains(workingGroup))
                workingGroupListView.getItems().add(workingGroup);
        }

        //Show buildings currently in the village
        assetsListView.getItems().clear();
        HashMap<Commodity, Integer> assets = settlement.getAssets();
        String assetString = new String();
        for (Map.Entry<Commodity, Integer> asset : assets.entrySet()) {
            assetString = "";
            if (asset.getValue() > 0)
                assetString = asset.getKey().getName() + ": " + asset.getValue();
            if (settlement.commodityPerTurn(asset.getKey(), false) > 0)
                if (assetString == "")
                    assetString = asset.getKey() + ", under construction: " + settlement.commodityPerTurn(asset.getKey(), false);
                else
                    assetString += ", under construction: " + settlement.commodityPerTurn(asset.getKey(), false);

            if (assetString!="") assetsListView.getItems().add(assetString);
        }

        //show the settlement's armoury content:
        equipmentListView.getItems().clear();
        HashSet<Equipment> equipmentInPresence = new HashSet<>();
        for (Map.Entry<Equipment, Integer> equipment : settlement.getEquipment().entrySet()) {
            String equipmentString = new String(equipment.getKey().getName());
            equipmentInPresence.add(equipment.getKey());
            if (equipment.getValue() > 0)
                equipmentString += " (" + equipment.getValue() + ")";
            if (settlement.getEquipmentOrders().getOrDefault(equipment.getKey(), 0) > 0)
                equipmentString += ", ordered: " + settlement.getEquipmentOrders().getOrDefault(equipment.getKey(), 0);

            equipmentListView.getItems().add(equipmentString);
        }
        for (Map.Entry<Equipment, Integer> equipment : settlement.getEquipmentOrders().entrySet()) {
            if (equipment.getValue() > 0 && !equipmentInPresence.contains(equipment.getKey()))
                equipmentListView.getItems().add(equipment.getKey().getName() + " ordered:" + equipment.getValue());

        }
        //workingGroupListView.getItems().addAll(settlement.getWorkingGroups().stream().filter(a->a.getPeople()>0).collect(Collectors.toCollection(HashSet<>)));

        //Prepare list of equipment available to order
        Equipment selectedEquipment = (Equipment)equipmentToOrderCombobox.getValue();
        equipmentToOrderCombobox.getItems().clear();
        equipmentToOrderCombobox.getItems().addAll(settlement.getOwner().getEquipmentAvailableToOrder());
        equipmentToOrderCombobox.getSelectionModel().select(selectedEquipment);

        wealthLevels.setText(populateWealthLabel());
        //populate idle people label
        idlePeopleLabel.setText("Total people: " + settlement.getTotalPeople()+", unemployed: " + settlement.getIdlePeople());
    }

    @FXML
    private String populateWealthLabel() {
    	String result = "";
    	HashMap<WealthLevel, Integer> settlementWealths = settlement.getWealthLevels();
    	for (Map.Entry<WealthLevel, Integer> wl : settlementWealths.entrySet())
    		result += wl.getKey().getName() + ": " + wl.getValue().toString() + "\n";
    		return result;
    }
    @Override
    public void initData(Object object, Object caller) {
//        armyListView = new ListView<>();
        this.settlement = (Settlement) object;
        //assetsTableView.getColumns().get(0).
        foodReservesLabel.textProperty().bind(new SimpleIntegerProperty(settlement.getFoodReserves()).asString());
        foodProducedLabel.textProperty().bind(new SimpleIntegerProperty(settlement.getFoodProduced()).asString());
        peopleLabel.textProperty().bind(new SimpleIntegerProperty(settlement.getTotalPeople()).asString());
        starvationLabel.textProperty().bind(new SimpleIntegerProperty(settlement.getStarvationLastTurn()).asString());
        updateControls();
    }

    @FXML
    private void initialize() {
        amountToOrderSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
//            settlement.orderEquipment((Equipment) equipmentToOrderCombobox.getValue(), (int) amountToOrderSlider.getValue());
            amountToOrderLabel.setText(Long.toString(Math.round(amountToOrderSlider.getValue())));
        });

        equipmentToOrderCombobox.valueProperty().addListener((observable, oldValue, newValue) -> {
        	if (newValue != null)
	            // set range for order amount on equipment selected
	            amountToOrderSlider.setMax((double) (settlement.getSettlementTreasury()/((IProducible)newValue).getLaboriousness()));
        	else
        		amountToOrderSlider.setMax(0);
        });

        nameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            settlement.setName(newValue);
        });
    }
}
