package com.company.Enumerations;

import java.util.HashSet;

import com.company.Interfaces.IProducible;

public enum SectorType {
	Agriculture("Agriculture")
	,Construction("Construction")
	,LightIndustry("Light Industry")
	,Extracting("Extracting")
	,HeavyIndustry("HeavyIndustry")
	,Trade("Trade")
	,Finances("Finances")
	,Energy("Energy")
	,HighTech("High-Tech")
	,Service("Service")
	,Military("Military")
	,PublicHealth("PublicHealth")
	,Education("Education")
	;
	private String name;
	private HashSet<IProducible> products = new HashSet<>();
	
	private SectorType (String name) {
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HashSet<IProducible> getProducts() {
		return products;
	}

	public void setProducts(HashSet<IProducible> products) {
		this.products = products;
	}

	public HashSet<IProducible> getCommodities() {
        HashSet<IProducible> result = new HashSet<>();
        for (Commodity commodity : Commodity.values())
            if (commodity.getSector().equals(this))
                result.add(commodity);

        for (Equipment equipment : Equipment.values())
            if (equipment.getSector().equals(this))
                result.add(equipment);

        for (Resource resource : Resource.values())
            if (resource.getSector().equals(this))
                result.add(resource);
        
        for (WealthLevel wl : WealthLevel.values())
            if (wl.getSector().equals(this))
                result.add(wl);

        return result;

    }
	

}
