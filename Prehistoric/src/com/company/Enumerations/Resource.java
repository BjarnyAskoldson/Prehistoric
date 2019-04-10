package com.company.Enumerations;

import java.util.HashMap;

import com.company.Interfaces.IProducible;

/**
 * class for natural resources, - wild game, flintstone etc.
 */
public enum Resource implements IProducible {
    wildGame ("Wild Game", SectorType.Agriculture, true, 1, 1),
    arableLand("Arable land", SectorType.Agriculture, true, 1, 1),
    wood("Wood", SectorType.Extracting, true, 1, 1),
    Copper("Copper ore", SectorType.Extracting, false, 1,1),
    flintStone ("Flint Stone", SectorType.Extracting, false, 2, 1);

    private String name;
    private boolean isRenewable;
//    private Commodity commodity;
    private int commodityYield;
    private double weight;
    private SectorType sector;

    @Override
    public double getLaboriousness() {
        return 1;
    }

    @Override
    public boolean isConsumable() {
        return false;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    public int getCommodityYield() {
        return commodityYield;
    }
//
//    public Commodity getCommodity() {
//        return commodity;
//    }
    public boolean isRenewable() {
        return this.isRenewable;
    }

    public String getName() {
        return name;
    }
    public static Resource getResourceByName (String resourceName) {
        for (Resource res : Resource.values()
             ) {
            if (res.name==resourceName) {
                return res;
            }
        }
        return null;
    }
    Resource(String name, SectorType sector, boolean isRenewable, int commodityYield, double weight) {
        this.name = name;
        this.isRenewable = isRenewable;
 //       this.commodity = commodity;
        this.sector = sector;
//        if (commodity!=null)
//            commodity.setResource(this);
        this.commodityYield = commodityYield;
        this.weight = weight;
    }

	@Override
	public SectorType getSector() {
		return sector;
	}

	@Override
	public HashMap<IProducible, Integer> getMaterials() {
		//Resources doesn't need materials
		return new HashMap<IProducible, Integer>();
	}
}
