package com.company.Enumerations;

import com.company.Interfaces.IProducible;

import java.util.HashSet;

public enum Profession {
    //Hunter ("Hunter", BranchOfKnowledge.Nature, WorkingGroupType.Nomadic),
    Miner ("Miner", BranchOfKnowledge.Nature, WorkingGroupType.Settled),
    Farmer ("Farmer", BranchOfKnowledge.Nature, WorkingGroupType.Settled),
//    Toolmaker("Toolmaker", BranchOfKnowledge.Craft, WorkingGroupType.Settled),
    Builder("Builder", BranchOfKnowledge.Craft, WorkingGroupType.Settled),
//    Armourer("Armourer", BranchOfKnowledge.Craft, WorkingGroupType.Settled),
    Trader("Trader", BranchOfKnowledge.Economy, WorkingGroupType.Nomadic),
//    Potter("Potter", BranchOfKnowledge.Craft, WorkingGroupType.Settled),
//    Weaver("Weaver", BranchOfKnowledge.Craft, WorkingGroupType.Settled),
//    Carpenter("Carpenter", BranchOfKnowledge.Craft, WorkingGroupType.Settled),
//    Skinner("Skinner", BranchOfKnowledge.Craft, WorkingGroupType.Settled),
    Crafter("Crafter", BranchOfKnowledge.Craft, WorkingGroupType.Settled)
    ;
    private String name;
    private BranchOfKnowledge branchOfKnowledge;
    private WorkingGroupType workingGroupType;
    public String getName() {
        return name;
    }

    public WorkingGroupType getWorkingGroupType() {
        return workingGroupType;
    }

    public BranchOfKnowledge getBranchOfKnowledge() {
        return branchOfKnowledge;
    }

    /**
     * returns list of all the commodities produced by a given profession
     * @return
     */
//    public HashSet<IProducible> getCommodities() {
//        HashSet<IProducible> result = new HashSet<>();
//        for (Commodity commodity : Commodity.values())
//            if (commodity.getProfession().equals(this))
//                result.add(commodity);
//
//        for (Equipment equipment : Equipment.values())
//            if (equipment.getProfession().equals(this))
//                result.add(equipment);
//
//        for (Resource resource : Resource.values())
//            if (resource.getProfession().equals(this))
//                result.add(resource);
//        
//        for (WealthLevel wl : WealthLevel.values())
//            if (wl.getProfession().equals(this))
//                result.add(wl);
//
//        return result;
//
//    }

//    public HashSet<Need> getNeeds() {
//        HashSet<Need> result = new HashSet<>();
//        for (IProducible item : getCommodities())
//            result.add(item.getNeed());
//        return result;
//    }

//    public Resource getResource () {
//        return  resource;
//    }
//    private Resource resource;
    Profession(String name, BranchOfKnowledge branchOfKnowledge, WorkingGroupType workingGroupType/* IProducible commodity, Resource resource*/) {
        this.name = name;
        this.branchOfKnowledge = branchOfKnowledge;
        this.workingGroupType = workingGroupType;
//        this.commodity = commodity;
//        this.resource = resource;
//        if (commodity!=null)
//            commodity.setProfession(this);

//        if (resource!=null)
//            resource.setProfession(this);

    }
}
