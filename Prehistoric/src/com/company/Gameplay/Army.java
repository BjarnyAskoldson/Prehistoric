package com.company.Gameplay;

import com.company.Enumerations.Equipment;
import com.company.HexMap;
import com.company.Interfaces.IAbleToFight;
import com.company.Interfaces.IMoveable;
import javafx.scene.image.ImageView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Army implements IAbleToFight, IMoveable, Serializable {
    private int people = 0;
    private HashMap<Equipment, Integer> equipment = new HashMap<>();
    private HashMap<Equipment, Integer> equipmentInUse = new HashMap<>();
    private Settlement homeBase;
    private String name = "";
    private Hex location;
    private Hex destination;
    private HashSet<Hex> hexesPatrolled = new HashSet<>();
    private transient ImageView imageView;
    private int captives;
    private boolean isAggressive;
    private boolean actedThisTurn;
    private boolean missionCompleted;
    private int foodReserve;
    private int patrolSize = 10;

    public HashSet<Hex> getHexesPatrolled() {
    	return hexesPatrolled;
    }
    
    public int getPatrolSize() {
    	return patrolSize;
    }
    
    public void setPatrolSize(int patrolSize) {
    	this.patrolSize = patrolSize;
    }
    
    public void removePatrol(Hex hex) {
    	if (hexesPatrolled.contains(hex)) 
    		hexesPatrolled.remove(hex);
    	
    }
    public boolean canAddPatrol() {
		int peopleAlreadyInPatrols = hexesPatrolled.size()*patrolSize;
		if (peopleAlreadyInPatrols+patrolSize>people)
			return false;
		else {
			return true;
		}	
    	
    }
    public void addPatrol(Hex hex) {
		hexesPatrolled.add(hex);
    }
    
    public void setAggressive(boolean aggressive) {
        isAggressive = aggressive;
    }

    public boolean isMissionCompleted() {
        return missionCompleted;
    }

    @Override
    public boolean isAggressive() {
        return isAggressive;
    }

    @Override
    public void disband() {
        location.getArmies().remove(this);
        homeBase.getArmies().remove(this);
    }

    @Override
    public void takeCaptives(int captives) {
        this.captives += captives;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public void setActedThisTurn(boolean actedThisTurn) {
        this.actedThisTurn = actedThisTurn;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public int getFirepower() {
        double result = 0;//homeBase.toolEffectiveness()*people*hexgame.WorkersDeathTool;

        for (Map.Entry<Equipment, Integer> e : equipment.entrySet()) {
            result += e.getValue()*e.getKey().getDeathToll();
        }
        return (int)result;
    }

    public HashMap<Equipment, Integer> getEquipment() {
        return equipment;
    }

    public int getPeople() {
        return people;
    }

    public Hex getDestination() {
        return destination;
    }

    public void setDestination(Hex destination) {
        this.destination = destination;
    }

    public int getPeopleMax() {
        int result = 0;
        for (Map.Entry<Equipment, Integer> equipment : equipment.entrySet())
            result += equipment.getValue()*equipment.getKey().getPeopleToOperate();
        return result;
    }

    public String getName() {
        return name;
    }

    public int getFoodReserve() {
        return foodReserve;
    }

    /**
     * Method to launch attack by selected army on chosen enemy object
     * @param destination
     */
    public void launchAttack(Hex destination) {
        this.destination = destination;
        int foodNeeded = HexMap.getPath(location, destination).size()*people;
        homeBase.spendStateFood(foodNeeded);
        foodReserve = foodNeeded;
    }

    @Override
    public boolean atTarget() {
        if (location.equals(destination)&& !location.equals(homeBase.getLocation()))
            return true;
        else
            return false;
    }

    public void setEquipment(HashMap<Equipment, Integer> equipment) {
        this.equipment = equipment;
    }

    public String getDescriptiveName() {
        return name + " ( people:"+people + ", " + "people limit: " + getPeopleMax() + ")";
    }

    public void addPeople (int peopleToAdd) {
        this.people += peopleToAdd;
    }

    public Settlement getHomeBase() {
        return homeBase;
    }

    public Tribe getOwner() {
        return homeBase.getOwner();
    }
    public void setPeople(int people) {
        this.people = people;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Hex getLocation() {
        return location;
    }

    public void addEquipment(Equipment equipment, Integer amount) {
        this.equipment.put(equipment, amount);
    }

    public HashMap<Equipment, Integer> getEquipmentInUse() {
        return equipmentInUse;
    }

    //To replace default text representation of the army, e.g. in GUI controls
    @Override
    public String toString(){
        return name + " ( people:"+people + ", " + "people limit: " + getPeopleMax() + ")";
    }

    public void processDay(){
        //first, feed the people
        foodReserve -= people;

        //if fought at destination, consider mission accomplished
        if (actedThisTurn&&location.equals(destination)&&missionCompleted==false) {
            missionCompleted = true;
            destination = homeBase.getLocation();

        }

        //if fought this turn, have a rest
        if (actedThisTurn) {
            actedThisTurn = false;
            return;
        }
        //if army is on its destination and fought already, time to go home
        if (location.equals(destination)) {

            destination = homeBase.getLocation();
        }
         else {
            //move towards destination
        location.getArmies().remove(this);
        location = HexMap.getPath(location, destination).get(1);//HexMap.getBoard()[newX][newY];
        location.getArmies().add(this);
        }
    }

    public Army(Settlement homeBase, int people, HashMap<Equipment, Integer> equipment) {
        this.name = homeBase.getName() + " army ";
        this.homeBase = homeBase;
        this.location = homeBase.getLocation();
        this.destination = homeBase.getLocation();
        this.people = people;
        this.equipment = equipment;
    }
}
