package com.company.Enumerations;

import java.util.HashMap;

import com.company.Interfaces.IProducible;

public enum WealthLevel implements IProducible {
	Homeless ("Homeless",0.05, 0, 0, 0, 0.1),
	Hut("Hut",0.07, 0.05, 5, 0.30, 0.5),
	House("House", 0.10, 0.2, 10, 0.5, 1),
	WarmHouse("Insulated house", 0.15, 0.5, 25, 1, 2),
	GlazedHouse("Glazed house", 0.20, 1, 50, 1.5, 4),
	PipedHouse("House with plumbing", 0.25, 2, 100, 2, 10),
	ElectrifiedHouse("House with electric appliances", 0.30, 5, 250, 2, 25),
	SmartHouse("House with electronic appliances", 0.40, 10, 500, 0, 50)
	;
	private String name;
	private double normalTax;
	private double defaultSalary;
	private int Laboriousness;
	private double naturalGrowthBonus;
	private double expenses;
    private HashMap<IProducible, Integer> materials = new HashMap<>();
	
	WealthLevel(String name, double normalTax, double defaultSalary, int Laboriousness, double naturalGrowthBonus, double expenses) {
		this.setName(name);
		this.setNormalTax(normalTax);
		this.defaultSalary = defaultSalary;
		this.Laboriousness = Laboriousness;
		this.setNaturalGrowthBonus(naturalGrowthBonus);
		this.setExpenses(expenses);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getNormalTax() {
		return normalTax;
	}
	
	public WealthLevel getNextLevel() {
		WealthLevel result = Homeless;
		switch(this){
        case Homeless:
        	result = Hut;
            break;
        case Hut:
            result = House;
            break;
        case House:
            result = WarmHouse;
            break;
		default:
			break;
           
    };
    return result;
	}

	public void setNormalTax(double normalTax) {
		this.normalTax = normalTax;
	}
	public double getNaturalGrowthBonus() {
		return naturalGrowthBonus;
	}

	public void setNaturalGrowthBonus(double naturalGrowthBonus) {
		this.naturalGrowthBonus = naturalGrowthBonus;
	}

	public double getExpenses() {
		return expenses;
	}

	public void setExpenses(double expenses) {
		this.expenses = expenses;
	}

	@Override
	public double getLaboriousness() {
		return Laboriousness;
	}

	@Override
	public double getWeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isConsumable() {
		return false;
	}

	//Returns sector that allows to upgrade properties, i.e. Construction
	@Override
	public SectorType getSector() {
		return SectorType.Construction;
	}

	@Override
	public HashMap<IProducible, Integer> getMaterials() {
		// TODO Auto-generated method stub
		return materials;
	}

	public double getDefaultSalary() {
		// TODO Auto-generated method stub
		return defaultSalary;
	}
}
