package com.company.Gameplay;

import com.company.Enumerations.*;
import com.company.Interfaces.IProducible;
import com.company.AssetsComparator;
import com.company.HexMap;
import com.company.hexgame;
import javafx.application.Platform;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class to represent a settlement. Keeps a list of workingGroups sent from it,
 * total number of people (including those in working groups) and location.
 */

@SuppressWarnings("serial")
public class Settlement implements Serializable {
    private int people;
    private String name;
    private Hex location;
    private Tribe owner;
    private transient ImageView imageView;
    public boolean reflectedOnMap = false;
    private HashSet <WorkingGroup> workingGroups = new HashSet<>();
    private HashMap <Commodity, Integer> commodities = new HashMap<>();
    private HashMap <IProducible, Double> prices = new HashMap<>();
    private HashMap <Resource, Integer> resources = new HashMap<>();
    private HashMap <Commodity, Integer> naturalTaxLastTurn = new HashMap<>();
    private Asset  defaultTool = Asset.StoneTools;
    private HashSet<Army> armies = new HashSet<>();
    private HashSet<TradeRoute> tradeRoutes = new HashSet<>();
    private HashSet<Hex> workingHexes = new HashSet<>();
    //Equipment in settlement's armory
    private HashMap<Equipment, Integer> equipment = new HashMap<>();

    //Equipment ordered
    private HashMap<Equipment, Integer> equipmentOrders = new HashMap<>();

    //storage for commodities collected as a tax
    private HashMap<Commodity, Integer> stateCommodityStorage = new HashMap<>();

    //Unemployed people by sectors they'd like to join
    private HashMap<SectorType, Integer> laborPool = new HashMap<>();
    
    private HashMap<IProducible, Integer> demand = new HashMap<>();
    
    private int localRulerReputation;
    private int starvationLastTurn;
    private int capital;

    public ImageView getImageView() {
        return imageView;
    }

    @Override
    public String toString() {
        return getName();
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public HashMap<IProducible, Double> getPrices() {
        return prices;
    }

    /**
     * Method to estimate average local wealth, i.e. amount of goods consumed per citizen
     * @return
     */
    public double getLocalWealth() {
        double result = 0;
        for (Commodity commodity : Commodity.values())
            result += commodityPerTurn(commodity, false)*commodity.getLaboriousness();

        return result/people;
    }

    public int getLocalRulerReputation() {
        return localRulerReputation;
    }

//    public void setStarvationLastTurn(int starvationLastTurn) {
//        this.starvationLastTurn.set(starvationLastTurn);
//    }
//
    public int getStarvationLastTurn() {
        return starvationLastTurn;
    }

    //    private void calculateNeedsDemand(){
//        needsDemand.clear();
//        for (Need)
//    }


    public boolean hasTroopsToPatrol() {
    	boolean result = false;
    	for (Army army : armies)
    		if (army.canAddPatrol()) {
    			result = true;
    			break;    			
    		}
    	return result;
    }
    
    public void sendPatrol(Hex hex) {
    	for (Army army : armies)
    		if (army.canAddPatrol()) {
    			army.addPatrol(hex);;
    			break;    			
    		}   	
    }
    public int getFoodReserves() {
    	int foodReserves = 0;
    	for (Map.Entry<Commodity, Integer> commodity: this.commodities.entrySet())
    		if (commodity.getKey().getNeed() == Need.Food)
    			foodReserves += commodity.getValue();
		return foodReserves;
    	
    }

    
    public int getFoodProduced() {
    	int foodProduced = 0;
    	for (Commodity commodity: Commodity.values())
    		if (commodity.getNeed() == Need.Food)
    			foodProduced += commodityPerTurn(commodity, true);
		return foodProduced;
    	
    }
    /**
     * Method to recalculate trade routes at the end of the year
     */
    private void updateTradeRoutes() {
//        for (Tribe tribe : hexgame.getTribes())
//            for (Settlement settlement : tribe.getSettlements())
//                for (Commodity commodity: Commodity.values())
//                    if (this.prices.getOrDefault(commodity,0.0)<settlement.getPrices().getOrDefault(commodity,0.0)) {
//                        //Oversupply = maximum possible product of this commodity in this settlement - amount in demand
//                        Optional <Integer> maxProduct = workingGroups.stream()
//                                .filter(a->a.getProfession()!=null)
//                                .filter(a->a.getProfession().equals(commodity.getProfession()))
//                                .map(WorkingGroup::getProductPerTurn).reduce((a,b)->a+b);
//                        int oversupply = maxProduct.isPresent() ? maxProduct.get()   - commodityPerTurn(commodity, false) : 0;//commodityPerTurnIgnoringResources
//                        //destination need is amount of goods to supply to equal the price between source and destination
//                        int destinationMaxLocalProduction = 0;
//                        int destinationPopulationProducingCommodity = 0;
//                        for (WorkingGroup destWg : settlement.workingGroups)
//                            if (destWg.getProfession()!=null)
//                                if (destWg.getProfession().equals(commodity.getProfession())) {
//                                    destinationMaxLocalProduction += destWg.getProductPerTurn();
//                                    destinationPopulationProducingCommodity += destWg.getPeople();
//                                }
//                        //This formula is derived from price formula: price = def_price - (1-demand coefficient)/(default price - min price) ->
//                        //-> balance demand = max prod (destination) *  (1 - (def price - price)/(def price - min price))
//                        int destinationUnsatisfiedNeed = (int)(destinationMaxLocalProduction*(1-(commodity.getLaboriousness()-this.prices.getOrDefault(commodity,0.0))/(commodity.getLaboriousness() - commodity.getLaboriousness()*destinationPopulationProducingCommodity/destinationMaxLocalProduction))-settlement.commodityPerTurn(commodity, false));
//                        int volume = oversupply<destinationUnsatisfiedNeed ? oversupply : destinationUnsatisfiedNeed;
//                        Optional <TradeRoute> existingTradeRoute = tradeRoutes.stream()
//                                .filter(a->a.getDestination().equals(settlement))
//                                .filter(a->a.getHomeBase().equals(this))
//                                .filter(a->a.getGoods().equals(commodity))
//                                .findFirst();
//                        if (volume>0 && !existingTradeRoute.isPresent())
//                            tradeRoutes.add(new TradeRoute(this, settlement, commodity, this.prices.getOrDefault(commodity, 0.0), settlement.getPrices().getOrDefault(commodity, 0.0), volume));
//                    }
    }

    public HashSet<Hex> getWorkingHexes() {
        return workingHexes;
    }

//    public int getFoodLimit() {
//        int result = 0;
//        for (Hex hex : workingHexes)
//            for (Map.Entry<Resource, Integer> resource : hex.getResources().entrySet())
//                if (resource.getKey().getNeed()==Need.Food)
//                    result += resource.getKey().getCommodityYield()*resource.getValue();
//         return result;
//    }

    public int getSettlementTreasury() {
        int result = 0;

        for (Map.Entry<Commodity, Integer> commodityEntry : stateCommodityStorage.entrySet())
            result += commodityEntry.getValue();
        return result;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void spendStateFood (int amount) {
        for (Map.Entry<Commodity, Integer> commodityEntry : stateCommodityStorage.entrySet()) {
            int amountToSpent = amount < commodityEntry.getValue() ? amount : commodityEntry.getValue();
            stateCommodityStorage.put(commodityEntry.getKey(), commodityEntry.getValue()- amountToSpent);
            amount -= amountToSpent;

            if (amount<=0)
                break;
        }

    }
    public void orderEquipment (Equipment equipment, int amount) {
        //add order
        equipmentOrders.put(equipment, amount);
        //pay costs from local treasury
        spendStateFood ((int)(equipment.getLaboriousness()*amount));
    }

    public void addPeople(int newPeople) {
        people += newPeople;
    }

    public  HashSet<Army> getArmies() {
        return armies;
    }

    public HashMap<Equipment, Integer> getEquipmentOrders() {
        return equipmentOrders;
    }

    public HashMap<Commodity, Integer> getNaturalTaxLastTurn() {
        return naturalTaxLastTurn;
    }

    public void setNaturalTaxLastTurn(HashMap<Commodity, Integer> naturalTaxLastTurn) {
        this.naturalTaxLastTurn = naturalTaxLastTurn;
    }

    public HashMap<Commodity, Integer> getStateCommodityStorage() {
        return stateCommodityStorage;
    }

    public void addStateCommodity (Commodity commodity, int amount) {
        stateCommodityStorage.put(commodity, stateCommodityStorage.getOrDefault(commodity,0)+amount);
    }

    public Asset getDefaultTool() {
        return defaultTool;
    }

    public Tribe getOwner() {
        return owner;
    }

    public void addCommodity (Commodity commodity, int amount) {
        int currentAmount = commodities.getOrDefault(commodity,0);
        commodities.put(commodity,currentAmount+amount);
    }

    public HashMap<Commodity, Integer> getCommodities() {
        return commodities;
    }

    public HashMap<Commodity, Integer> getAssets() {
        HashMap<Commodity, Integer> result = new HashMap<>();
        for (Map.Entry<Commodity, Integer> asset : commodities.entrySet())
            if (!asset.getKey().isConsumable())
                result.put(asset.getKey(),asset.getValue());

        return result;
    }



    public String getName() {
        return name;
    }
    public int getTotalPeople() {
        return people;
    }

    public HashSet<WorkingGroup> getWorkingGroups() {
        return workingGroups;
    }

    public Hex getLocation() {
        return location;
    }

    public HashMap<Equipment, Integer> getEquipment() {
        return equipment;
    }

    /**
     * Method to produce ordered equipment using available people and available materials
     * @param equipment
     */
//    private void produceEquipment(Equipment equipment){
//        int result = 0;
//        int peopleAvailableFor = getIdlePeople()/equipment.getLaboriousness();
//        int materialsAvailableFor = getIdlePeople()/equipment.getLaboriousness();
//        for (Map.Entry<Commodity, Integer> material : equipment.getMaterials().entrySet())
//            if (commodities.getOrDefault(material.getKey(),0)<materialsAvailableFor*material.getValue())
//                materialsAvailableFor = commodities.getOrDefault(material,0)/material.getValue();
//
//        this.equipment.put(equipment,materialsAvailableFor);
//
//
//    }


    /**
     * Method to produce laborous commodities (houses, tools etc.) using available people and available materials
     * @param commodity
     */
    private void produceCommodity(Commodity commodity){
        int result = commodityPerTurn(commodity, false);
        addCommodity(commodity, result);
        //write off the resources
        if (commodity.getResource()!=null)
            resources.put(commodity.getResource(),resources.getOrDefault(commodity.getResource(),0)-(int)Math.round((double)(result/commodity.getResource().getCommodityYield())));

    }

    public int getIdlePeople() {
        int result = people;
        for (WorkingGroup workingGroup : workingGroups
             ) {
            result -= workingGroup.getPeople();

        }
        return result;
    }

    public HashMap<Resource, Integer> getResources() {
        return resources;
    }

    /**
     * This method used to calculate commodities yield for needs having more than 1 commodity, - e.g. food
     * It assumes that no resources needed, and that demand is unlimited
     * @param commodities
     * @return
     */
//    protected int commodityPerTurn(HashSet<Commodity> commodities){
//        int result = 0;
//        for (Commodity commodity:commodities) {
//            result += workingGroups.stream()
//                    .filter(a->!(a.getProfession()==null))
////                    .filter(a->!(a.getProfession().getCommodities().size()==0))
//                    .filter(a->a.getProfession().getCommodities().contains(commodity))
//                    .map(WorkingGroup::getProductPerTurn)
//                    .reduce(0,Integer::sum)
//            ;
//        }
//        return result;
//    }

    /**
     * returns average tool effectiveness for the settlement (productiveness per person)
     * @return
     */
//    public double toolEffectiveness () {
//        double result = 0;
//        int nonDefaultTools = 0;
//        int peopleToDistribute = people;
//        int toolsInUse = 0;
//        //to do: sort tools by efficiency in descending order
//        for (Map.Entry<Commodity, Integer> tool: commodities.entrySet())
//            if (tool.getKey().getNeed().equals(Need.Tools)) {
//            	//If we have less people than tools, use people number to calculate general effectiveness
//            	if (peopleToDistribute>tool.getValue())
//            		toolsInUse = tool.getValue();
//            	else
//            		toolsInUse = peopleToDistribute;
//                result += toolsInUse * tool.getKey().getEffectiveness();
//                peopleToDistribute -= toolsInUse;
//                nonDefaultTools += toolsInUse;
//            }
//        //
//        return (result+defaultTool.getEffectiveness()*(people-nonDefaultTools))/people;
//    }

    public int getEquipmentOrdersLaboriousnessTotal() {
        int result = 0;
        for (Map.Entry<Equipment, Integer> order : equipmentOrders.entrySet())
            result += order.getKey().getLaboriousness()*order.getValue();
        return result;
    }

//    public void calculatePricesAndEmployment() {
//        for (WorkingGroup wg : workingGroups) {
//            //First, calculate business of the current working group
//            double employmentCoefficient = 0;
//            double demandCoefficient = 0;
//
//            //no need to calculate employment for buffer/migrant groups
//            if (wg.getProfession()==null)
//                continue;
//            //no need to calculate employment for food producers
//            if (wg.getNeed().equals(Need.Food))
//                continue;
//            for (IProducible commodity: wg.getProfession().getCommodities()) {
//                //demand coefficient shows the share of theoretical product of the group actually sold. Affects prices
//                demandCoefficient += (double) (commodityPerTurnIgnoringResources(commodity) * commodity.getLaboriousness()) / wg.getProductPerTurn();
//                //Food price = 1 always;
//                if(commodity.getNeed().equals(Need.Food))
//                	employmentCoefficient = demandCoefficient;
//                else
//	                //employment coefficient shows the coverage of basic needs by the group member income. If it is not satisfying, people abandon their occupation
//	                employmentCoefficient += (commodityPerTurn(commodity) * commodity.getLaboriousness() * prices.getOrDefault(commodity,0.0)) / wg.getPeople();
//            }
//
//            //unemployed from working group become idle
//            if (employmentCoefficient<0)
//                employmentCoefficient=0;
//
//            if (employmentCoefficient< hexgame.unemploymentRate) {
//                wg.assignPeople((int)(wg.getPeople()*employmentCoefficient/hexgame.unemploymentRate));
//            }
//
//
//           // wg.setEmploymentCoefficient(employmentCoefficient);
//            //now, set prices for all the commodities that can be produced by this group
//            if (demandCoefficient<0)
//                demandCoefficient = 0;
//            if (demandCoefficient<1)
//            for (IProducible commodity: wg.getProfession().getCommodities()) {
//                double minPrice = commodity.getLaboriousness()*wg.getPeople()/wg.getProductPerTurn();
//                //default price of the commodity = laboriousness; if demand coeff<1, decrease it but no less than minimum price (the one that allows producers to get income = 1, i.e. feed themselves)
//                double consensualPrice = commodity.getLaboriousness() - (1-demandCoefficient)*(commodity.getLaboriousness()-minPrice);
//                prices.put(commodity, consensualPrice);
//            }
//        }
//    }

	/**
	 * Method to distribute capital available by assets that can be produced with a given resources
	 * @param capitalAvailable Capital to distribute
	 * @return list of assets to purchase
	 */
    protected HashMap<Asset, Integer> assetsOnGivenResources() {
    	HashMap<Asset, Integer> result = new HashMap<>();
    	//First, get list of assets already invented by the tribe
    	Asset[] array = Asset.values();//(Asset[]) owner.getAssetsAvailable().toArray();//
    	AssetsComparator c = new AssetsComparator();
    	Arrays.sort(array, c);  
    	//now, check what we can produce with our resources
		int amountOfAssetToProduce = 0; 
    	for (Asset asset: array) {
    		if (owner.getAssetsAvailable().contains(asset)) {
	    		int resourcesAvailable = resourcesAvailable(asset);
    			//...otherwise, by resources
    			amountOfAssetToProduce = resourcesAvailable;	    		
	    		result.put(asset, amountOfAssetToProduce);
    		}
    	}
    		
    	//TreeSet<Asset> assetsAvailableByTechnology = new TreeSet<>()//owner.getAssetsAvailable(),);
    	//assetsAvailableByTechnology.comparator(). new Comparator<Asset>
    	return result;
    }
    

	/**
	 * Method to distribute capital available by assets that can be produced with a given resources
	 * @param capitalAvailable Capital to distribute
	 * @return list of assets to purchase
	 */
    protected HashMap<Asset, Integer> distributeCapitalByAssets(int capitalToInvest) {
    	HashMap<Asset, Integer> result = new HashMap<>();
    	//First, get list of assets already invented by the tribe
    	Asset[] array = Asset.values();//(Asset[]) owner.getAssetsAvailable().toArray();//
    	AssetsComparator c = new AssetsComparator();
    	Arrays.sort(array, c);  
    	//now, check what we can produce
		int amountOfAssetToProduce = 0; 
    	for (Asset asset: array) {
    		if (owner.getAssetsAvailable().contains(asset)) {
	    		int resourcesAvailable = resourcesAvailable(asset);
	    		if (resourcesAvailable==-1||capitalToInvest<(resourcesAvailable*asset.getLaboriousness()))
	    			//if we don't need resources or have more capital than resources, amount of assets limited by capital... 
	    			amountOfAssetToProduce = (int) (capitalToInvest/asset.getLaboriousness());
	    		else
	    			//...otherwise, by resources
	    			amountOfAssetToProduce = resourcesAvailable;
	    		
	    		result.put(asset, amountOfAssetToProduce);
	    		capitalToInvest -= amountOfAssetToProduce*asset.getLaboriousness();
    		}
    	}
    		
    	//TreeSet<Asset> assetsAvailableByTechnology = new TreeSet<>()//owner.getAssetsAvailable(),);
    	//assetsAvailableByTechnology.comparator(). new Comparator<Asset>
    	return result;
    }

    /**
	 * Method to return best asset available by resources and technology
	 * @return
	 */
    protected Asset getBestAsset() {
    	Asset result = Asset.StoneTools;
    	//To do: filter by resources; add assets available for sale by trade partners
    	//iterating through already-invented assets
    	for (Asset asset: owner.getAssetsAvailable()) {
    		//best asset is the one that have maximum  all resources available
    		if (asset.getEffectiveness()>result.getEffectiveness()&&resourcesAvailable(asset)!=0) {
   				result = asset;
    		}
    	}
    	return result;
    		
    }
    /**
     * Method to check resource limitations for a given product 
     * @param product product to check
     * @return Amount of product that can be produced with resources available in the village. If -1, - resources are unlimited/not needed
     */
    protected int resourcesAvailable(IProducible product) {
    	int result = -1;
    	//Recursively check what is minimal material available
		for (Map.Entry<IProducible, Integer> material: product.getMaterials().entrySet()) {
			int productByresourcesPossible = (int)((double)(resourcesAvailable(material.getKey()))/material.getValue());
			if (result==-1 || productByresourcesPossible<result)
				result = productByresourcesPossible;
		}
		//If product is a resource, check how many do we have
		if(product instanceof Resource) {
			result = 0;
			for (Hex hex: workingHexes)
				result += hex.getResources().getOrDefault(product, 0);
		}
			 
    	return result;
    	
    }
    
    private int getDemand(IProducible commodity) {
    	int result = 0;
    	if (commodity.isConsumable())
    		if (commodity.getSector().equals(SectorType.Agriculture))
    			result = people;
    		else if (commodity.equals(Commodity.Goods)) {
    			//For goods demand = sum of all goods' expenses across all wealth levels
    			Optional<Double> optGoodsDemand = getWealthLevels().entrySet().stream().map(a->a.getValue()*(a.getKey().getExpenses())).reduce(Double::sum);
    			if (optGoodsDemand.isPresent())
    				result = optGoodsDemand.get().intValue();
    		} else
    			//To do: decide if other consumables are possible
    			result = people;
    			
    			
    	
    	else {
    		//demand on houses depends on how many houses people can afford this year
    		if (commodity.getClass().getName().contains("WealthLevel")) {
    	        HashSet<WorkingGroup> workingGroupsToIterate = new HashSet<>(workingGroups);
    			Optional<Integer> optResult = workingGroupsToIterate.stream()
    					.filter(a->a.getProfession()!=null)
    					.filter(a->!(a.getProfession().equals(Profession.Builder)))
    					.map(WorkingGroup::getHousesBought)
    					.map(a->a.getOrDefault(commodity, 0))
    					.reduce(Integer::sum)
    					; 
    			if (optResult.isPresent())
    				result = optResult.get();
    		}
    		//demand on army equipment created by government's orders
    		if (commodity.getClass().getName().contains("Equipment")) {
    			result = equipmentOrders.getOrDefault(commodity, 0);
    		}
    	}		
    	return result;
    }
    /**
     * Returns share of a given commodity in a given sector outcome
     * @param sector
     * @param commodity
     * @return share of the commodity
     */
    public double getCommodityShare(SectorType sector, IProducible commodity) {
    	double result = 0;
    	if (!sector.getCommodities().contains(commodity))
    		return result;
    	int totalDemand = 0;
    	for (IProducible localCommodity : sector.getCommodities())
    		totalDemand += getDemand(localCommodity);
    	
    	result = ((double)getDemand(commodity))/totalDemand;
    	return result;
    }
    /**
     * Method returns maximum amount of the commodity can be produced on this day, - checks available resources, labor force and demand
     * @param commodity
     * @return
     */
    public int commodityPerTurn(IProducible commodity, boolean ignoreDemand ){
    	Optional<Integer> optTotalMaxProductivity = this.workingGroups.stream()
    			.filter(a->a.getSectorType()!=null)
    			.filter(a->a.getSectorType().equals(commodity.getSector()))
    			.map(a->a.getPotentialProductivity(commodity))
    			.reduce(Integer::sum);
    	int labourAvailable = 0;
        if (optTotalMaxProductivity.isPresent())
        	labourAvailable = optTotalMaxProductivity.get();
        //Consider distribution of labour between different commodities for this working group
        labourAvailable = (int)(labourAvailable*getCommodityShare(commodity.getSector(),commodity)); 

        int resourceAvailable = labourAvailable;
        //Check the resources available 
    	for (Map.Entry<IProducible, Integer> material : commodity.getMaterials().entrySet())
    		if (commodityPerTurn(material.getKey(),true)<resourceAvailable)
    			resourceAvailable = commodityPerTurn(material.getKey(),true);
    	//If we are not checking demand, return the smallest from labour available and resource available
    	if (ignoreDemand)
    		return resourceAvailable;

        
        int demand = getDemand(commodity);
        //consumable commodities (e.g. food) must have >=0 balance per turn;
        // for non-consumable (e.g. houses) we need to consider amount created before, - it decreases the demand
        if (!commodity.isConsumable())
            demand -= commodities.getOrDefault(commodity,0);
        //looking for smallest from materials, labour force and demand
        int result = labourAvailable<resourceAvailable ? labourAvailable : resourceAvailable;
        result = demand < result ? demand : result;
        return result;
    }
    /**
     * Method returns how much of the commodity can be produced on this day, - checks available resources, labor force and demand
     * Considers labor pool available
     * @param commodity
     * @return
     */
//    public int commodityPerTurn(IProducible commodity){
//    	int result = maximalCommodityPerTurn(commodity);
//        HashSet<WorkingGroup> workingGroupsToIterate = new HashSet<>(workingGroups);
//    	double workload = workingGroupsToIterate.stream()
//                .filter(a->!(a.getProfession()==null))
//                .filter(a->a.getProfession().getCommodities().contains(commodity))
//                .map(WorkingGroup::getWorkloadCoefficient)
//                .reduce(0.0,Double::sum);
//    	
//    	long WGCOunt = workingGroupsToIterate.stream()
//                .filter(a->!(a.getProfession()==null))
//                .filter(a->a.getProfession().getCommodities().contains(commodity))
//                .count();
//    	
//    	result = (int)(result*workload/WGCOunt);
//        
//    	return result;
//    }

    //This method is used in prices' calculations
//    public int commodityPerTurnIgnoringResources(IProducible commodity){
//        HashSet<WorkingGroup> workingGroupsToIterate = new HashSet<>(workingGroups);
//        int labourAvailable = workingGroupsToIterate.stream()
//                .filter(a->!(a.getProfession()==null))
//                .filter(a->a.getProfession().getCommodities().contains(commodity))
//                .map(WorkingGroup::getProductPerTurn)
//                .reduce(0,Integer::sum);
//
//        int demand = people;
//        //consumable commodities (e.g. food) must have >=0 balance per turn;
//        // for non-consumable (e.g. houses) we need to consider amount created before, - it decreases the demand
//        if (!commodity.isConsumable())
//            demand -= commodities.getOrDefault(commodity,0);
//        //looking for smallest from labour force and demand
//        int result = demand < labourAvailable ? demand : labourAvailable;
//        return result;
//    }

//    private int resourcePerTurn(Resource resource){
//        int result = workingGroups.stream()
//                .filter(a->!(a.getResource()==null))
//                .filter(a->a.getResource().equals(resource))
//                .map(WorkingGroup::getResourcePerTurn)
//                .reduce(0,Integer::sum);
//        return result;
//    }
    /**
     * This method returns a group of volunteers looking for opportunity to join working group
     * covering a given need. If such a group doesn't exist, it creates it and return it by reference
     * @param need The need which people give a priority to
     * @return
     */
//    public WorkingGroup getBufferGroup(Need need) {
//        Optional <WorkingGroup> bufferGroup = workingGroups.stream()
//                .filter(a->!(a.getNeed()==null))
//                .filter(a->a.getNeed().equals(need))
//                .filter(a->a.getCamp()==a.getHomeBase().getLocation())
//                .findAny();
//        WorkingGroup result;
//        if (!bufferGroup.isPresent()) {
//            result = new WorkingGroup(0, 0, null, need, null, this.location, null, this, false/*, WorkingGroupType.Buffer*/);
//            workingGroups.add(result);
//        } else
//            result =bufferGroup.get();
//        return result;
//    }
    
    /**
     * Method to distribute idle people, - first by vital needs, then non-vital, then recruiting into army if no vacancies
     */
    private void distributeIdlePeople() {
    	
    	//Food has priority, - cover food demand first
    	int uncoveredFoodDemand = people - this.getIdlePeopleYield()-commodityPerTurn(Commodity.Food, true);
    	if (uncoveredFoodDemand>0) {  	
	        HashSet<WorkingGroup> agricultureWorkingGroups =
	                workingGroups.stream()
	                        .filter(a -> a.getSectorType().equals(SectorType.Agriculture))
	                        .collect(Collectors.toCollection(HashSet::new));
	        for (WorkingGroup wg: agricultureWorkingGroups) {
	        	int peopleToAssign = (int) (uncoveredFoodDemand/this.getBestAsset().getEffectiveness()>wg.getVacancies() ? 
	        				wg.getVacancies()
	        				:uncoveredFoodDemand/this.getBestAsset().getEffectiveness());
	        	wg.addPeople(peopleToAssign);
	        	uncoveredFoodDemand -= peopleToAssign*this.getBestAsset().getEffectiveness();
	        }
	        	
    	}
    	int peopleForcedToHunt = (int) ((uncoveredFoodDemand + this.getIdlePeopleYield() > this.getLocalWildGame() ? this.getLocalWildGame() : uncoveredFoodDemand + this.getIdlePeopleYield())/this.defaultTool.getEffectiveness()); 
    	int peopleToDistribute = getIdlePeople()  > peopleForcedToHunt ? getIdlePeople() - peopleForcedToHunt : 0;
    	int vacanciesTotal = 0;
    	for (WorkingGroup wg: workingGroups) {
    		vacanciesTotal += wg.getVacancies();
    		
    	}
    	
    	//calculate what percentage of vacancies is covered by job seekers this year
    	double coeffPeopleAvailable  = peopleToDistribute>=vacanciesTotal ? 1 : ((double)peopleToDistribute/vacanciesTotal);
      	for (WorkingGroup wg: workingGroups) {
      		wg.addPeople((int)(coeffPeopleAvailable* wg.getVacancies()));
      	}

        //people that still don't have a job join an army, if any recruiting
        HashSet<Army> armiesToIterate = new HashSet<>(armies);
        for (Army army : armiesToIterate)
            if (army.getLocation().equals(this.location)&& army.getPeople()<army.getPeopleMax()) {
                //army recruiting if it is at home base and needs people
                int peopleAvailable = getIdlePeople();
                int peopleNeeded = army.getPeopleMax() - army.getPeople();
                int newRecruits = peopleAvailable>peopleNeeded ? peopleNeeded : peopleAvailable;
                for (Map.Entry<Equipment, Integer> equipmentToAssign : army.getEquipment().entrySet()) {
                    //Assign all equipment currently in the army
                    int equipmentAlreadyInUse = army.getEquipmentInUse().getOrDefault(equipmentToAssign.getKey(),0);
                    int equipmentNotInUse = equipmentToAssign.getValue() - equipmentAlreadyInUse;
                    int peopleNeededOnEquipment = equipmentNotInUse*equipmentToAssign.getKey().getPeopleToOperate();
                    int equipmentToUse = (peopleNeededOnEquipment<newRecruits ? peopleNeededOnEquipment : newRecruits)/equipmentToAssign.getKey().getPeopleToOperate();
                    army.getEquipmentInUse().put(equipmentToAssign.getKey(), equipmentAlreadyInUse+equipmentToUse);
                    army.addPeople(equipmentToUse*equipmentToAssign.getKey().getPeopleToOperate());
                    this.people -= equipmentToUse*equipmentToAssign.getKey().getPeopleToOperate();
                    newRecruits -= equipmentToUse*equipmentToAssign.getKey().getPeopleToOperate();
                    if (newRecruits <=0)
                        break;
                }
            }

      	
//        //first, check the satisfaction of the vital needs
//    	
//    	//add people from all buffer group into pool to distribute
//    	for (WorkingGroup wg: workingGroups) {
//    		if(wg.getType().equals(WorkingGroupType.Buffer))
//    			wg.assignPeople(0);
//    	}
//        int peopleToDistribute = getIdlePeople();
//
//        int peopleToSatisfyVitalNeeds = 0;
//
//        HashMap<Need, Integer> unsatisfiedVitalNeeds = new HashMap<>();
//        for (Need need: Need.values())
//            //Get list of non-satisfied vital needs
//            if (need.isVital()) {
//                //HashSet<Commodity> commoditiesForNeed = new HashSet<>();
//                int commoditiesForNeedPerTurn = 0;//commodityPerTurn(commoditiesForNeed);
//                for (Commodity c : Commodity.values())
//                    if (c.getNeed().equals(need))
//                    	commoditiesForNeedPerTurn += commodityPerTurn(c);
//                        //commoditiesForNeed.add(c);
//
//                WorkingGroup bufferForNeed = getBufferGroup(need);
//                if (people-bufferForNeed.getPeople()>=commoditiesForNeedPerTurn) {
//                //if we consume more than produce, add the need to the list and increase the amount of people needed to cover vital needs
//                    peopleToSatisfyVitalNeeds += people - commoditiesForNeedPerTurn - bufferForNeed.getPeople();
//                    unsatisfiedVitalNeeds.put(need, people - bufferForNeed.getPeople() - commoditiesForNeedPerTurn);
//                }
//            }
//
//        //calculate the percent of needed vital jobs that can be filled with idle people
//        double coefficientOfVitalJobsOccupation = peopleToSatisfyVitalNeeds==0 ? 1 : (double)((peopleToDistribute>peopleToSatisfyVitalNeeds)? peopleToSatisfyVitalNeeds : peopleToDistribute)/peopleToSatisfyVitalNeeds;
//
//        for (Map.Entry<Need, Integer> need: unsatisfiedVitalNeeds.entrySet()) {
//            //Get list of non-full groups for a given need
//
//            WorkingGroup bufferForNeed = getBufferGroup(need.getKey());
//            //int peopleToAssign = peopleToDistribute + bufferForNeed.getPeople();
//           // int peopleAlreadyInBuffer = (bufferForNeed == null) ? 0 : bufferForNeed.getPeople();
//            //bufferForNeed.assignPeople(0);
//            
//
//            //number of people needed to satisfy need, and not yet in buffer group for the need.
//            // Note that we are making number of people needed equal to number of commodity in demand, - since we don't know the yield per person
//            int peopleToSatisfyNeed = (int) (coefficientOfVitalJobsOccupation * need.getValue());
//            //getting a list of working groups that can satisfy current need and recruiting; joining them if any
//            HashSet<WorkingGroup> relatedWorkingGroups = workingGroups.stream()
//                    .filter(a -> a.getPeople() < a.getPeopleMax())
//                    .filter(a -> a.getLocation().equals(location)||a.getType().equals(WorkingGroupType.Settled))
//                    .filter(a -> a.getProfession().getNeeds().contains(need.getKey()))
//                    .collect(Collectors.toCollection(HashSet<WorkingGroup>::new));
//            for (WorkingGroup wg : relatedWorkingGroups) {
//                //if the group is in the settlement, join it
//                int peopleToAdd = (wg.getPeopleMax() - wg.getPeople() > peopleToSatisfyNeed) ? peopleToSatisfyNeed : wg.getPeopleMax() - wg.getPeople();
////                if (peopleToAdd>peopleToAssign)
////                	//if we need to add more people than we have youth, borrow from buffer group
////                	bufferForNeed.addPeople(bufferForNeed.getPeople()>-1*(peopleToDistribute - peopleToAdd)?peopleToDistribute - peopleToAdd:bufferForNeed.getPeople());
//                wg.addPeople(peopleToAdd);
//                peopleToSatisfyNeed -= peopleToAdd;
//                if (peopleToSatisfyNeed <= 0)
//                    break;
//            }
//
//            //if there are migration destinations with vital needs satisfied, migrate there
//            for (Settlement destinationSettlement: owner.getSettlements())
//                if (Need.Food.satisfied(destinationSettlement)>=Need.Food.satisfied(this) &&!destinationSettlement.equals(this)) {
//                    int potentialMigrants = destinationSettlement.getFoodLimit() - destinationSettlement.getTotalPeople();
//                    if (potentialMigrants>=peopleToSatisfyNeed)
//                        potentialMigrants=peopleToSatisfyNeed;
//                    WorkingGroup migrants = new WorkingGroup(potentialMigrants, potentialMigrants, null, Need.Food, null, destinationSettlement.getLocation(), null, this, false, WorkingGroupType.Migrant);
//                    peopleToSatisfyNeed -= potentialMigrants;
//                    workingGroups.add(migrants);
//                }
//            //if there are non-distributed people to cover this need, put them into a special volunteers group. First group recruiting will take them aboard
//            if (peopleToSatisfyNeed > 0) {
//                    bufferForNeed.addPeople(peopleToSatisfyNeed);
//            }
//        }
//
//        //Lets check if traders' career brings more wealth than local job
//        if (owner.getProfessionsAvailable().contains(Profession.Trader))
//            for (TradeRoute tradeRoute: tradeRoutes) {
//                if (tradeRoute.profit(Transport.Backpack)>getLocalWealth()) {
//                    //Check if beginner trader group already exists
//                    Optional <WorkingGroup> noobTradersOptional = workingGroups.stream().filter(a->a.getMainAsset().equals(Transport.Backpack)).findFirst();
//                    WorkingGroup noobTraders;
//                    if (noobTradersOptional.isPresent())
//                        noobTraders = noobTradersOptional.get();
//                    else {
//                        noobTraders = new WorkingGroup(0, 0, null, null, Profession.Trader, tradeRoute.getDestination().getLocation(), Transport.Backpack, this, false, WorkingGroupType.Nomadic);
//                        workingGroups.add(noobTraders);
//                    }
//                }
//            }
//
//
//        //if we have idlers after satisfying of vital needs, distribute them by non-vital needs
//        if (peopleToDistribute - peopleToSatisfyVitalNeeds>0) {
//            int nonDistributedPeople = peopleToDistribute - peopleToSatisfyVitalNeeds;
//            HashMap<IProducible,Integer> commoditiesInDemand = new HashMap<>();
////            int totalCommoditiesToProduce = 0;
//            int peopleToProduceNonvitalCommodities = 0;
//            for (Commodity commodity : Commodity.values()) {
////                int commodityInStore = commodities.getOrDefault(commodity,0);
//                if (commodity.equals(defaultTool))
//                    continue;
//              if (!commodity.getNeed().isVital()) {
//                    int commodityToProduce =  getDemand(commodity) - commodityPerTurn(commodity);
//                    		// people - commodities.getOrDefault(commodity,0) - commodityPerTurn(commodity);
//                    if (commodity.getResource()!=null) {
//                        int resourceAvailable = (resources.getOrDefault(commodity.getResource(), 0) + resourcePerTurn(commodity.getResource())) * commodity.getResource().getCommodityYield();
//                        //If we have less resources than we need, consider resource amount as a limit to planned production; also, add resource needed into list of competing demands
//                        if (resourceAvailable < commodityToProduce) {
//                            int resourceToGather = commodityToProduce - resourceAvailable;
//                            commodityToProduce = resourceAvailable;
//                            //Check if resources available in working hexes; if not, their collection is not among the jobs available 
//                            Optional<Integer> resourceCanGatherOptional = workingHexes.stream()
//                            		.map(a->a.getResources().getOrDefault(commodity.getResource(),0))
//                            		.reduce(Integer::sum);
//                            int resourceCanGather = resourceCanGatherOptional.isPresent() ? resourceCanGatherOptional.get() : 0;
//                            //if we need more resources that are available, use the available amount for demand
//                            if (resourceCanGather<resourceToGather)
//                            	resourceToGather = resourceCanGather;
//                            commoditiesInDemand.put(commodity.getResource(),resourceToGather);
//                        }
//                    }
//                    if (commodityToProduce>0) {
// //                       totalCommoditiesToProduce += commodityToProduce;
//                        peopleToProduceNonvitalCommodities += commodityToProduce * commodity.getLaboriousness();
//                        commoditiesInDemand.put(commodity, (int)(commodityToProduce * commodity.getLaboriousness()));//peopleToProduceNonvitalCommodities);
//                    }
//                }
//            }
//            //state equipment orders compete with other non-vital demands
//            for (Map.Entry<Equipment, Integer> equipmentOrderedEntry : equipmentOrders.entrySet())
//                commoditiesInDemand.put(equipmentOrderedEntry.getKey(),(int)(equipmentOrderedEntry.getValue()*equipmentOrderedEntry.getKey().getLaboriousness()));
//
//
//            //demand on properties compete with other non-vital demands
//            for (WealthLevel wl : WealthLevel.values())
//            	if (getDemand(wl)>0)
//            		commoditiesInDemand.put(wl,(int)(getDemand(wl)*wl.getLaboriousness()));
//            
//            //calculate the percent of needed non-vital jobs that can be filled with idle people
////            double coefficientOfNonVitalJobsOccupation = peopleToProduceNonvitalCommodities==0 ? 1 : ((nonDistributedPeople>peopleToProduceNonvitalCommodities)? peopleToProduceNonvitalCommodities : nonDistributedPeople)/peopleToProduceNonvitalCommodities;
//            //For each non-vital (i.e. non-consumable) commodity increase number of non-vital producers
//        	int totalUncoveredDemand = 0;
//        	Optional<Integer> optionalUncoveredDemand =  commoditiesInDemand.entrySet().stream()
//        		//default tools doesn't create demand 
//        		.filter(a->!a.getKey().equals(defaultTool))
//        		.map(a->a.getValue())
//        		.reduce(Integer::sum);
//        	if (optionalUncoveredDemand.isPresent())
//        		totalUncoveredDemand = optionalUncoveredDemand.get();
//
//        	if (peopleToProduceNonvitalCommodities>0 && totalUncoveredDemand>0) {
//            	
//                for (Map.Entry<IProducible, Integer> commodity : commoditiesInDemand.entrySet()) {
//                    //default tool is provided for free and doesn't create a demand
//                    if (commodity.getKey().equals(defaultTool))
//                        continue;
//
//                    //villager cannot select profession that is not opened yet 
//                    if (!owner.getProfessionsAvailable().contains(commodity.getKey().getProfession()))
//                        continue;
//
//                    double commodityShare = (double)commodity.getValue()/totalUncoveredDemand;
//                   //check if the group for the commodity already exist; if yes, join it, if not, create one
//                    Optional <WorkingGroup> commodityGroupOptional = workingGroups.stream()
//                            .filter(a->!(a.getProfession()==null))
//                            .filter(a->a.getProfession().getCommodities().contains(commodity.getKey()))
//                            .findFirst();
//                    int peopleToAdd = (int) (commodity.getValue()/toolEffectiveness()>commodityShare*nonDistributedPeople ?
//                    		commodityShare*nonDistributedPeople : commodity.getValue()/toolEffectiveness());
//                    		
//                    		//(int)(commodityShare*nonDistributedPeople*commodity.getValue()/peopleToProduceNonvitalCommodities);
//                    WorkingGroup commodityGroup;
//                    if (commodityGroupOptional.isPresent()) {
//                        commodityGroup = commodityGroupOptional.get();
//                        if (commodityGroup.getPeopleMax()-commodityGroup.getPeople()<peopleToAdd&&commodity.getKey().getClass().getName().contains("Resource"))
//                        	peopleToAdd = commodityGroup.getPeopleMax()-commodityGroup.getPeople();
//                        commodityGroup.addPeople(peopleToAdd);
//                    } else {
//                        //For non-resource producibles, create new groups
//                        if (!commodity.getKey().getClass().getName().contains("Resource")) {
//                            commodityGroup = new WorkingGroup(0
//                                    , 0
//                                    , commodity.getKey().getResource()//== null ? null : commodity.getKey()/*.getProfession()*/.getResource()
//                                    , commodity.getKey().getNeed()
//                                    , commodity.getKey().getProfession()
//                                    , this.location
//                                    , defaultTool
//                                    , this
//                                    , false
//                                    ,WorkingGroupType.Settled
//                            );
//                            workingGroups.add(commodityGroup);
//                            commodityGroup.addPeople(peopleToAdd);
//                         };
//                    }
//                }
//            }
//        }
//
    }
    

    public void abandonWorkingHex(Hex hex){
        workingHexes.remove(hex);
        HashSet<WorkingGroup> currentWorkingGroups = new HashSet<>(workingGroups);
        for (WorkingGroup wg: currentWorkingGroups) {
            if (wg.getCamp().equals(hex))
                wg.disband();
        }
        for (Army army: armies)
        	army.removePatrol(hex);
    }
    /**
     * called by timer thread; disbands working group that run out of resources to collect
     */
    public void disbandIdleWorkingGroups () {
        HashSet<WorkingGroup> workingGroupsToCheck = new HashSet<>(workingGroups);
        for (WorkingGroup wg : workingGroupsToCheck)
            if (wg.isResourceDepleted()) {
                wg.getCamp().getWorkingGroupsCamps().remove(wg);
                location.getWorkingGroups().remove(wg);
                workingGroups.remove(wg);
                wg = null;
            }
    }
    /**
     * Return number of people on different wealth levels in the settlement 
     * @return
     */
    public HashMap<WealthLevel, Integer> getWealthLevels() {
    	HashMap<WealthLevel, Integer> result = new HashMap<>();
    	for (WorkingGroup wg : getWorkingGroups()) 
    		for (Map.Entry<WealthLevel, Integer> wl : wg.getWealthLevels().entrySet())
    			result.put(wl.getKey(), result.getOrDefault(wl.getKey(), 0)+wl.getValue());
    	
    	result.put(WealthLevel.Homeless, result.getOrDefault(WealthLevel.Homeless, 0)+getIdlePeople());
    	return result;
    }
     

    /**
     * method to turn a given hex into working zone of the settlement. Creates working groups for all available resources, that starts recruiting
     * @param location
     * @param peopleToSend
     * @param peopleMax
     */

    public void sendExpedition(Hex location, int peopleToSend, int peopleMax) {
        boolean aggressiveness = false;
        workingHexes.add(location);
        HashSet<SectorType> groupsCreated = new HashSet<>();
        for (Map.Entry<Resource, Integer> resource : location.getResources().entrySet()) {
            int peopleResult = peopleToSend;
            
            sendPatrol(location);
            int peopleMaxResult = resource.getValue();
            Need need;
            try {
//                need = resource.getKey().getCommodity().getNeed();
            } catch (NullPointerException e)
            { need = null;}
            if (!groupsCreated.contains(resource.getKey().getSector())) {
            	//if sector was not added yet, add it
	            WorkingGroup wg = new WorkingGroup(peopleResult, peopleMaxResult, resource.getKey(), /*need,*/ resource.getKey().getSector(), location, defaultTool, this, aggressiveness/*, resource.getKey().getProfession().getWorkingGroupType()*/);
	            workingGroups.add(wg);
	            groupsCreated.add(resource.getKey().getSector());
	            this.location.addWorkingGroup(wg);
	            location.addWorkingGroupCamp(wg);
	            Platform.runLater(()->HexMap.updateMapPanel());
            } else {
            	//If group already exists, add maximum capacity
            	Optional<WorkingGroup> wgOpt = workingGroups.stream()
            			.filter(a->a.getSectorType().equals(resource.getKey().getSector()))
            			.filter(a->a.getLocation().equals(location))
            			.findFirst();
            	if (wgOpt.isPresent())	{
            		WorkingGroup wg = wgOpt.get();
                	//wg.setPeopleMax(wg.getPeopleMax()+peopleMaxResult);
            	} else {
            		String a =  "we shoouldn't be here!!!";
            	}
            	
            		
            	
            };
            try {
                hexgame.logFile.write("Player " + this.owner.getName() + " set new sector, headcount " + peopleResult + " " + resource.getKey().getSector().getName() + "s to (" + location.getX() + ", " + location.getY() + ")");
                hexgame.logFile.newLine();
            } catch (Exception e){
                System.out.print("oops");
            }

        }

   }

    /**
     * returns number of newborns for a given timer tick
     * @return
     */
    private int getNewborns() {
        int result = 0;
//        int idlePeople = this.getIdlePeople();
//        int localFoodLimit = location.getResources().get(Resource.wildGame);
//        if (idlePeople<=localFoodLimit)
//            result = (int)(idlePeople*0.1);
        result = (int)(people*hexgame.BIRTHRATE+1);
//        else
//            result = localFoodLimit;
        return result;
    }

    /**
     * Method to consume commodities for needs related in a given time tick.
     *
     */
    private void consumeCommodities() {
        //First, iterate via the list of needs
        for (Need need : Need.values()) {
            int totalNeed = this.people;
            if (need.consumesCommodities()) {
                //if need requires to consume commodities, iterate via the list of suitable commodities
                Map<Commodity, Integer> commoditiesToIterate = new HashMap<>(commodities);
                for (Map.Entry<Commodity, Integer> commodity : commoditiesToIterate.entrySet())
                    if (commodity.getKey().isConsumable() && commodity.getKey().getNeed().equals(need)) {
                        if (totalNeed<commodity.getValue()) {
                            commodities.put(commodity.getKey(), commodity.getValue() - totalNeed);
                            totalNeed = 0;
                        }
                        else {
                            totalNeed -= commodity.getValue();
                            commodities.remove(commodity.getKey());
                        }
                    }
            }
        }
    }

    private int getLocalWildGame() {
        int wildGame = 0;
    	for (Hex hex : this.workingHexes) {
    		Optional<Integer> wildGameOpt = hex.getResources().entrySet().stream()
    				.filter(a->a.getKey().equals(Resource.wildGame))
    				.map(a->a.getValue())
    				.reduce(Integer::sum);
    		wildGame += wildGameOpt.isPresent() ? wildGameOpt.get() : 0;
    	}
    	return wildGame;
    }
    /**
     * Method to add food collected by idle people on the village hex
     */
    protected int getIdlePeopleYield() {
        int peopleAvailable = this.getIdlePeople();
        if (peopleAvailable<=0)
            return 0;
        else {
        	//To do: add criminals' income
            int wildGame = getLocalWildGame();
        	int result = wildGame<= peopleAvailable*defaultTool.getEffectiveness() ?  wildGame : (int)(peopleAvailable*defaultTool.getEffectiveness());
        	return result;
        }
//        HashMap<Resource,Integer> resourcesLocal = new HashMap<>();
//        //create a list of food resources on the village hex
//        for (Map.Entry <Resource,Integer> resource: location.getResources().entrySet())
//            if (resource.getKey().getCommodity().getNeed().equals( Need.Food))
//                resourcesLocal.put(resource.getKey(),resource.getKey().getCommodityYield());
//
//
//
//        //  Convert Map to List of Map to do sorting
//        List<Map.Entry<Resource, Integer>> list =
//                new LinkedList<Map.Entry<Resource, Integer>>(resourcesLocal.entrySet());
//
//
//        // Sort list with Collections.sort(), provide a custom Comparator
//        //    Can switch the o1 o2 position for a different order
//        Collections.sort(list, new Comparator<Map.Entry<Resource, Integer>>() {
//            public int compare(Map.Entry<Resource, Integer> o1,
//                               Map.Entry<Resource, Integer> o2) {
//                return (o1.getValue()).compareTo(o2.getValue());
//            }
//        });
//
//        //Now iterate through sorted list, - from most efficient food resources to least efficient
//        for (Map.Entry<Resource, Integer> resourceToCollect : list) {
//            int peopleOnResource;
//            int resourceAvailable = this.location.getResources().getOrDefault(resourceToCollect.getKey(),0);
//            if (peopleAvailable<this.location.getResources().getOrDefault(resourceToCollect.getKey(),0))
//                peopleOnResource = peopleAvailable;
//            else
//                peopleOnResource = resourceAvailable;
//
//            addCommodity(resourceToCollect.getKey().getCommodity(), peopleOnResource*resourceToCollect.getKey().getCommodityYield());
//        }
    }
    /**
     * Method to calculate number of people starved last year
     */
    private void doStarvations() {
        int totalStarvation =  (int)((people * (1-((double)(commodityPerTurn(Commodity.Food, true)+getIdlePeopleYield()))/people)) * hexgame.STARVATIONRATE);
        
        if (people<totalStarvation)
            totalStarvation = people;

        //if no starvation happened, no need to distribute it by social groups
        if (totalStarvation<=0)
            return;

        //Unemployed starving first
        int starvationToDistribute = totalStarvation > getIdlePeople() ? totalStarvation - getIdlePeople() : 0;


        //then starve those not in agriculture 
        Optional<Integer> i =  workingGroups.stream().filter(a->!a.getSectorType().equals(SectorType.Agriculture)).map(a->a.getPeople()).reduce((a,b)->a+b);
        int peopleNotAgricultureTotal = i.isPresent() ? i.get() : 0;
        double starvationCoefficientPeopleNotinAgriculture;
        if (starvationToDistribute>=peopleNotAgricultureTotal)
        	starvationCoefficientPeopleNotinAgriculture = 1;
        else
        	starvationCoefficientPeopleNotinAgriculture = (double) starvationToDistribute/peopleNotAgricultureTotal;

        for (WorkingGroup workingGroup : workingGroups)
            if (!workingGroup.getSectorType().equals(SectorType.Agriculture)) {
                //workingGroup.assignPeople(workingGroup.getPeople() - (int)(workingGroup.getPeople()*starvationCoefficientPeopleLookingForjobs));
            	workingGroup.distributeStarvation((int)(workingGroup.getPeople()*starvationCoefficientPeopleNotinAgriculture));
                starvationToDistribute -= (int)(workingGroup.getPeople()*starvationCoefficientPeopleNotinAgriculture);
            }

//        //Now starving those on non-vital jobs
//        if (starvationToDistribute>0) {
//            HashSet<WorkingGroup> nonVitalWorkingGroups =
//                    workingGroups.stream()
//                            .filter(a -> a.getProfession() != null)
//                            .filter(a -> !a.getNeed().isVital())
//                            .collect(Collectors.toCollection(HashSet::new));
//            i = nonVitalWorkingGroups.stream().map(a -> a.getPeople()).reduce((a, b) -> a + b);
//            int peopleNonVitalJobsTotal = i.isPresent() ? i.get() : 0;
//            double starvationCoefficientPeopleNonVitalJobs;
//            if (starvationToDistribute >= peopleLookingForJobsTotal)
//                starvationCoefficientPeopleNonVitalJobs = 1;
//            else
//                starvationCoefficientPeopleNonVitalJobs = (double) starvationToDistribute / peopleNonVitalJobsTotal;
//
//            for (WorkingGroup workingGroup : nonVitalWorkingGroups) {
//                //workingGroup.assignPeople(workingGroup.getPeople() - (int) (workingGroup.getPeople() * starvationCoefficientPeopleNonVitalJobs));
//            	workingGroup.distributeStarvation((int)(workingGroup.getPeople()*starvationCoefficientPeopleNonVitalJobs));
//                starvationToDistribute -= (int) (workingGroup.getPeople() * starvationCoefficientPeopleNonVitalJobs);
//
//            }
//        }

        //Now starving those in agriculture
        if (starvationToDistribute>0) {
            HashSet<WorkingGroup> VitalWorkingGroups =
                    workingGroups.stream()
                            .filter(a -> a.getSectorType().equals(SectorType.Agriculture))
                            .collect(Collectors.toCollection(HashSet::new));
            i = VitalWorkingGroups.stream().map(a -> a.getPeople()).reduce((a, b) -> a + b);
            int peopleVitalJobsTotal =  i.isPresent() ? i.get() : 0;
            double starvationCoefficientPeopleVitalJobs;
            if (starvationToDistribute >= peopleVitalJobsTotal)
                starvationCoefficientPeopleVitalJobs = 1;
            else
                starvationCoefficientPeopleVitalJobs = (double) starvationToDistribute / peopleVitalJobsTotal;

            for (WorkingGroup workingGroup : VitalWorkingGroups) {
//                workingGroup.assignPeople(workingGroup.getPeople() - (int) (workingGroup.getPeople() * starvationCoefficientPeopleVitalJobs));
                if (workingGroup.getSectorType().equals(SectorType.Agriculture)) {
	            	workingGroup.distributeStarvation((int)(workingGroup.getPeople()*starvationCoefficientPeopleVitalJobs));
	                starvationToDistribute -= (int) (workingGroup.getPeople() * starvationCoefficientPeopleVitalJobs);
                }
            }
        }
        people -= totalStarvation;
        starvationLastTurn = totalStarvation;

    }

    private void recalculateLocalRulerReputation() {
        //first, consider starvation happened this turn
        if (starvationLastTurn == 0)
            localRulerReputation = 100;
        else
            localRulerReputation = (int) (100 * (1 - (double)(starvationLastTurn / (people + starvationLastTurn))));

        //add effect of commodities purchased this turn
        for (Commodity commodity : Commodity.values())
            localRulerReputation += (100 * commodityPerTurn(commodity, false) * commodity.getLaboriousness())/(people == 0 ? 1: people);
        //now consider taxation level
        if (owner.getGeneralTaxRate()>hexgame.fairTaxLevel)
            localRulerReputation -= owner.getGeneralTaxRate() - hexgame.fairTaxLevel;
    }
    /**
     * calculates the changes during a year
     */
    public void processDay() {
//        if (people<=0) {
//            ;
//            EndGameScreen endGameScreen = Platform.runLater(()-> {new EndGameScreen();});
//            endGameScreen.show();
//            hexgame.stopGame();
//        }
        addPeople(getNewborns());
        calculateDemand();
        //Get capital accumulated by people with no regular jobs, - hunters, etc.
    	setCapital(capital + getIdlePeopleYield());
        disbandIdleWorkingGroups();
        //First, do calculations that needs to be done before profit calculation, e.g. establish and upgrade enterprises
        HashSet<WorkingGroup> groupsBeforeTheCalculation = new HashSet<>(workingGroups);
        
        for (WorkingGroup wg : groupsBeforeTheCalculation) {
            wg.preProcessYear();
        }
        distributeIdlePeople();
        //now, calculate financial outcomes of all working groups
        groupsBeforeTheCalculation = new HashSet<>(workingGroups);
        for (WorkingGroup wg : groupsBeforeTheCalculation)
        	wg.processDay();
        doStarvations();
        recalculateLocalRulerReputation();
//        consumeCommodities();
//        calculatePricesAndEmployment();
        updateTradeRoutes();
    }
    public Settlement (String name, Hex location, Tribe owner){
        this.owner = owner;
        this.name = name;
        this.location = location;
        this.localRulerReputation = owner.getRulerReputation();
        //If hex contains a village, put the available wild game to minimum
        location.setResource(Resource.wildGame,50);
        this.people = 30;
        this.commodities.put(Commodity.Food, 1000);
        this.workingGroups = new HashSet<WorkingGroup>();
        this.equipment.put(Equipment.StoneAxes, 10);
        this.defaultTool = Asset.StoneTools;
        //this.workingHexes.add(location);//sendExpedition(location, 0, 0);
        this.sendExpedition(location, 0, 0);
        //workingGroups.add(new WorkingGroup(0,0,null, SectorType.Agriculture, location, defaultTool, this, false));
        workingGroups.add(new WorkingGroup(0,0,null, SectorType.LightIndustry, location, defaultTool, this, false));
        workingGroups.add(new WorkingGroup(0,0,null, SectorType.Construction, location, defaultTool, this, false/*, WorkingGroupType.Settled*/));
     }

	public HashMap<SectorType, Integer> getLaborPool() {
		return laborPool;
	}

	public HashMap<IProducible, Integer> getDemand() {
		return demand;
	}

	/*
	 * Sets demand to certain value
	 */
	public void setDemand(IProducible commodity, Integer amount) {
		this.demand.put(commodity, amount);
	}

	/*
	 *  Increase demand by certain value
	 */
	public void addDemand(IProducible commodity, Integer amount) {
		if (commodity!= null)
			this.demand.put(commodity, demand.getOrDefault(commodity, 0)+amount);
	}

	public int getCapital() {
		return capital;
	}
	
	/*
	 * method to calculate demand for this year
	 */
	private void calculateDemand() {
		//Here, we calculate demand for end user products only. Demand for assets, weapons added elsewhere
		for (Commodity commodity: Commodity.values())
			demand.put(commodity, getDemand(commodity));
		
		HashSet<WorkingGroup> workingGroupsCopy = new HashSet<>(workingGroups);
		
		//To do: add demand on assets, - move from WOrkGroup class, to improve traceability
		for  (WorkingGroup wg: workingGroupsCopy) {
			for (Map.Entry<Asset, Integer> asset: wg.getPlannedUpgrades().entrySet())
				addDemand(asset.getKey(),asset.getValue());
			
			for (Map.Entry<Enterprise, Double> asset: wg.getPlannedNewEnterprises().entrySet()) {
				addDemand(asset.getKey().getEnterpriseType(),asset.getValue().intValue());
				addDemand(asset.getKey().getAsset(),asset.getValue().intValue());
			}
		}
	}
	
	/**
	 * 
	 * @param sectorType
	 * @return
	 */
	private int sectorUncoveredDemand(SectorType sectorType) {
		int result = 0;
		Optional<Integer> sectorDemandOpt = demand.entrySet().stream()	
				.filter(a->a.getKey()!=null)
				//we need to consider positive demand only, - otherwise negative demands can hide existing ones 
				.filter(a->a.getValue()>0)
				.filter(a->a.getKey().getSector().equals(sectorType))
				.map(a->a.getValue())
				.reduce(Integer::sum);
		double sectorDemand = sectorDemandOpt.isPresent() ? sectorDemandOpt.get() : 0;
		//Sum all supply, to calculate uncovered demand
		Optional<Integer> sectorSupplyOpt = workingGroups.stream()
				.filter(a->a.getSectorType().equals(sectorType))
				.map(a->a.getTotalPotentialProductivity())
				.reduce(Integer::sum);
		int sectorSupply = sectorSupplyOpt.isPresent() ? sectorSupplyOpt.get() : 0;
		result = (int) (sectorDemand>sectorSupply? (sectorDemand-sectorSupply): 0.0);
		
		return result;
	}
	
	private double sectorDemandShare(SectorType sectorType) {
		double result = 0;
		//Sum of all positive demands, to calculate share for a given sector
//		Optional<Integer> totalDemandOpt = demand.entrySet().stream()
//				.filter(a->a.getValue()>0)
//				.map(a->(int)(a.getValue()*a.getKey().getLaboriousness()))
//				.reduce(Integer::sum);
//		
//		int totalDemand = totalDemandOpt.isPresent() ? totalDemandOpt.get() : 0;
//		
//		
//		//Sum all supply, to calculate uncovered demand
//		Optional<Integer> totalSupplyOpt = workingGroups.stream()
//				.map(a->a.getTotalPotentialProductivity())
//				.reduce(Integer::sum);
//		int totalSupply = totalSupplyOpt.isPresent() ? totalSupplyOpt.get() : 0;
		
		int totalUncoveredDemand = 0;//totalDemand-totalSupply;
		for (SectorType sectorTypeLocal : SectorType.values())
			totalUncoveredDemand += sectorUncoveredDemand(sectorTypeLocal);
		
		double sectorUncoveredDemand = sectorUncoveredDemand(sectorType);
		result = totalUncoveredDemand == 0 ? 0 : sectorUncoveredDemand/totalUncoveredDemand;
		//result = totalDemand == 0 ? 0 : sectorDemand/totalDemand;
		return result;
	}

	/*
	 * Returns workforce willing to go into the sector
	 */
	public int getWorkforceIntoSector(SectorType sectorType) {
		int result = (int)(getIdlePeople()*sectorDemandShare(sectorType));
		return result;
	}
	/*
	 * Returns capital willing to go into the sector
	 */
	public int getInvestmentIntoSector(SectorType sectorType) {
		int result = 0;
		result = (int)(capital*sectorDemandShare(sectorType));
		return result;
	}

	public void setCapital(int capital) {
		this.capital = capital;
	}
}
