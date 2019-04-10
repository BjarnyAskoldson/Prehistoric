package com.company.Enumerations;

import java.util.Map;

import com.company.Gameplay.Settlement;

public enum Need {
    Food ("Food", true, true),
    Tools ("Tools", false, false),
    Dwelling ("Dwelling", false, false),
    Weaponry ("Weaponry", false, false),
    Utensil ("Utensil", false, true),
    Clothes("Clothes",false, true),
    Furniture("Furniture", false, true),
    CraftGoods("CraftGoods", false, true),
    Transport("Transport", false, false),
    IndustryMaterial("Industry material", false, false)
    ;

    private String name;
    private boolean isVital;
    private boolean consumesCommodity;

    public boolean consumesCommodities() {
        return consumesCommodity;
    }

    public String getName() {
        return name;
    }
    public boolean isVital() {
        return isVital;
    }
    Need(String name, boolean isVital, boolean consumesCommodity) {
        this.name = name;
        this.isVital = isVital;
        this.consumesCommodity = consumesCommodity;
    }
//    public double satisfied (Settlement settlement) {
//         int commoditiesAvailable = 0;
//        for (Map.Entry<Commodity, Integer> commodity: settlement.getCommodities().entrySet())
//            if (commodity.getKey().equals(Commodity.Food))
//                commoditiesAvailable += commodity.getValue();
//        return (double)commoditiesAvailable/settlement.getTotalPeople();
//    }
}
