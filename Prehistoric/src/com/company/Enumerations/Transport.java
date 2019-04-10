package com.company.Enumerations;

import java.util.HashMap;

import com.company.Interfaces.IProducible;

public enum Transport implements IProducible {
    Canoe ("Canoe", SectorType.LightIndustry, 5, 1, 1, 1)
    ;
    private String name;
    private int capacity;
    private int crew;
    private double laboriousness;
    private double weight;
    private Profession profession;
    private HashMap<IProducible, Integer> materials = new HashMap<>();   
    private SectorType sector;

    public Resource getResource() {return null;};

    public double getLaboriousness() {
        return laboriousness;
    }

    public boolean isConsumable() {
        return false;
    }

    public Profession getProfession() {
        return profession;
    }

    public double getWeight() {
        return weight;
    }

    public int getCrew() {
        return crew;
    }

    public int getCapacity() {
        return capacity;
    }
    
    Transport (String name, SectorType sector, int capacity, int crew, double laboriousness, double weight) {
        this.setName(name);
        this.sector = sector;
        this.capacity = capacity;
        this.crew = crew;
        this.laboriousness = laboriousness;
        this.weight = weight;
    }

	@Override
	public SectorType getSector() {
		return sector;
	}

	@Override
	public HashMap<IProducible, Integer> getMaterials() {
		return materials;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
