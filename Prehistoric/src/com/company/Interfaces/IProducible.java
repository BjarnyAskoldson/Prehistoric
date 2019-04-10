package com.company.Interfaces;

import java.util.HashMap;

import com.company.Enumerations.SectorType;

//import com.company.Enumerations.Need;
//import com.company.Enumerations.Profession;
//import com.company.Enumerations.Resource;

public interface IProducible {
//    Need getNeed();
//    Profession getProfession();
//    void setProfession(Profession profession);
    double getLaboriousness();
    SectorType getSector();
    double getWeight();
    boolean isConsumable();
    HashMap<IProducible, Integer> getMaterials();
}
