package com.company.Enumerations;
//import java.util.Comparator;
import java.util.HashMap;

import com.company.Interfaces.IProducible;

public enum Asset implements IProducible {
//	industrial assets,
	StoneTools ("Stone tools", SectorType.LightIndustry, 0.1, 1.05, 0.1, 1.1, EnterpriseType.Workshop, 1),
	FlintTools ("Flynt tools", SectorType.LightIndustry, 0.1, 1.1, 0.1, 1.2, EnterpriseType.Workshop, 1),
	BronzeTools ("Bronze tools", SectorType.LightIndustry, 0.1, 1.5, 0.5, 2, EnterpriseType.Workshop, 1),
	IronTools ("Iron tools", SectorType.LightIndustry, 0.1, 2, 1, 3, EnterpriseType.Workshop, 1),
	WaterPoweredMachinery ("Water Powered Machinery", SectorType.LightIndustry, 2, 3, 10, 5, EnterpriseType.Workshop, 10),
	SteamPoweredMachinery ("Steam Powered Machinery", SectorType.LightIndustry, 10, 5, 1000, 7, EnterpriseType.Factory, 500),
	ElectricMachinery ("Electric Machinery", SectorType.HeavyIndustry, 20, 10, 2000, 20, EnterpriseType.Factory, 250),
	AlgorythmicRobots ("Algorythmic Robots", SectorType.HeavyIndustry, 20, 25, 5000, 100, EnterpriseType.Factory, 100),
	AIRobots ("AI Robots", SectorType.HeavyIndustry, 30, 50, 10000, 200, EnterpriseType.Factory, 25)
	
	;
	
//	Canoe,
//	Galley;
	
	static {
		StoneTools.entepriseTypes.put(SectorType.Agriculture, EnterpriseType.Farm);
		StoneTools.entepriseTypes.put(SectorType.LightIndustry, EnterpriseType.Workshop);
		StoneTools.entepriseTypes.put(SectorType.Construction, EnterpriseType.Builder);
		StoneTools.entepriseTypes.put(SectorType.Extracting, EnterpriseType.ShallowMine);
		FlintTools.entepriseTypes.put(SectorType.Agriculture, EnterpriseType.Farm);
		FlintTools.entepriseTypes.put(SectorType.LightIndustry, EnterpriseType.Workshop);
		FlintTools.entepriseTypes.put(SectorType.Construction, EnterpriseType.Builder);
		FlintTools.entepriseTypes.put(SectorType.Extracting, EnterpriseType.ShallowMine);
		BronzeTools.entepriseTypes.put(SectorType.Agriculture, EnterpriseType.Farm);
		BronzeTools.entepriseTypes.put(SectorType.LightIndustry, EnterpriseType.Workshop);
		BronzeTools.entepriseTypes.put(SectorType.Construction, EnterpriseType.Builder);
		IronTools.entepriseTypes.put(SectorType.Agriculture, EnterpriseType.Farm);
		IronTools.entepriseTypes.put(SectorType.LightIndustry, EnterpriseType.Workshop);
		IronTools.entepriseTypes.put(SectorType.Construction, EnterpriseType.Builder);
	}
	
	private String name ;
	private double operationalCosts;
	private double defaultSalary;
//    private Profession profession;
    private SectorType sector;
//    private HashSet<Equipment> equipment;
    private double laboriousness;
    private double efectiveness;
 //   private EnterpriseType entepriseType;
    private HashMap<SectorType, EnterpriseType> entepriseTypes = new HashMap<>();
    private int workers;
    private HashMap<IProducible, Integer> materials = new HashMap<>();
    
    static {
    	FlintTools.materials.put(Resource.flintStone, 1);
    	
    }
    
    
    private Asset(String name, SectorType sector, double operationalCosts,  double defaultSalary, double laboriousness, double effectiveness, EnterpriseType enterpriseType, int workers) {
    	this.name = name;
    	this.sector = sector;
    	this.operationalCosts = operationalCosts;
    	this.defaultSalary = defaultSalary;
    	this.laboriousness = laboriousness;
    	this.efectiveness = effectiveness;
//    	this.entepriseType = enterpriseType;
    	this.workers = workers;
    	
    }

	@Override
	public double getLaboriousness() {
		return laboriousness;
	}

	@Override
	public double getWeight() {
		return 0;
	}

	@Override
	public boolean isConsumable() {
		return false;
	}

	public double getEffectiveness() {
		return efectiveness;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getOperationalCosts() {
		return operationalCosts;
	}

	public void setOperationalCosts(double operationalCosts) {
		this.operationalCosts = operationalCosts;
	}

	public EnterpriseType getEntepriseType(SectorType type) {
		return entepriseTypes.getOrDefault(type, null);
	}

	public int getWorkers() {
		return workers;
	}

	public void setWorkers(int workers) {
		this.workers = workers;
	}

	@Override
	public SectorType getSector() {
		return sector;
	}

	@Override
	public HashMap<IProducible, Integer> getMaterials() {
		return materials;
	}

	public double getDefaultSalary() {
		return defaultSalary;
	}
	
	
	
}
