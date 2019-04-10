package com.company.Enumerations;

import com.company.Gameplay.Tribe;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public enum Technology {
    Domestication("Domestication"),
    Archery ("Archery"),
    Pottery("Pottery"),
    Trade("Trade"),
    Carpentry("Carpentry");
    static {
        Domestication.experienceRequired.put(BranchOfKnowledge.Nature, 1000);
        Pottery.experienceRequired.put(BranchOfKnowledge.Craft, 1000);
        Archery.experienceRequired.put(BranchOfKnowledge.Nature, 10000);
        Trade.experienceRequired.put(BranchOfKnowledge.Craft, 5000);
        Carpentry.experienceRequired.put(BranchOfKnowledge.Craft, 10000);
        Archery.equipmentToOpen.add(Equipment.Bow);
        Trade.professionsToAdd.add(Profession.Trader);
        Carpentry.wealthLevelToAdd = WealthLevel.House;
//        Pottery.professionsToAdd.add(Profession.Potter);

    }
    private String name;
    private HashMap<BranchOfKnowledge, Integer> experienceRequired;
    private HashSet<Equipment> equipmentToOpen;
    private HashSet<Profession> professionsToAdd;
    private WealthLevel wealthLevelToAdd;
    private Asset AssetToAdd;

    public HashSet<Equipment> getEquipmentToOpen() {
        return equipmentToOpen;
    }

    public HashSet<Profession> getProfessionsToAdd() {
        return professionsToAdd;
    }

    //Method to check whether conditions for the technolodgy were met already
    public boolean experienceSufficient(Tribe tribe) {
        boolean result = true;
        for (Map.Entry<BranchOfKnowledge, Integer> experienceRequired : experienceRequired.entrySet())
            if (experienceRequired.getValue()>tribe.getTribeExperience().getOrDefault(experienceRequired.getKey(),0)) {
                result = false;
                break;
            }
        return result;
    }
    public String getName() {
		return name;
	}

    public WealthLevel getWealthLevelToAdd() {
		return wealthLevelToAdd;
	}

	Technology(String fullName) {
        this.name = fullName;
        this.experienceRequired = new HashMap<>();
        this.equipmentToOpen = new HashSet<>();
        this.professionsToAdd = new HashSet<>();
    }

	public Asset getAssetToAdd() {
		return AssetToAdd;
	}
}
