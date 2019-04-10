package com.company.Enumerations;

import java.util.HashMap;

//import com.company.Enumerations.Equipment;
import com.company.Enumerations.Need;
import com.company.Enumerations.Profession;
import com.company.Enumerations.Resource;
import com.company.Interfaces.IProducible;

//import java.util.HashSet;

public enum Commodity implements IProducible {
    Food ("Meat", SectorType.Agriculture, true, 1, 1),
    Goods("Consumer Goods", SectorType.LightIndustry, true, 1,1),
    Timber("Timber",SectorType.Extracting, false, 1,1),
    Flintstone("Flintstone", SectorType.Extracting, false,1,1),
    Bronze("Bronze", SectorType.HeavyIndustry, false,1,1),
    
    //Wood("Wood", Nee)
//    Dugout ("Dugout", Need.Dwelling, Profession.Builder, null, false, 5, 1, 0),
//    Pottery("Pottery", Need.CraftGoods, Profession.Crafter, null, true, 0.2, 1.5, 0.1),
//    Pottery("Pottery", Need.Utensil, Profession.Potter, null, true, 0.2, 1.5, 0.1),
   // Yarn("Yarn", Ne)
//    Cloth("Cloth", Need.Clothes, Profession.Weaver, null, true, 0.1, 1.5, 0.01)
    ;
//    static {
//        FlintBlades.equipment.add(Equipment.FlintAxes);
//    }

	static {
		Timber.materials.put(Resource.wood, 1);
		Bronze.materials.put(Resource.Copper, 1);
	}
    public String name;
    private Need need;
    private Profession profession;
    
    
    private Resource resource;//obsolete, - to replace with hashmap!
    private HashMap<IProducible, Integer> materials = new HashMap<>();
    
//    private HashSet<Equipment> equipment;
    private boolean consumable;
    private double laboriousness;
    private double weight;
    private SectorType sector;
    public Need getNeed() {
        return need;
    }

    protected void setResource (Resource resource) {
        this.resource = resource;
    }

    public void setProfession (Profession profession) {
        this.profession = profession;
    }

    public double getLaboriousness() {
        return laboriousness;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    public boolean isConsumable() {
        return consumable;
    }

    public Profession getProfession() {
        return profession;
    }

    public Resource getResource() {
        return resource;
    }

    public String getName() {
        return name;
    }
    Commodity(String name, SectorType sector, boolean consumable, double laboriousness, double weight) {
        this.name = name;
        this.sector = sector;
        this.consumable = consumable;
        this.laboriousness = laboriousness;
        this.weight = weight;

    }

	@Override
	public SectorType getSector() {
		return sector;
	}

	@Override
	public HashMap<IProducible, Integer> getMaterials() {
		// TODO Auto-generated method stub
		return materials;
	}

}
