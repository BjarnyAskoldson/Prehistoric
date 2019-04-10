package com.company.Gameplay;

import com.company.Enumerations.*;
import com.company.Interfaces.IAbleToFight;
import com.company.Interfaces.IMoveable;
import com.company.Interfaces.IProducible;
import javafx.scene.image.ImageView;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

/**
 * Working group describes a group of people on a given cell from a given settlement
 */
public class WorkingGroup implements IAbleToFight, IMoveable, Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	private int people;
//    private int peopleMax;
    private Resource resource;
    private Need need;
    private Profession profession;
	private SectorType sectorType;
    private Hex location;
    private Hex destination;
    private Settlement homebase;
    private Hex camp;
    private IProducible mainAsset;
    private int product = 0;
    private boolean aggressive;
    private boolean resourceDepleted = false;
//    private boolean actedThisTurn = false;
//    private double employmentCoefficient = 1;
    private boolean collectedRenewableResourceThisTurn = false;
    private transient ImageView imageView;
//    private WorkingGroupType type;
    public boolean reflectedOnMap = false;
	private HashMap<Enterprise, Integer> enterprises = new HashMap<>();
	private HashMap<Asset, Integer> plannedUpgrades = new HashMap<>();
	private HashMap<Enterprise, Double> plannedNewEnterprises = new HashMap<>();
    private HashMap<WealthLevel,Integer> wealthLevels = new HashMap<>();
    //Variable to keep not invested, not spent leftover from past years 
    private HashMap<WealthLevel,Double> workerSavings = new HashMap<>();
//    private int savings = 0;
//    private HashMap<Equipment, Integer> equipment = new HashMap<>();
//    private Equipment equipmentToGet;


    @Override
    public boolean isMissionCompleted() {
        return false;
    }
//
//    public double getEmploymentCoefficient() {
//        return employmentCoefficient;
//    }
//
//    public void setEmploymentCoefficient(double employmentCoefficient) {
//        this.employmentCoefficient = employmentCoefficient;
//    }

    /**
     * Calculate new wealth levels after the end of economical year
     */
//    private void updateWealthLevels() {
//    	double totalIncome = getIncome();
//    	for (Map.Entry<WealthLevel, Integer> wl : wealthLevels.entrySet()) {
//    		//amount to spend after current expenses are covered
//    		if (savings.getOrDefault(wl.getKey(), 0.0)==null)
//    			savings.put(wl.getKey(), 0.0);
//    		double amountToInvestForThisWL = people == 0 ?  0 : savings.getOrDefault(wl.getKey(), 0.0) + (totalIncome*wl.getValue()/people) - wl.getKey().getExpenses()*wl.getValue();    		
//    		if (getOwner().getHousesAvailable().contains(wl.getKey().getNextLevel())&&amountToInvestForThisWL>0) {
//    			int housesCanBuy = (int)(amountToInvestForThisWL/wl.getKey().getNextLevel().getLaboriousness());
//    			int housesBought = housesCanBuy;
//    			//Number of houses to buy on next level is limited by number of people on current level 
//    			if ( wealthLevels.get(wl.getKey())<housesBought)
//    				housesBought = wealthLevels.get(wl.getKey());
//    			//Decrease the amount of houses of the current wealth level
//    			wealthLevels.put(wl.getKey(), wealthLevels.get(wl.getKey())-housesBought);
//    			//Increase the amount of houses for the next level
//    			wealthLevels.put(wl.getKey().getNextLevel(), wealthLevels.getOrDefault(wl.getKey().getNextLevel(),0)+housesBought);
//    			//Sums spent on properties' upgrades cannot be invested
//    			amountToInvestForThisWL -= housesBought*wl.getKey().getNextLevel().getLaboriousness();
//    		} 
//   			//TO DO: invest into business enterprise!
//    		savings.put(wl.getKey(), amountToInvestForThisWL);
//    	}
//  
//    }
//    
    /**
     * Method to return number of properties purchased by working group last year
     * @return
     */
    public HashMap<WealthLevel,Integer> getHousesBought(){
    	HashMap<WealthLevel,Integer> result = new HashMap<>();
    	double totalIncome = 0;//getIncome();
    	for (Map.Entry<WealthLevel, Integer> wl : wealthLevels.entrySet()) {
    		double amountToInvestForThisWL = (totalIncome*wl.getValue()/getPeople()) - wl.getKey().getExpenses()*wl.getValue();    		
    		if (getOwner().getHousesAvailable().contains(wl.getKey().getNextLevel())&&amountToInvestForThisWL>0) {
    			int housesCanBuy = (int)(amountToInvestForThisWL/wl.getKey().getNextLevel().getLaboriousness());
    			int housesBought = housesCanBuy;
    			//Number of houses to buy on next level is limited by number of people on current level 
    			if ( wealthLevels.get(wl.getKey())<housesBought)
    				housesBought = wealthLevels.get(wl.getKey());
    			result.put(wl.getKey().getNextLevel(), housesBought);
    		} else {
    			//To do: downgrade properties in case of negative excess amounts
    		}
    	}
    		return result;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public Need getNeed() {
        return need;
    }
//
//    public WorkingGroupType getType() {
//        return type;
//    }

    @Override
    public boolean atTarget() {
        return false;
    }

    public IProducible getMainAsset() {
        return mainAsset;
    }

    @Override
    public Tribe getOwner() {

        return homebase.getOwner();
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public void takeCaptives(int captives) {

    }



    //    public void addEquipment (Equipment equipment, int amount) {
//        this.equipment.put(equipment,amount);
//    }


    public boolean isResourceDepleted() {
        return resourceDepleted;
    }

//    public int getPeopleMax() {
//        return peopleMax;
//    }

    public void setAggressive(boolean aggressive) {
        this.aggressive = aggressive;
    }

//    public void setPeopleMax(int peopleMax) {
//        this.peopleMax = peopleMax;
//    }

    public boolean isAggressive() {
        return aggressive;
    }

    public Hex getDestination() {
        return destination;
    }

    public void setActedThisTurn(boolean actedThisTurn) {
        //this.actedThisTurn = false;actedThisTurn;
    }

    public boolean collectedRenewableResourceThisTurn() {
        return collectedRenewableResourceThisTurn;
    }

    public int getFirepower() {
    	return 0;
//        double result = homebase.toolEffectiveness()*people* hexgame.WorkersDeathTool;

//        for (Map.Entry<Equipment, Integer> e : equipment.entrySet()) {
//            result += e.getValue()*e.getKey().getDeathToll();
//        }
        //return (int)result;
    }
    public HashMap<WealthLevel,Integer> getWealthLevels() {
		return wealthLevels;
	}
//
//	public void setWealthLevels(HashMap<WealthLevel,Integer> wealthLevels) {
//		this.wealthLevels = wealthLevels;
//	}

	public void assignPeople (int newHeadcount) {
		double coeff = (double) newHeadcount/getPeople();
		HashMap <WealthLevel, Integer> wealthLevelsCopy = new HashMap<>(wealthLevels);
		for (Map.Entry<WealthLevel, Integer> wl : wealthLevelsCopy.entrySet()) {
			wealthLevels.put(wl.getKey(), (int)(wl.getValue()*coeff));
		}
//        people = newHeadcount;
//        int defaultEquipmentAmount = 0;
//        int nonDefaultEquipmentAmount = equipment.entrySet().stream().filter(a->!a.getKey().equals(homebase.getDefaultEqupment())).map(Map.Entry::getValue).reduce(0,Integer::sum);
//
//        if (nonDefaultEquipmentAmount<newHeadcount)
//            defaultEquipmentAmount = newHeadcount-nonDefaultEquipmentAmount;
//
//        equipment.put(homebase.getDefaultEqupment(),defaultEquipmentAmount);
    }
    public int getPeople() {
    	int totalPeople = 0;
    	for (Map.Entry<WealthLevel, Integer> wl : wealthLevels.entrySet())
    		totalPeople += wl.getValue();
        return totalPeople;
    }

    public Hex getCamp() {
        return camp;
    }

    public int getResourceCollected() {
        return product;
    }
    public void setResourceCollected(int product){
        this.product = product;
    }

    public void disband (){
        this.getHomeBase().getWorkingGroups().remove(this);
        this.getCamp().getWorkingGroupsCamps().remove(this);
        this.getLocation().getWorkingGroups().remove(this);

    }

    /**
     * Returns descriptive name for the workgroup
     * @return
     */
    public String getName() {
        String result = getPeople()+ " ";
        if (sectorType!=null)
            result += sectorType.getName();
        else if (camp==homebase.getLocation())
            //volunteers
            result += " volunteers dreaming of " + need.getName() + " industry";
        else
            //migrants
            result += need.getName() + " migrants to " + camp.getHexName();

        result += " from " + getHomeBase().getName();

        if (camp!=homebase.getLocation())
            result += ", camp at ("+camp.getX()+","+camp.getY()+")";
        return result;
    }

    public void addPeople (int peopleToAdd) {    	
 //       people += peopleToAdd;
        if (peopleToAdd > 0)
        	//TO do: do we need to define start wealth level for newcomers somehow?
        	wealthLevels.put(WealthLevel.Homeless, wealthLevels.getOrDefault(WealthLevel.Homeless, 0)+peopleToAdd);
        
    }
    public Profession getProfession() {
        return profession;
    }
    
    /**
     * Shows ratio between demand on workgroup's production and its capacity
     * if >1, the group is overloaded
     * @return
     */
//    public double getWorkloadCoefficient() {
//    	double result;
//    	double demand = 0;
//       	for (IProducible commodity : profession.getCommodities())
//       		demand += homebase.maximalCommodityPerTurn(commodity) * commodity.getLaboriousness();
//       	
//       	if (people*homebase.toolEffectiveness()==0)
//       		result = 1;
//       	else if (demand>people*homebase.toolEffectiveness())
//       		result = demand/(people*homebase.toolEffectiveness());
//       	//If demand on group's product is higher than its available time, coefficient = 1, - we don't do overtime
//       	else result = 1;
//    	return result;
//    }

    /**
     * Method to return forecasted income for the group
     * @return
     */
//    public int getIncome() {
//    	int result = 0;
//    	
//    	if (profession==null)
//    		return result;
//    	
//
//    	//find  total people in the same profession in the settlement
//    	Optional<Integer> totalPeopleOpt = homebase.getWorkingGroups().stream()
//    			.filter(a->a.profession!=null)
//    			.filter(a->a.profession.equals(this.profession))
//    			.map(a->a.people)
//    			.reduce(Integer::sum);
//    			//.get();
//    	int totalPeople = 0;
//    	if (totalPeopleOpt.isPresent())
//    		totalPeople = totalPeopleOpt.get();
//    	//Find product of a given profession
//    	int totalProduct = 0;
//    	for (IProducible commodity : profession.getCommodities())
//    		totalProduct += homebase.commodityPerTurn(commodity, false);
//    	if (totalPeople!=0)
//    		result = (int)(totalProduct*(double)people/totalPeople);
//    	else 
//    		result = totalProduct;
//    	return result;
//    }
//    //return maximum possible product yield per turn (regardless demand and resources available)
//    public int getProductPerTurn() {
//        int yield = (int) (people*homebase.toolEffectiveness()*( resource==null ? 1 : resource.getCommodityYield()));
//
////        for (Map.Entry<Equipment, Integer> equipment : equipment.entrySet())
////            yield += equipment.getValue()*equipment.getKey().getResourceYield()*( resource==null ? 1 : resource.getCommodityYield());
//
//        //1 turn for load, 1 turn for unload + doubled time to travel between camp and homebase for travelling groups
//        //1 turn for settled working groups
//        //int turnsToGetYield = (camp==homebase.getLocation())? 1 : 2 + 2*(HexMap.getPath(homebase.getLocation(),camp).size()-1);
//        int turnsToGetYield = (profession.getWorkingGroupType()==WorkingGroupType.Settled)? 1 : 2 + 2*(HexMap.getPath(homebase.getLocation(),camp).size()-1);
//        return yield/turnsToGetYield;
//    }
    //return resource yield per turn
//    public int getResourcePerTurn() {
//        int yield = (int) (people*homebase.toolEffectiveness());
//
////        for (Map.Entry<Equipment, Integer> equipment : equipment.entrySet())
////            yield += equipment.getValue()*equipment.getKey().getResourceYield();
//
//        //1 turn for load, 1 turn for unload + doubled time to travel between camp and homebase for travelling groups
//        //1 turn for settled working groups
//        int turnsToGetYield = (camp==homebase.getLocation())? 1 : 2 + 2*(HexMap.getPath(homebase.getLocation(),camp).size()-1);
//        return yield/turnsToGetYield;
//    }
    public Resource getResource() {
        return resource;
    }

    /**
      this method makes a working group to do its action. If the group have already reached its destination, it collects
      the resource it is after and preparing to go home. If not, it does a step towards destination. If loaded and at home, unloads the resource
      and set sail to resource area again
     */
//   public void processDay (){
//        collectedRenewableResourceThisTurn = false;
//
////        updateWealthLevels();
//
//        //if acted already (e.g. participated in battle), pass the turn
//        if (actedThisTurn) {
//            actedThisTurn = false;
//            return;
//        }
//        //buffer groups don't do anything
//        if (type == WorkingGroupType.Buffer)//this.equals(homebase.getBufferGroup(this.need))&&profession==null)
//            return;
//
//        //process nomadic groups (hunters, traders etc.)
//       if (type==WorkingGroupType.Nomadic) {
//           //groups that lost all people should return to replenish the headcount
//            if (people==0 && !destination.equals(homebase.getLocation()))
//                destination = homebase.getLocation();
//
//           if (/*(people>0 || destination.equals(homebase.getLocation())) &&*/ HexMap.getPath(location, destination).size() > 1) {
//               //if group is on its way, take a step towards destination. Empty group can move towards homebase only, to get reinforcements
//               location.getWorkingGroups().remove(this);
//               location = HexMap.getPath(location, destination).get(1);
//               location.getWorkingGroups().add(this);
//               return;
//           }
//
//           if (location.equals(camp) && product == 0 && resource != null && people > 0) {
//               //resource collecting group reached resource area and ready to collect the resource
//               int resourceAvailable = camp.getResources().getOrDefault(resource, 0);
//               if (people <= resourceAvailable)
//                   product = people;
//               else
//                   product = resourceAvailable;
//
//               getOwner().getTribeExperience().put(profession.getBranchOfKnowledge(), getOwner().getTribeExperience().getOrDefault(profession.getBranchOfKnowledge(), 0) + product);
//               if (!resource.isRenewable())
//                   camp.setResource(resource, resourceAvailable - product);
//               else
//                   collectedRenewableResourceThisTurn = true;
//               destination = homebase.getLocation();
//               return;
//           }
//           if (location.equals(homebase.getLocation())/* && location.equals(destination) && camp != null && !camp.equals(homebase.getLocation())*/) {
//               //nomadic group returned home
//               if (product != 0) {
//                   //...and ready to unload the resource
//                   homebase.addCommodity(resource.getCommodity(), product * resource.getCommodityYield());
//                   product = 0;
//               }
//               if (camp.getResources().getOrDefault(resource, 0) > 0)
//                   destination = camp;
//               else
//                   resourceDepleted = true;
//
//
//               //while in homebase, replenish the people by volunteers from buffer group if any
//               if (people < peopleMax && homebase.getBufferGroup(need).getPeople() > 0)
//                   assignPeople(people + Math.min(peopleMax - people, homebase.getBufferGroup(need).getPeople()));
//
//               return;
//           }
//
//       }
//       //migrants reached their destination
//       if (type == WorkingGroupType.Migrant) {//profession==null && location.equals(destination) && !homebase.getLocation().equals(destination)) {
//           if (location.equals(destination)) {
//               destination.getSettlement().addPeople(people);
//               this.disband();
//           } else {
//               //if group is on its way, take a step towards destination. 
//               location.getWorkingGroups().remove(this);
//               location = HexMap.getPath(location, destination).get(1);
//               location.getWorkingGroups().add(this);
//           }
//           return;
//       }
//       //static non-buffer group, - builders, toolmakers etc.
//       if (type==WorkingGroupType.Settled) {
//        //if (camp.equals(homebase.getLocation())/*&&profession!=null*/) {
//
//            //first, produce civilian commodities and related experience
//            for (IProducible productType : profession.getCommodities()) {
//            	
//            	if (productType.equals(homebase.getDefaultTool()))
//            			continue;
//                product = homebase.commodityPerTurn(productType, false);
//                //Lets accumulate food/goods
////                        if (!productType.isConsumable())
//                if (productType.getClass().toString().contains("Commodity"))
//                	homebase.addCommodity((Commodity)productType, product);
//                getOwner().getTribeExperience().put(profession.getBranchOfKnowledge(),getOwner().getTribeExperience().getOrDefault(profession.getBranchOfKnowledge(),0) + product);
//            }
//            for (Resource resource: Resource.values())
//            	if (resource.getProfession()!=null)
//            		if (resource.getProfession().equals(profession)) 
//            			homebase.getResources().put(resource, homebase.getResources().getOrDefault(resource, 0)+people);
//
//            //produce the army equipment ordered
////            if (profession.equals(Profession.Armourer)) {
////                //check how much labour can group provide for all orders
////                double productionCoefficient = getProductPerTurn()>homebase.getEquipmentOrdersLaboriousnessTotal()? 1 : (double)getProductPerTurn()/homebase.getEquipmentOrdersLaboriousnessTotal();
////                for (Map.Entry<Equipment, Integer> equipmentOrdered : homebase.getEquipmentOrders().entrySet()) {
////                    int equipmentToProduce = (int)(equipmentOrdered.getValue()*productionCoefficient);
////                    //check if we have sufficient resources to produce equipment; if not, adjust amount to produce
////                    if (equipmentOrdered.getKey().getResource()!=null) {
////                        int resourceAvailable = homebase.getResources().getOrDefault(equipmentOrdered.getKey().getResource(),0);
////                        if (equipmentToProduce>resourceAvailable) {
////                            if (resourceAvailable > 0)
////                                equipmentToProduce = resourceAvailable;
////                            else
////                                equipmentToProduce = 0;
////                        }
////                        homebase.getResources().put(equipmentOrdered.getKey().getResource(),resourceAvailable-equipmentToProduce);
////                    }
////                    homebase.getEquipment().put(equipmentOrdered.getKey(), homebase.getEquipment().getOrDefault(equipmentOrdered.getKey(),0)+equipmentToProduce);
////                    homebase.getEquipmentOrders().put(equipmentOrdered.getKey(), homebase.getEquipmentOrders().getOrDefault(equipmentOrdered.getKey(),0)-equipmentToProduce);
////                    getOwner().getTribeExperience().put(profession.getBranchOfKnowledge(),getOwner().getTribeExperience().getOrDefault(profession.getBranchOfKnowledge(),0) + equipmentToProduce);
////                }
////            }
//        } else {
//            //Non-static groups, - hunters etc.
////            if (location.equals(camp) && product == 0 && resource != null && people > 0) {
////                //resource collecting group reached resource area and ready to collect the resource
////                int resourceAvailable = camp.getResources().getOrDefault(resource, 0);
////                if (people <= resourceAvailable)
////                    product = people;
////                else
////                    product = resourceAvailable;
////
////                getOwner().getTribeExperience().put(profession.getBranchOfKnowledge(), getOwner().getTribeExperience().getOrDefault(profession.getBranchOfKnowledge(), 0) + product);
////                if (!resource.isRenewable())
////                    camp.setResource(resource, resourceAvailable - product);
////                else
////                    collectedRenewableResourceThisTurn = true;
////                destination = homebase.getLocation();
////                return;
////            }
//
////            if (location.equals(homebase.getLocation())/* && location.equals(destination) && camp != null && !camp.equals(homebase.getLocation())*/) {
////                //non-static group returned home
////                if (product != 0) {
////                    //...and ready to unload the resource
////                    homebase.addCommodity(resource.getCommodity(), product * resource.getCommodityYield());
////                    product = 0;
////                }
////                if (camp.getResources().getOrDefault(resource, 0) > 0)
////                    destination = camp;
////                else
////                    resourceDepleted = true;
////
////
////                //while in homebase, replenish the people by volunteers from buffer group if any
////                if (people < peopleMax && homebase.getBufferGroup(need).getPeople() > 0)
////                    assignPeople(people + Math.min(peopleMax - people, homebase.getBufferGroup(need).getPeople()));
////
////                return;
////            }
//
//        }
////        actedThisTurn = false;
//       
//    }
   
   public void distributeStarvation(int people) {
	   
	   //prepare ordered list of group's wealth levels, - from poorest to richest
       List<Map.Entry<WealthLevel, Integer>> wealthLevelsOrdered =
               new LinkedList<Map.Entry<WealthLevel, Integer>>(wealthLevels.entrySet());

       Collections.sort(wealthLevelsOrdered, new Comparator<Map.Entry<WealthLevel, Integer>>() {
           public int compare(Map.Entry<WealthLevel, Integer> o1,
                              Map.Entry<WealthLevel, Integer> o2) {
               return ((Integer)o1.getKey().ordinal()).compareTo((Integer)o2.getKey().ordinal());
           }
       });
       int peopleToStarve = people;
       for (Map.Entry<WealthLevel, Integer> wl : wealthLevelsOrdered) {
    	   int peopleToStarveOnThisWL = peopleToStarve;
    	   //We cannot starve more on WL than we have people there
    	   if(wl.getValue()<peopleToStarveOnThisWL)
    		   peopleToStarveOnThisWL = wl.getValue();
    	   
    	   wealthLevels.put(wl.getKey(), wl.getValue()-peopleToStarveOnThisWL);
    	   
    	   peopleToStarve -= peopleToStarveOnThisWL;
       }
	   
   }

    public WorkingGroup (int people, int peopleMax, Resource resource, SectorType sectorType, Hex camp, IProducible mainAsset, Settlement homebase, boolean aggressive/*, WorkingGroupType type*/){
//        this.people = people;
//        this.peopleMax = peopleMax;
        this.resource = resource;
//        this.need = need;
        this.sectorType = sectorType;
        this.location = homebase.getLocation();
        this.homebase = homebase;
        this.aggressive = aggressive;
//        this.type = type;
        this.mainAsset = mainAsset;
        this.wealthLevels.put(WealthLevel.Homeless, people);
        this.workerSavings.put(WealthLevel.Homeless, 0.0);
        //if group is migrant, set destination; otherwise set destination to homebase (for working groups, - to have a round of recruiting)
        if (camp.getSettlement()==null || camp.getSettlement().equals(homebase))//(type == WorkingGroupType.Settled || type==WorkingGroupType.Buffer)
            this.destination = homebase.getLocation();//camp;
        else
            this.destination = camp;
        this.camp = camp;
//        addEquipment(homebase.getDefaultEqupment(), people);
    }

	public SectorType getSectorType() {
		return sectorType;
	}
	/*
	 * returns revenue for the working group in a current FY
	 */
	private int getSectorRevenue() {
		int result = 0;
		for (IProducible product : sectorType.getProducts()) {
			result+=homebase.commodityPerTurn(product, false)*product.getLaboriousness();
		}
		return result;
	}
	/*
	 * Calculates working group's expenses, - wages, resource costs etc.
	 */
	private int getSectorExpenses() {
		int result = 0;
		for (Entry<Enterprise, Integer> enterprise: enterprises.entrySet()) {
			result += enterprise.getKey().getAsset().getOperationalCosts()*enterprise.getValue();
		}
		return result;
	}
	/*
	 * Returns maximum amount of commodity the sector can produce with existing assets
	 */
	protected int getPotentialProductivity(IProducible commodity) {
		double resultDouble = 0;
		//First, calculating productivity as double so as not to loose too much on rounding;  
		for (Map.Entry<Enterprise, Integer> enterprise : enterprises.entrySet()) {
			resultDouble += enterprise.getValue()*enterprise.getKey().getAsset().getEffectiveness()*enterprise.getKey().getAsset().getWorkers();
		}
		int result = (int)(resultDouble/commodity.getLaboriousness());
		return result;
	}
	
	/*
	 * Returns maximum amount of labour working group can provide
	 */
	protected int getTotalPotentialProductivity() {
		int result = 0;
		for (Map.Entry<Enterprise, Integer> enterprise : enterprises.entrySet()) {
			result += (int)enterprise.getValue()*(enterprise.getKey().getAsset().getEffectiveness()*enterprise.getKey().getAsset().getWorkers());
		}
		return result;
	}
	/*
	 * Return jobs available in the sector
	 */
	public int getVacancies() {
		int result = 0;
		
		//Workers required by all enterprises...
		for (Map.Entry<Enterprise, Integer> enterprise : enterprises.entrySet()) {
			result += enterprise.getKey().getAsset().getWorkers()*enterprise.getValue();
		}
		//...minus Workers already in sector
		for (Map.Entry<WealthLevel, Integer> wl : wealthLevels.entrySet()) {
			result -= wl.getValue();
		}
		return result;
	}
	
	public void preProcessYear() {
		//get upgrades ordered last year
		upgradeEnterprises();
		//establish enterprises planned last year
		establishNewEnterprises();
		
	}
	
	public void processDay() {
		//getNewWorkers();
		int capitalToInvest = homebase.getInvestmentIntoSector(this.sectorType);
		if (capitalToInvest>0) {
			//Improve the sector
			//First, lets try to upgrade the enterprises with better assets
			for (Map.Entry<Enterprise, Integer> enterprise : enterprises.entrySet())
				if (enterprise.getKey().getAsset().getEffectiveness()<homebase.getBestAsset().getEffectiveness())
					capitalToInvest = scheduleUpgradesNextYear(enterprise.getKey(), homebase.getBestAsset(), capitalToInvest);
			if (capitalToInvest>0)
			//If we still have some capital, establish new enterprises
				capitalToInvest = scheduleNewEnterprisesNextYear(capitalToInvest);
		}
		updateWealthLevels();
		this.shrinkWorkGroup();
		
		//clean buildings' and assets' demand for this year, to start from 0 on the next year
		
		for (IProducible asset : homebase.getDemand().keySet()) {
			if (sectorType.equals(SectorType.Construction)) {		
				if ((asset.getClass().toString().contains("EnterpriseType"))||(asset.getClass().toString().contains("WealthLevel")))
					homebase.getDemand().put(asset, 0);
			}				

			if (sectorType.equals(SectorType.LightIndustry)) {					
				if ((asset.getClass().toString().contains("Asset"))&(asset.getSector().equals(SectorType.LightIndustry)))							
					homebase.getDemand().put(asset, 0);
			}				
			if (sectorType.equals(SectorType.HeavyIndustry)) {					
				if ((asset.getClass().toString().contains("Asset"))&(asset.getSector().equals(SectorType.HeavyIndustry)))							
					homebase.getDemand().put(asset, 0);
			}				
		}
			
		//savings = capitalToInvest;
		
	}
	/*
	 * Change work group size according to financial results, - expand if profitable, shrink otherwise
	 */
	public void recalculateWorkGroupSize() {
		upgradeEnterprises();
		establishNewEnterprises();
		shrinkWorkGroup();
	}
	
	/*
	 * Close enterprises if unprofitable
	 */
	private void shrinkWorkGroup() {
		int profit = getProfit();
		if (profit<0) {
//			double excessProductivity = getTotalPotentialProductivity()*(-1*getProfit())/getSectorRevenue();
			 HashMap<Enterprise,Double> enterprisesSorted = new HashMap<>();
	        for (Map.Entry <Enterprise,Integer> enterprise: enterprises.entrySet())
	        	enterprisesSorted.put(enterprise.getKey(),enterprise.getKey().getAsset().getEffectiveness());
			
			      //  Convert Map to List of Map to do sorting
			  List<Map.Entry<Enterprise, Double>> list =
			          new LinkedList<Map.Entry<Enterprise, Double>>(enterprisesSorted.entrySet());
			
			
			  // Sort list with Collections.sort(), provide a custom Comparator
			  //    Can switch the o1 o2 position for a different order
			  Collections.sort(list, new Comparator<Map.Entry<Enterprise, Double>>() {
			      public int compare(Map.Entry<Enterprise, Double> o1,
			                         Map.Entry<Enterprise, Double> o2) {
			          return (o1.getValue()).compareTo(o2.getValue());
			      }
			  });
			
			  //Now iterate through sorted list, - from least efficient enterprises to most efficient
			  double uncoveredLoss = 0;
			      for (Map.Entry<Enterprise, Double> enterprise : list) {
			    	  double enterpriseExpenses = getEnterpriseExpenses(enterprise.getKey());
			    	  int enterprisesCurrentlyExisting = enterprises.getOrDefault(enterprise.getKey(),0);
			          int enterprisesToClose = uncoveredLoss > enterpriseExpenses ? 
			        		  enterprisesCurrentlyExisting 
			        		  : (int)(enterprisesCurrentlyExisting*uncoveredLoss/enterpriseExpenses);
	        		  uncoveredLoss -= enterprisesToClose/enterprisesCurrentlyExisting*enterpriseExpenses;
	        		  enterprises.put(enterprise.getKey(), enterprisesCurrentlyExisting - enterprisesToClose);
			      }
				
			}
		
	}
	/*
	 * Establish new enterprises funded last year, as far as builders can handle this
	 */
	private void establishNewEnterprises() {
		HashMap<Enterprise, Double> plannedEnterprisesCopy = new HashMap<>(plannedNewEnterprises);
		Optional<Integer> maxBuildersProductivityOptional = homebase.getWorkingGroups().stream()
				.filter(a->a.sectorType.equals(SectorType.Construction))
				.map(a->a.getTotalPotentialProductivity())
				.reduce(Integer::sum)
				
				;
		int maxBuildersProductivity = maxBuildersProductivityOptional.isPresent() ? maxBuildersProductivityOptional.get() : 0;
		
		Optional <Integer> totalBuildingDemandOptional = homebase.getDemand().entrySet().stream()
				.filter(a->a.getValue()>0)
				.map(a->a.getValue())
				.reduce(Integer::sum);
		int totalBuildingDemand = totalBuildingDemandOptional.isPresent() ? totalBuildingDemandOptional.get() : 0;
		//Check if builders can handle the demand on new building projects; 
		//if not, their capacity is a limit, unless we are creating building enterprises (chicken&egg issue)  
		double buildersAvailableCoefficient = 
				maxBuildersProductivity>=totalBuildingDemand ||sectorType.equals(SectorType.Construction) 
					? 1 : ((double)(maxBuildersProductivity)/totalBuildingDemand);
	
		
				
		for (Map.Entry<Enterprise, Double> newEnterprise : plannedEnterprisesCopy.entrySet()) {
/*			Optional <Enterprise> existingEnterpisesKeyOptional = enterprises.keySet().stream()
					.filter(a->a.getAsset().equals(newEnterprise.getKey().getAsset()))
					.filter(a->a.getEnterpriseType().equals(newEnterprise.getKey().getEnterpriseType()))
					.findFirst()
					;
			Enterprise existingEnterpisesKey 
*/					
			//We cannot bankrupt more enterprises that exist 
			if (newEnterprise.getValue()+enterprises.getOrDefault(newEnterprise.getKey(), 0)>0) {
				double enterprisesToEstablish = newEnterprise.getValue()*buildersAvailableCoefficient;
				plannedNewEnterprises.put(newEnterprise.getKey(), plannedNewEnterprises.getOrDefault(newEnterprise.getKey(), 0.0)-enterprisesToEstablish);
				enterprises.put(newEnterprise.getKey(), enterprises.getOrDefault(newEnterprise.getKey(), 0)+(int)Math.round(enterprisesToEstablish));
				
				//Decrease the demand on assets and buildings, since the projects were completed
				homebase.addDemand(newEnterprise.getKey().getAsset(),-1*(int)Math.round(enterprisesToEstablish));
				homebase.addDemand(newEnterprise.getKey().getEnterpriseType(),-1*(int)Math.round(enterprisesToEstablish));
			} else {
				enterprises.put(newEnterprise.getKey(),0);			
				plannedNewEnterprises.remove(newEnterprise.getKey());
			}
		}
	}
	
	public int getProfit() {
		return getSectorRevenue() - getSectorExpenses();
	}
//	public void addPrifitToCapitalPool() {
//		int profitThisYear = getSectorRevenue() - getSectorExpenses();
//		homebase.setCapital(homebase.getCapital()+profitThisYear);
//	}
	
	private double getTotalGroupDemand() {
		double result = 0;
		//Find total demand on all products of the group
		Optional<Double> totDemandOptional = homebase.getDemand().entrySet().stream()
				.filter(a->a.getKey().getSector().equals(sectorType))
				.map(a->a.getValue()*a.getKey().getLaboriousness())
				.filter(a->a>0)
				.reduce(Double::sum);
		//Find total productivity of similar groups 
		Optional <Integer> totalProductivityOpt = homebase.getWorkingGroups().stream()
				.filter(a->a.sectorType.equals(sectorType))
				.map(a->a.getTotalPotentialProductivity())
				.reduce(Integer::sum);
		int totalProductivity = totalProductivityOpt.isPresent() ? totalProductivityOpt.get() : 0;
		double groupShareInMarket = totalProductivity == 0 ? 1 : (double)this.getTotalPotentialProductivity()/totalProductivity;
		result = totDemandOptional.isPresent() ? totDemandOptional.get()*groupShareInMarket : 0;
		//For agriculture, decrease demand by wild game caught
		if (sectorType.equals(SectorType.Agriculture))
			result -= homebase.getIdlePeopleYield();
		return result;
	}

	/*
	 * starts new enterprises' projects with capital, resources and labor pool available. Returns capital unspent
	 */
	private int scheduleNewEnterprisesNextYear(int capitalToInvest) {
		Asset assetToAdd = homebase.getBestAsset();
		EnterpriseType enterpriseType = assetToAdd.getEntepriseType(sectorType); 
		//Check how many new enterprises capital limit allows
		int EnterprisesToEstablishByCapital = (int)(capitalToInvest/assetToAdd.getLaboriousness());
		//Check how many new enterprises demand limit allows
		int EnterprisesToEstablishByDemand = (int)((getTotalGroupDemand() - getTotalPotentialProductivity())/(assetToAdd.getEffectiveness()*assetToAdd.getWorkers()));
		//If the enterprises to establish should have resource input, their amount will be limited by the resources available 
		Optional<Integer> enterprisesAlreadyOnResourceOptional = enterprises.entrySet().stream()
				.filter(a->a.getKey().getEnterpriseType().equals(enterpriseType))
				.map(a->(int)(a.getValue()*a.getKey().getAsset().getEffectiveness()))
				.reduce(Integer::sum);
		int enterprisesAlreadyOnResource = enterprisesAlreadyOnResourceOptional.isPresent() ? enterprisesAlreadyOnResourceOptional.get() : 0;
		int EnterprisesToEstablishByResource = enterpriseType.getResourceInput().size()!=0 ? -1*enterprisesAlreadyOnResource : EnterprisesToEstablishByDemand;
		for (IProducible resource: enterpriseType.getResourceInput())
			EnterprisesToEstablishByResource += location.getResources().getOrDefault(resource, 0);
		int EnterprisesToEstablishByLabor = (int)(homebase.getWorkforceIntoSector(sectorType)/assetToAdd.getWorkers());
		//now, check all the limits and schedule the start for enterprises that is within all limits (by capital, labor, demand and resource) 
		int enterprisesToEstablish = EnterprisesToEstablishByDemand;
		//check capital available...
		if (EnterprisesToEstablishByCapital<enterprisesToEstablish)
			enterprisesToEstablish = EnterprisesToEstablishByCapital;
		//check Labor available...
		if (EnterprisesToEstablishByLabor<enterprisesToEstablish)
			enterprisesToEstablish = EnterprisesToEstablishByLabor;
		//check Resources available...
		if (EnterprisesToEstablishByResource<enterprisesToEstablish && enterpriseType.getResourceInput().size()!=0)
			enterprisesToEstablish = EnterprisesToEstablishByResource;
		
		//Add demand for given type of enterprise (for Construction sector)
		homebase.addDemand(enterpriseType, enterprisesToEstablish);
		//Add demand for given type of asset (for light industry/machine building sector)
		homebase.addDemand(assetToAdd, enterprisesToEstablish);
		Enterprise enterpriseToAdd=null;
		for (Enterprise enterprise: enterprises.keySet())
			if (enterprise.getAsset().equals(assetToAdd)&enterprise.getEnterpriseType().equals(enterpriseType))
				enterpriseToAdd = enterprise;
		if (enterpriseToAdd==null)
			enterpriseToAdd = new Enterprise(enterpriseType,assetToAdd);
		plannedNewEnterprises.put(enterpriseToAdd, (double)enterprisesToEstablish);
		//this.enterprises.put(enterprise, this.enterprises.getOrDefault(enterprise, 0)+enterprisesToEstablish);		
		return 0;
	}
	/*
	 * schedules upgrades for the enterprises of a given type with given assets; returns the capital left after the upgrade
	 */
	private int scheduleUpgradesNextYear(Enterprise enterpriseToUpgrade, Asset newAsset, int capitalAvailable) {
		int result = capitalAvailable;
		int numberOfUpgrades = (int)(capitalAvailable/newAsset.getLaboriousness());
		homebase.addDemand(newAsset, numberOfUpgrades);
		plannedUpgrades.put(newAsset, numberOfUpgrades);
		result -= newAsset.getLaboriousness()*numberOfUpgrades; 
		return result;
	}
	/*
	 * Do enterprise upgrades scheduled last year
	 */
	private void upgradeEnterprises() {
		//int result = capitalAvailable;
		for (Map.Entry<Asset, Integer> newAsset : plannedUpgrades.entrySet()) {
			int numberOfUpgrades = newAsset.getValue();
			for (Map.Entry<Enterprise, Integer>  enterprise : enterprises.entrySet()) {		
				if (enterprise.getKey().getAsset().getEffectiveness()>=newAsset.getKey().getEffectiveness())
					//If enterprise already has this or better asset, don't upgrade 
					continue;
				else {
					int numberOfUpgradesThisEnterprise = numberOfUpgrades > enterprise.getValue() ? numberOfUpgrades : enterprise.getValue();
					//decrease amount of enterprises having old asset
					if (numberOfUpgradesThisEnterprise>=enterprise.getValue())
						enterprises.remove(enterprise.getKey());
					else {
						enterprises.put(enterprise.getKey(), enterprise.getValue()-numberOfUpgradesThisEnterprise);					
					}
					Enterprise newEnterprise = new Enterprise(enterprise.getKey().getEnterpriseType(),newAsset.getKey());
					enterprises.put(newEnterprise, enterprises.getOrDefault(newEnterprise, 0)+numberOfUpgradesThisEnterprise);
				}
			}
			plannedUpgrades.remove(newAsset.getKey());
		}
//		result -= newAsset.getLaboriousness()*numberOfUpgrades; 
//		return result;
	}

	public Settlement getHomeBase() {
		return homebase;
	}
	/*
	 * Returns total employees' income for the sector. Depends on assets used and wealth levels of the employees
	 */
	public double getEmployeesIncome() {
		double result = 0;
		//First, add expectations of workers having skills to work on certain assets
		for (Map.Entry<Enterprise, Integer> enterprise : enterprises.entrySet()) {
			result += enterprise.getKey().getAsset().getDefaultSalary()*enterprise.getKey().getAsset().getWorkers() * enterprise.getValue();
		}
		//Now, increase expectations by established wealth stratification in the settlement
		for (Map.Entry<WealthLevel, Integer> wealthLevel : wealthLevels.entrySet()) {
			result += wealthLevel.getKey().getDefaultSalary()*wealthLevel.getValue();
		}
		return result;
	}
	/**
	 * Returns total expenses for all given enterprise type + asset
	 */
	private double getEnterpriseExpenses(Enterprise enterprise) {
		double result = 0;
		//Add operational costs of the asset (current repair, replacements etc.
		result += enterprise.getAsset().getOperationalCosts()*enterprises.getOrDefault(enterprise, 0);

		//Now, add employees' salaries
		result += getEmployeesIncome()*enterprise.getAsset().getWorkers()*enterprises.getOrDefault(enterprise, 0)/this.getWorkersTotal();
		return result;
	}
	
	/**
	 * Returns total headcount for the sector
	 */
	public int getWorkersTotal() {
		int result = 0;
		for (Map.Entry<WealthLevel, Integer> wealthLevel : wealthLevels.entrySet()) {
			result += wealthLevel.getValue();
		}
		return result;
		
	}

    /**
     * Calculate new wealth levels after the end of economical year
     */
    private void updateWealthLevels() {
    	double totalIncome = getEmployeesIncome();
    	for (Map.Entry<WealthLevel, Integer> wl : wealthLevels.entrySet()) {
    		//amount to spend after current expenses are covered
    		if (workerSavings.getOrDefault(wl.getKey(), 0.0)==null)
    			workerSavings.put(wl.getKey(), 0.0);
    		double amountToInvestForThisWL = getWorkersTotal() == 0 ?  0 : workerSavings.getOrDefault(wl.getKey(), 0.0) + (totalIncome*wl.getValue()/getWorkersTotal()) - wl.getKey().getExpenses()*wl.getValue();    		
    		if (homebase.getOwner().getHousesAvailable().contains(wl.getKey().getNextLevel())&&amountToInvestForThisWL>0) {
    			int housesCanBuy = (int)(amountToInvestForThisWL/wl.getKey().getNextLevel().getLaboriousness());
    			int housesBought = housesCanBuy;
    			//Number of houses to buy on next level is limited by number of people on current level 
    			if ( wealthLevels.get(wl.getKey())<housesBought)
    				housesBought = wealthLevels.get(wl.getKey());
    			//Decrease the amount of houses of the current wealth level
    			wealthLevels.put(wl.getKey(), wealthLevels.get(wl.getKey())-housesBought);
    			//Increase the amount of houses for the next level
    			wealthLevels.put(wl.getKey().getNextLevel(), wealthLevels.getOrDefault(wl.getKey().getNextLevel(),0)+housesBought);
    			//Sums spent on properties' upgrades cannot be invested
    			amountToInvestForThisWL -= housesBought*wl.getKey().getNextLevel().getLaboriousness();
    		} 
   			//TO DO: invest into business enterprise!
    		workerSavings.put(wl.getKey(), amountToInvestForThisWL);
    	}
  
    }
	public Hex getLocation() {
		return location;
	}
    
}
