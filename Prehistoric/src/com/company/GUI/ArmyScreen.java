package com.company.GUI;

import com.company.Gameplay.Army;
import com.company.Enumerations.Equipment;
import com.company.Interfaces.Initialisable;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.*;

import java.util.Map;

public class ArmyScreen implements Initialisable/*extends JPanel implements ActionListener */ {
    @FXML
    private Slider equipmentSlider;
    @FXML
    private ListView<Equipment> armory = new ListView<>();
    @FXML
    private ListView<Equipment> armyEquipment = new ListView<>();
    @FXML
    private CheckBox aggressiveChooser;
    @FXML
    private TextField nameTextField;
    @FXML
    private Label peopleLabel = new Label("People:     0");
    @FXML
    private Label peopleMaxLabel = new Label("People needed:     0");

    private Army army;
    private Object caller;


    @Override
    public Object getEntity() {
        return army;
    }

    @Override
    public void initData(Object army, Object caller) {
        this.army = (Army) army;
        this.caller = caller;
        updateControls();
//        armyEquipment.getItems().addAll(this.army.getEquipment().keySet());
//        armory.getItems().addAll(this.army.getHomeBase().getEquipment().keySet());
    }

    private void updateControls() {
        armyEquipment.getItems().clear();
        armyEquipment.getItems().addAll(this.army.getEquipment().keySet());
        armory.getItems().clear();
        armory.getItems().addAll(this.army.getHomeBase().getEquipment().keySet());
    }
    @FXML
    private void initialize() {
 //       initData(army);
    }

    @FXML
    private void moveEquipmentToArmy() {
        Equipment equipmentSelected = armory.getSelectionModel().getSelectedItem();
        if (equipmentSelected!=null) {
            army.getEquipment().put(equipmentSelected, army.getHomeBase().getEquipment().getOrDefault(equipmentSelected, 0));
            army.getHomeBase().getEquipment().remove(equipmentSelected);
            updateControls();
        }
    }

    public void shutdown() {
        ((SettlementScreen)caller).updateControls();
    }
    @FXML
    private void moveAllToArmy() {
        for (Map.Entry<Equipment, Integer> equipment : army.getHomeBase().getEquipment().entrySet()) {
            army.getEquipment().put(equipment.getKey(), army.getHomeBase().getEquipment().getOrDefault(equipment.getKey(), 0));
            army.getHomeBase().getEquipment().remove(equipment.getKey());
        }
        updateControls();
    }

    @FXML
    private void moveEquipmentToArmory() {
        Equipment equipmentSelected = armyEquipment.getSelectionModel().getSelectedItem();
        if (equipmentSelected!=null) {
            army.getHomeBase().getEquipment().put(equipmentSelected, army.getEquipment().getOrDefault(equipmentSelected, 0));
            army.getEquipment().remove(equipmentSelected);
            armyEquipment.getItems().remove(equipmentSelected);
            updateControls();
        }
    }

    @FXML
    private void moveAllToArmory() {
        for (Map.Entry<Equipment, Integer> equipment : army.getEquipment().entrySet()) {
            army.getHomeBase().getEquipment().put(equipment.getKey(), army.getEquipment().getOrDefault(equipment.getKey(), 0));
            army.getEquipment().remove(equipment.getKey());
        }
        updateControls();
    }

//    public ArmyScreen(Army army) {
//        try {
//            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ArmyScreen.fxml"));
//            Parent root = fxmlLoader.load();
//            ((Initialisable) (fxmlLoader.getController())).initData(army);
////            hexgame.getMainTimer().getScreensToRefresh().add(fxmlLoader.getController());
//            Stage stage = new Stage();
//            stage.setScene(new Scene(root));
//            stage.show();
//        } catch (IOException e) {
//            e.getLocalizedMessage();
//        }
//        ;
//
//    }
}
    /*
    public HashMap<Equipment, Integer> equipmentUpdated() {
        return armyUpdated.getEquipment();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().startsWith("equipment_")) {
            selectedWeapon = Equipment.getEquipmentByName(e.getActionCommand().substring(10));
            weaponChooser.select(e.getActionCommand().substring(10));
        }
    }
    private void updateGUI() {

 //       this.removeAll();

    }
    public int getPeopleMax() {
        return equipmentMax;
    }

    public int getPeopleToSend () {
        return peopleToTake;
    }
    public Resource getResourceSelected () {
        return resourceSelected;
    }
    public boolean getAggressiveness () {
        return aggressive;
    }
    
    public ArmyScreen(Army army) {
        this.armyUpdated = army;
        nameTextField.setText(army.getName());
//        Choice weaponChooser = new Choice();
        for (Map.Entry<Equipment, Integer> mainWeapon: army.getHomeBase().getEquipment().entrySet()) {

//            boolean equipmentAlreadyInUse =  army.getHomeBase().getEquipment().entrySet().stream()
//                    .filter(a->a.getKey().equals(mainWeapon))
//                    .findAny()
//                    .isPresent();
//            if (!equipmentAlreadyInUse)
                weaponChooser.add(mainWeapon.getKey().getName());
        }

        this.setLayout( new GridBagLayout());
//
//        JSlider peopleSlider = new JSlider();
//        peopleSlider.setMinimum(0);
//        peopleSlider.setValue(0);
//        peopleSlider.setPaintLabels(true);
//        peopleSlider.setPaintTicks(true);
//        peopleSlider.setMajorTickSpacing(50);
//        peopleSlider.setMinorTickSpacing(10);
//        peopleSlider.addChangeListener(this);

        JSlider equipmentSlider = new JSlider();
        equipmentSlider.setMinimum(0);
        equipmentSlider.setValue(0);
        equipmentSlider.setPaintLabels(true);
        equipmentSlider.setPaintTicks(true);
        equipmentSlider.setMajorTickSpacing(50);
        equipmentSlider.setMinorTickSpacing(10);
//        equipmentSlider.addChangeListener(this);
        aggressiveChooser.add("No, let's be nice with them for awhile..");
        aggressiveChooser.add("Of course! Why do you ask?");
        aggressiveChooser.select(0);

        //Populate amounts of weapon available, - it is sum of home base armory and army's equipment
        HashMap<Equipment, Integer> weaponsAvailable = new HashMap<>(army.getHomeBase().getEquipment());
        for (Map.Entry<Equipment, Integer> equipment : weaponsAvailable.entrySet())
            equipment.setValue(equipment.getValue()+army.getEquipment().getOrDefault(equipment.getKey(),0));

        
        selectedWeapon = Equipment.getEquipmentByName(weaponChooser.getSelectedItem());
        selectedWeaponAvailable = weaponsAvailable.getOrDefault(selectedWeapon,0);

        weaponChooser.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                selectedWeapon = Equipment.getEquipmentByName(weaponChooser.getSelectedItem());
                selectedWeaponAvailable = weaponsAvailable.getOrDefault(selectedWeapon,0);
//                updateGUI();
            }
        });

        aggressiveChooser.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                aggressive = aggressiveChooser.getSelectedIndex() > 0;
            }
        });
        updateGUI();


        equipmentSlider.setMaximum(selectedWeaponAvailable);
        equipmentSlider.setValue(selectedWeaponAvailable);
        peopleSlider.setValue(selectedWeaponAvailable);
        equipmentMax = selectedWeaponAvailable;
        armyUpdated.getEquipment().put(selectedWeapon, equipmentMax);
        String peopleToSendString = Integer.toString(peopleSlider.getValue());
        peopleLabel.setText("People to send:"+ new String(new char[6-peopleToSendString.length()]).replace("\0", " ") + peopleToSendString);
        String equipmentString = Integer.toString(equipmentSlider.getValue());
        peopleMaxLabel.setText("Amount:"+ new String(new char[6-equipmentString.length()]).replace("\0", " ") + equipmentString);
        nameTextField.setText(army.getName());
        int peopleAvailable = army.getHomeBase().getIdlePeople()/(selectedWeapon==null ? 1 : selectedWeapon.getPeopleToOperate())+ (selectedWeapon==null ? 0 : army.getEquipmentInUse().getOrDefault(selectedWeapon,0));
        if(selectedWeaponAvailable<=peopleAvailable)
            peopleSlider.setMaximum(selectedWeaponAvailable);
        else
            peopleSlider.setMaximum(peopleAvailable);

//
//        peopleSlider.addChangeListener(new ChangeListener() {
//            @Override
//            public void stateChanged(ChangeEvent changeEvent) {
//                peopleToTake = peopleSlider.getValue();
//                String peopleToSendString = Integer.toString(peopleSlider.getValue());
//                peopleLabel.setText("People to send:"+ new String(new char[6-peopleToSendString.length()]).replace("\0", " ") + peopleToSendString);
//                army.setPeople(peopleToTake);
//                army.getEquipmentInUse().put(selectedWeapon, peopleToTake/selectedWeapon.getPeopleToOperate());
////                updateGUI();
//            }
//        });

        equipmentSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                int old_value = equipmentMax;
                int weaponInArmory = army.getHomeBase().getEquipment().getOrDefault(selectedWeapon,0);
                equipmentMax = equipmentSlider.getValue();
                String peopleToSendString = Integer.toString(equipmentSlider.getValue());
                peopleMaxLabel.setText("Amount:"+ new String(new char[6-peopleToSendString.length()]).replace("\0", " ") + peopleToSendString);
                armyUpdated.getEquipment().put(selectedWeapon, equipmentMax);
                updateGUI();
            }
        });

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;//set the x location of the grid for the next component
        c.gridy = 0;//set the y location of the grid for the next component
        c.anchor=GridBagConstraints.WEST;//left align components after this point
        this.add (nameTextField, c);
//        c.gridy++;
//
//        this.add(peopleLabel,c);
//        c.gridx = 1;//set the x location of the grid for the next component
//        this.add(peopleSlider,c);
        c.gridx = 0;
        c.gridy++;
        this.add(peopleMaxLabel,c);
        c.gridx = 1;
        this.add(equipmentSlider,c);

//        c.gridx = 2;
//        this.add(peopleLabel);
        c.gridy++;//change the y location
        c.gridx = 0;
        this.add(new Label("Select weapon: "),c);
        c.gridx = 1;
        this.add(weaponChooser,c);
        resourceSelected = Resource.getResourceByName(weaponChooser.getSelectedItem());
        c.gridx = 0;
        c.gridy++;//change the y location
        this.add(new Label("Are we to attack strangers if any?"),c);
        c.gridx = 1;
        this.add(aggressiveChooser,c);
        for (Map.Entry<Equipment, Integer> equipmentInArmy : army.getEquipment().entrySet()) {
            c.gridy++;
            int equipmentInUse = army.getEquipmentInUse().getOrDefault(equipmentInArmy.getKey(),0);
            JLabel equipmentLabel = new JLabel(equipmentInArmy.getKey().getName() + ": " + equipmentInArmy.getValue() + ", " + equipmentInUse);
            equipmentLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent mouseEvent){
                    selectedWeapon = equipmentInArmy.getKey();
                    weaponChooser.select(equipmentInArmy.getKey().getName());
                    updateGUI();
                } });
            this.add(equipmentLabel);
        }
    }
}
    */