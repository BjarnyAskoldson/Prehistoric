package com.company.Gameplay;

import java.io.Serializable;

import com.company.Enumerations.Asset;
import com.company.Enumerations.EnterpriseType;

public class Enterprise implements Serializable {
	private EnterpriseType enterpriseType;
	//private int employees;
	private Asset asset;
	public Enterprise(EnterpriseType enterpriseType, Asset asset) {
		this.setEnterpriseType(enterpriseType);
		this.asset = asset;
	}
	
	@Override
	public boolean equals (Object o) {
		if (o==this)
			return true;
		if (!(o instanceof Enterprise)) { 
            return false; 
        } 
		
		return this.asset.equals(((Enterprise) o).asset) & this.enterpriseType.equals(((Enterprise)o).enterpriseType);
	
	}

	@Override
	public String toString() {
		String result = enterpriseType.getName() + " using " + asset.getName();
		return result;
	}
	public Asset getAsset() {
		return asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
	}

	public EnterpriseType getEnterpriseType() {
		return enterpriseType;
	}

	public void setEnterpriseType(EnterpriseType enterpriseType) {
		this.enterpriseType = enterpriseType;
	}
}
