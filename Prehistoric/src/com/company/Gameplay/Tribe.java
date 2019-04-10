package com.company.Gameplay;

import com.company.Enumerations.*;
import com.company.HexMap;
import com.company.hexgame;

import java.io.Serializable;
import java.util.*;

public class Tribe implements Serializable {
    private List<Settlement> settlements = new LinkedList<>();
    private boolean isHuman;
    private String name;
    private int generalTax = 5;
    private int rulerReputation;

    private AI AI;
    private HashSet<Equipment> equipmentAvailableToOrder = new HashSet<>();
    private HashSet<Technology> technologiesOpen = new HashSet<>();
    private HashMap<BranchOfKnowledge, Integer> tribeExperience;

    public HashMap<BranchOfKnowledge, Integer> getTribeExperience() {
        return tribeExperience;
    }

    public List<Settlement> getSettlements() {
        return settlements;
    }

    public boolean isHuman() {
        return isHuman;
    }

    public int getRulerReputation() {
        return rulerReputation;
    }

    public void setRulerReputation(int rulerReputation) {
        this.rulerReputation = rulerReputation;
    }

    public void recalculateRulerReputation() {
        int totalReputation = settlements.stream().map(a->a.getLocalRulerReputation()).reduce((a,b)->a+b).get();
        rulerReputation = totalReputation/settlements.size();
    }

    public String getName() {
        return name;
    }

    /**
     * Returns types of assets available to the tribe
     * @return set of assets
     */
    public HashSet<Asset> getAssetsAvailable() {
    	HashSet<Asset> result = new HashSet<>();
    	result.add(Asset.StoneTools);
    	result.add(Asset.FlintTools);
        for (Technology technology: technologiesOpen)
        	if (technology.getAssetToAdd()!=null)
        		result.add(technology.getAssetToAdd());
    	return result;
    }
    
    /**
     * Returns types of houses available to the tribe
     * @return
     */
    public HashSet<WealthLevel> getHousesAvailable() {
    	HashSet<WealthLevel> result = new HashSet<>();
    	result.add(WealthLevel.Homeless);
    	result.add(WealthLevel.Hut);
        for (Technology technology: technologiesOpen)
        	if (technology.getWealthLevelToAdd()!=null)
        		result.add(technology.getWealthLevelToAdd());
    	return result;
    }
    //returns list of professions available in a given tribe
    public HashSet<Profession> getProfessionsAvailable() {
        HashSet<Profession> result = new HashSet<>();
        //First, return default professions, available from the beginning
        //result.add(Profession.Hunter);
        result.add(Profession.Farmer);
        result.add(Profession.Miner);
        result.add(Profession.Builder);
//        result.add(Profession.Toolmaker);
//        result.add(Profession.Armourer);
        result.add(Profession.Crafter);
        for (Technology technology: technologiesOpen)
            for (Profession profession: technology.getProfessionsToAdd())
                result.add(profession);
        return result;
    }
    public int getGeneralTaxRate() {
        return generalTax;
    }

    public void setGeneralTax(int generalTax) {
        this.generalTax = generalTax;
    }

    public HashSet<Equipment> getEquipmentAvailableToOrder() {
        return equipmentAvailableToOrder;
    }

    //Returns consolidated treasury totals for all settlements
    public int getTreasury() {
        int result = 0;
        for (Settlement settlement :settlements) {
            for (Map.Entry<Commodity, Integer> commodity : settlement.getStateCommodityStorage().entrySet())
                result += commodity.getValue();
        }

        return result;
    }
    public int getTaxLastTurn () {
        int result = 0;
        for (Settlement settlement :settlements) {
            for (Map.Entry<Commodity, Integer> commodity : settlement.getNaturalTaxLastTurn().entrySet())
                result += commodity.getValue();
        }

        return result;
    }


    /**
     * Method to process tax collection, - it calculates the amount of tax and adds it to the treasury
     */
    public void collectTaxes () {
        LinkedList<Settlement> settlementsToTax = new LinkedList<>(settlements);
        int result = 0;

        for (Settlement settlement : settlementsToTax) {
            //calculate the coefficient of tax fraud
            //Number of tax frauds = 100 - local ruler reputation; amount of loss = 100 - local ruler reputation * tax amount
            //thus, loss coefficient = (100 - local ruler reputation)^2
            double taxFraudCoefficient = Math.pow((double)((100 - (settlement.getLocalRulerReputation()>100 ? 100 :settlement.getLocalRulerReputation()))/100),2);

            settlement.setNaturalTaxLastTurn(new HashMap<>());
            for (Commodity commodity : Commodity.values()) {
                int generalTaxOnCommodity = (int)((1-taxFraudCoefficient) * settlement.commodityPerTurn(commodity, false) * commodity.getLaboriousness() * generalTax / 100);
                //negative and 0 results (unemployed groups, army etc.) are ignored
                if (generalTaxOnCommodity>0) {
                    Commodity commodityToAdd;
                    if (commodity.getNeed() == Need.Food)
                        commodityToAdd = commodity;
                    else {
                        //find most popular food commodity and use it to tax non-food professions
//                        int maxFoodYield = 0;
                        commodityToAdd = Commodity.Food;
//                        for (Commodity commodityForNonFoodProfession : Commodity.values())
//                            if (commodityForNonFoodProfession.getNeed() == Need.Food && maxFoodYield < settlement.commodityPerTurn(commodityForNonFoodProfession)) {
//                                maxFoodYield = settlement.commodityPerTurn(commodityForNonFoodProfession);
//                                commodityToAdd = commodityForNonFoodProfession;
//                            }
                    }

                    settlement.addStateCommodity(commodityToAdd, generalTaxOnCommodity);
                    settlement.getNaturalTaxLastTurn().put(commodityToAdd, settlement.getNaturalTaxLastTurn().getOrDefault(commodityToAdd,0)+generalTaxOnCommodity);
                    result += generalTaxOnCommodity;
                }
            }
        }
//        TaxScreen.updateTaxInfo();
    }
    /**
     * method to process Tribe's day. For human it just processes settlements' statuses;
     * for AI it assigns new expeditions
     */
    public void processDay() {
    	
    	if(!isHuman)
    		AI.processDay();

        for(Technology technology: Technology.values())
            if (technology.experienceSufficient(this)) {
                technologiesOpen.add(technology);
                for (Equipment equipment : technology.getEquipmentToOpen())
                    equipmentAvailableToOrder.add(equipment);
            }
        for (Settlement settlement : settlements) {
            settlement.processDay();
        }
        collectTaxes();
        recalculateRulerReputation();
    }

    public Tribe(String name, String villageName, boolean isHuman) {
        this.name = name;
        this.isHuman = isHuman;

        //Add first village into random location; if it is already occupied, lets try another place until we'll find a vacant one
        int x = (int)(Math.random()*hexgame.BSIZE);
        int y = (int)(Math.random()*hexgame.BSIZE);
        while (HexMap.getBoard()[x][y].getSettlement()!=null) {
            x = (int)(Math.random()*hexgame.BSIZE);
            y = (int)(Math.random()*hexgame.BSIZE);
        }
        Settlement firstVillage = new Settlement(villageName,HexMap.getBoard()[x][y], this);
        this.settlements.add(firstVillage);
        this.tribeExperience = new HashMap<>();
        HexMap.getBoard()[x][y].setSettlement(firstVillage);

        rulerReputation = 100;
        
        if(!isHuman)
        	this.AI = new AI(this);

        equipmentAvailableToOrder.add(Equipment.FlintAxes);//Arrays.stream(Equipment.values()).collect(Collectors.toCollection(HashSet<Equipment>::new));
        equipmentAvailableToOrder.add(Equipment.StoneAxes);//Arrays.stream(Equipment.values()).collect(Collectors.toCollection(HashSet<Equipment>::new));


    }
}
