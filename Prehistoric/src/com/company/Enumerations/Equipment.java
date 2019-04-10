package com.company.Enumerations;

import com.company.Interfaces.IProducible;

import java.util.HashMap;

public enum Equipment implements IProducible {
    StoneAxes ("Stone axes", SectorType.LightIndustry,  0.05, 1, 1,1, 0.1),
    FlintAxes ("Flint axes", SectorType.LightIndustry,  0.1, 1, 1, 1, 0.1),
    Bow("Bow", SectorType.LightIndustry, 0.05, 1,1,3, 0.1)
    ;
    static {
        FlintAxes.materials.put(Resource.flintStone,1);
    }
    private String name;
    private double deathToll;
//    private double resourceYield;
    private double laboriousness;
    private HashMap<IProducible, Integer> materials = new HashMap<>();
    private int peopleToOperate;
    private int range;
    private SectorType sector;
    private double weight;

    @Override
    public boolean isConsumable() {
        return false;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    //    @Override
//    public void setProfession(Profession profession) {
//
//    }

    public static Equipment getEquipmentByName (String name) {
        for (Equipment equipment : values())
            if (equipment.name.equals(name))
                return equipment;

        return null;
    }

    public String getName() {
        return name;
    }

    public int getPeopleToOperate() {
        return peopleToOperate;
    }

    public double getLaboriousness() {
        return laboriousness;
    }

    public HashMap<IProducible, Integer> getMaterials() {
        return materials;
    }

    public double getDeathToll() {
        return deathToll;
    }

    @Override
    public String toString() {
        return name;
    }

//    public double getResourceYield() {
//        return resourceYield;
//    }

    Equipment(String name, SectorType sector, double deathToll, /*double resourceYield,*/ int laboriousness, int peopleToOperate, int range, double weight) {
        this.name = name;
        this.deathToll = deathToll;
        this.sector = sector;
//        this.resourceYield = resourceYield;
        this.laboriousness = laboriousness;
        this.peopleToOperate = peopleToOperate;
        this.range = range;
        this.weight = weight;
    }

	@Override
	public SectorType getSector() {
		// TODO Auto-generated method stub
		return sector;
	}
}
