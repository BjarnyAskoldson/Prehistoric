package com.company.Enumerations;

import java.util.HashMap;
import java.util.HashSet;

import com.company.Interfaces.IProducible;

//import java.util.HashSet;

//import com.company.Interfaces.IProducible;

public enum EnterpriseType implements IProducible {
//	Farm ("Farm", 1,4),
//	WOrkshop ("Crafter's workshop",1,4);
	Farm ("Farm", SectorType.Agriculture, 1),
	Workshop ("Crafter's workshop", SectorType.LightIndustry, 1),
	Builder("Builder's workshop", SectorType.Construction, 1),
	ShallowMine("Shallow mine", SectorType.Extracting,25),
	Factory("Factory", SectorType.LightIndustry, 500)
	;
	
	private String name;
	private HashMap<IProducible, Integer> materials = new HashMap<>();
	private double laboriousness;
	private HashSet<IProducible>ResourceInput = new HashSet<>();
	private SectorType sector;
//	private int minEmployees;
//	private int maxEmployees;
//	private HashSet<IProducible> products;

	static {
		Farm.ResourceInput.add(Resource.arableLand);
		ShallowMine.ResourceInput.add(Resource.flintStone);
	}

	public String getName() {
		return name;
	}
	
	EnterpriseType(String name, SectorType sector, double laboriousness/*, int minEmployees, int maxEmployees*/) {
		this.name = name;
		this.sector = sector;
		this.laboriousness = laboriousness;
//		this.setMinEmployees(minEmployees);
//		this.setMaxEmployees(maxEmployees);
	}
	
	public HashSet<IProducible> getResourceInput() {
		return ResourceInput;
	}

	@Override
	public double getLaboriousness() {
		return laboriousness;
	}

	@Override
	public double getWeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isConsumable() {
		// TODO Auto-generated method stub
		return false;
	}

	//Returns sector that allows to establish enterprises regardless of their output, i.e. Construction
	@Override
	public SectorType getSector() {
		return SectorType.Construction;
	}

	@Override
	public HashMap<IProducible, Integer> getMaterials() {
		return materials;
	}

//	public int getMinEmployees() {
//		return minEmployees;
//	}
//
//	public void setMinEmployees(int minEmployees) {
//		this.minEmployees = minEmployees;
//	}
//
//	public int getMaxEmployees() {
//		return maxEmployees;
//	}
//
//	public void setMaxEmployees(int maxEmployees) {
//		this.maxEmployees = maxEmployees;
//	}
}
