package com.company.Gameplay;

import com.company.Enumerations.BranchOfKnowledge;
import com.company.Enumerations.Resource;
import com.company.Enumerations.TerrainType;
import com.company.GUI.HexScreen;
import com.company.HexMap;
import com.company.Interfaces.IAbleToFight;
import com.company.Message;
import com.company.hexgame;
import javafx.scene.image.ImageView;

import java.io.Serializable;
import java.util.*;

/**
 * Hex describes 1 piece of territory. It has a terrain type and list of resources as a properties
 */
public class Hex implements Serializable{
    private TerrainType terrain;
    private HashMap<Resource, Integer> resources;
    private Settlement settlement;
    private HashSet<WorkingGroup> workingGroups = new HashSet<>();
    private HashSet<WorkingGroup> workingGroupsCamps = new HashSet<>();
    private HashSet<Army> armies = new HashSet<>();
    private transient ImageView imageView;
    private transient HexScreen hexScreen = new HexScreen(this);
    private int x;
    private int y;

    public TerrainType getTerrain() {
        return terrain;
    }

    public HexScreen getHexScreen() {
        return hexScreen;
    }

    public void setHexScreen(HexScreen hexScreen) {
        this.hexScreen = hexScreen;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public HashSet<Army> getArmies() {
        return armies;
    }

    public String getHexName() {
        return "("+x+","+y+")";
    }

    public void addWorkingGroupCamp(WorkingGroup workingGroup) {
        workingGroupsCamps.add(workingGroup);
    }

    public HashSet<IAbleToFight> getCombatants() {
        HashSet<IAbleToFight> result = new HashSet<>(workingGroups);
        result.addAll(armies);
        return result;
    }

    public void depleteRenewableResources() {
        for (Map.Entry<Resource, Integer> res : resources.entrySet())
            if (res.getKey().isRenewable()) {
                int resourceCollected = workingGroupsCamps.stream()
                                            .filter(a->a.getResource().equals(res.getKey()))
                                            .filter(a->a.collectedRenewableResourceThisTurn())
                                            .map(WorkingGroup::getResourceCollected).reduce(0,Integer::sum);

                if (resourceCollected>res.getValue()) {
                    Message depleteResource = new Message();
                    int resourceDepletion = resourceCollected - res.getValue();
                    res.setValue(res.getValue()-resourceDepletion);
                    depleteResource.setHeader(res.getKey().getName() + " at " + getHexName() + "depleted by "+ resourceDepletion);
                    depleteResource.flushToLog();
                }
            }
    }

    public HashSet<WorkingGroup> getWorkingGroupsCamps() {
        return workingGroupsCamps;
    }

    /**
     * This method calculates and applies the result of a conflict.
     * First, it divides all the participants into sides, - each aggressive group is a side,
     * and all non-aggressive groups group together to defend themselves; armies on its destination also ci=ounted as a side
     *
     * Then we calculate the casualties
     */
    public void battle(){
        HashMap<HashSet<IAbleToFight>, Integer> sides = new HashMap<>();
        HashSet<IAbleToFight> nonAggressiveSide = new HashSet<>();

        Message battleMessage = new Message();
        battleMessage.setHeader("Battle at "+getHexName());
        List<String> messageBody = new LinkedList<>();

        int nonAggressiveFirepower = 0;
        int sidesCounter = 0;
        String nonAggressiveTribes = "";
        //Populate the sides of conflict
        for (IAbleToFight fightingGroup: getCombatants()) {
            if (fightingGroup.isAggressive() || fightingGroup.atTarget()) {
                HashSet<IAbleToFight> newSide = new HashSet<>();
                newSide.add(fightingGroup);
                sidesCounter++;
                messageBody.add("Size "+sidesCounter+": "+fightingGroup.getOwner().getName()+"; firepower: " + fightingGroup.getFirepower());
                sides.put(newSide, fightingGroup.getFirepower());

            } else {
                nonAggressiveSide.add(fightingGroup);
                nonAggressiveFirepower += fightingGroup.getFirepower();
                if(nonAggressiveSide.size()>1 )
                    nonAggressiveTribes += ", ";
                nonAggressiveTribes += fightingGroup.getOwner().getName();
            }

        }
        sides.put(nonAggressiveSide, nonAggressiveFirepower);
        messageBody.add("Size "+sidesCounter+": " + nonAggressiveTribes+"; firepower: " + nonAggressiveFirepower);
         //Calculate the total firepower/casualties and a winner
        int totalPeople = getCombatants().stream().map(IAbleToFight::getPeople).reduce(0, (a,b)->(a+b));
        HashSet<IAbleToFight> winner = sides.entrySet().stream().max(Comparator.comparingInt(Map.Entry::getValue)).map(Map.Entry::getKey).get();

        messageBody.add("Winner: " + winner.stream().map(IAbleToFight::getOwner).map(Tribe::getName).reduce("",(a, b)->a+", "+b));
        int totalCaptives = 0;
        //Every  side now get its casualties; winner get captives equails to its firepower
        for (Map.Entry<HashSet<IAbleToFight>, Integer> side : sides.entrySet()) {
            int casualtiesCausedByOpponents = sides.entrySet().stream().filter(a->!a.getKey().equals(side)).map(Map.Entry::getValue).reduce(0,(a,b)->a+b);
            int sideCasualties = 0;
            int sideCaptives = 0;
            int sidePeople = side.getKey().stream().map(IAbleToFight::getPeople).reduce(0, (a,b)->(a+b));
            
            if (sidePeople == 0)
            	continue;
            
            for (Map.Entry<HashSet<IAbleToFight>, Integer> opponent : sides.entrySet() ){

                if (!opponent.equals(side)) {
                    int opponentPeople = opponent.getKey().stream().map(IAbleToFight::getPeople).reduce(0, Integer::sum);
                    int casualtiesCausedByOpponent = (totalPeople - opponentPeople)==0 ? 0 : (int) (opponent.getValue() * (double)(sidePeople / (totalPeople - opponentPeople)));
                        sideCasualties += casualtiesCausedByOpponent;
                        for (IAbleToFight opponentGroup : opponent.getKey())
                            opponentGroup.getOwner().getTribeExperience().put(BranchOfKnowledge.Warcraft,opponentGroup.getOwner().getTribeExperience().getOrDefault(BranchOfKnowledge.Warcraft,0) + casualtiesCausedByOpponent);

                        if (opponent.getKey().equals(winner)) {
                            sideCaptives += (totalPeople - opponentPeople)==0 ? 0 : (int) (opponent.getValue() * (double)(sidePeople / (totalPeople - opponentPeople)));
                            totalCaptives += sideCaptives;
                        for (IAbleToFight winnerGroup : winner) {
                            winnerGroup.takeCaptives((int) (winnerGroup.getPeople() + sideCaptives*winnerGroup.getFirepower() / opponent.getValue()));
                            messageBody.add(winnerGroup.getName() + " from " + winnerGroup.getHomeBase().getName() + " got " + sideCaptives*(double)(winnerGroup.getFirepower() / opponent.getValue()) + " captives");
                            winnerGroup.getHomeBase().addPeople((int)(sideCaptives*(double)(winnerGroup.getFirepower() / opponent.getValue())));
                        }
                    }

                }
            }

            //decrease workgroups' headcount
            for (IAbleToFight wg : side.getKey()) {
                //To do: remove the groups with 0 people
                if (wg.getPeople() - (sideCasualties + sideCaptives) * (double)(wg.getPeople() / sidePeople) <= 0) {
                    messageBody.add(wg.getName() + " totally annihilated!");
                    wg.disband();
                    wg = null;
                } else {
                    messageBody.add(wg.getName() + " lost " + (sideCasualties + sideCaptives) * (double)(wg.getPeople() / sidePeople) + " people");
                    wg.addPeople((int)(-(sideCasualties + sideCaptives) * (double)(wg.getPeople() / sidePeople)));
                    wg.takeCaptives((int)((wg.getPeople() - (sideCasualties + sideCaptives) *(double)(wg.getPeople() / sidePeople))));
                    wg.getHomeBase().addPeople((int)(-(sideCasualties + sideCaptives) * (double)(wg.getPeople() / sidePeople)));
                }
            }
        }

        //all participants of the battle pass their turn
        for (IAbleToFight wg : getCombatants())
            wg.setActedThisTurn(true);

        battleMessage.setBody(messageBody);
        battleMessage.showOnScreen();
//        battleMessage.flushToLog();

    }

    public HashSet<WorkingGroup> getWorkingGroups() {
        return workingGroups;
    }
    public Settlement getSettlement() {
        return  settlement;
    }
    public void setSettlement(Settlement settlement) {
        this.settlement = settlement;
    }
    public TerrainType GetTerrain() {
        return terrain;
    }
    public  HashMap<Resource, Integer> getResources() {
        return resources;
    }

    /**
     * Method to return all the neighbouring hexes for a given hex
     * @return list of hexes neighbouring to a given
     */
    public HashSet<Hex> getNeighbouringHexes() {
        //from columns with even X bottom corners of 3X3 square around the current location are unreachable;
        //from columns with odd X top corners of 3X3 square around the current location are unreachable;

        /*
        even X: +++      Odd X:  -+-
                +O+              +O+
                -+-              +++
         */
        HashSet<Hex> result = new HashSet<>();
        if (this.y>=1) {
            result.add(HexMap.getBoard()[x][y - 1]);
            if (x % 2 == 0) {
                if (x>=1)
                    result.add(HexMap.getBoard()[x - 1][y - 1]);
                if (x< hexgame.BSIZE-1)
                    result.add(HexMap.getBoard()[x + 1][y - 1]);
            }
        }
        if (x>=1)
            result.add(HexMap.getBoard()[x-1][y]);
        if (x<hexgame.BSIZE-1)
            result.add(HexMap.getBoard()[x + 1][y]);
        if (y<hexgame.BSIZE-1) {
            result.add(HexMap.getBoard()[x][y + 1]);
            if (x % 2 == 1) {
                if (x>=1)
                    result.add(HexMap.getBoard()[x - 1][y + 1]);
                if (x<hexgame.BSIZE-1)
                    result.add(HexMap.getBoard()[x + 1][y + 1]);
            }
        }

        return result;
    }

    /**
     * internal method to restore renewable resources
     */
    private void restoreResources(){
        for (Map.Entry<Resource, Integer> res :resources.entrySet()) {
            int maxValue = terrain.getResources().getOrDefault(res.getKey(),0);
            if (res.getKey().isRenewable()&&res.getValue()<maxValue && settlement == null)
                resources.put(res.getKey(),res.getValue()+(int)(maxValue*hexgame.ResourceRestore));

        }

    }

    public boolean hasBattle() {
        boolean result = false;

        for (Army army: armies)
            if (army.getDestination()==this && army.getHomeBase().getLocation()!=this)
                result = true;
        if (workingGroups.size()>1) {
            HashSet<WorkingGroup> groupsOnLocation = new HashSet<>(workingGroups);
            long tribesOnHex = groupsOnLocation.stream().map(WorkingGroup::getOwner).distinct().count();
            for (WorkingGroup wg : groupsOnLocation)
                if (wg.isAggressive() && tribesOnHex > 1) {
                    result = true;
                    break;
                }
        }
        return result;
    }

    public void processDay() {
        if (hasBattle())
            battle();

        restoreResources();
    }

    public void addWorkingGroup(WorkingGroup workingGroup) {
        workingGroups.add(workingGroup);
    }
//    public void calculateWorkResults() {
//        for (Map.Entry<Resource,Integer> res: resources.entrySet()) {
//            HashSet<WorkingGroup> concurents = new HashSet<>();
//            for (WorkingGroup wg : workingGroups) {
//                if (wg.getResource().equals(res.getKey()))
//                    concurents.add(wg);
//            }
//        }
//    }
    public void setResource (Resource resource, int newValue) {
        resources.put(resource, newValue);
    }

    public Hex (TerrainType terrain, int x, int y){
        this.terrain = terrain;
        this.x = x;
        this.y = y;
//        this.hexScreen.setController(new HexScreen(this));
        resources = new HashMap<>();
        for (Map.Entry <Resource, Integer> resource: terrain.getResources().entrySet()
             ) {
            this.resources.put(resource.getKey(),resource.getValue());
        }
//        this.resources.put(Resource.wildGame, (int)(Math.random()*200));
//       if (Math.random()<0.1)
//            resources.put(Resource.flintStone,(int)(Math.random()*200));

    }
}
