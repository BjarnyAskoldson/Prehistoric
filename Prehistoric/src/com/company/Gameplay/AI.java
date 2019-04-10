package com.company.Gameplay;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.company.Enumerations.Equipment;
import com.company.Interfaces.IProducible;

public class AI implements Serializable {
	
	private Tribe tribe;
	public void processDay() {
		orderWeapons();
		createTroops();
		sendExpeditions();
		
	}
	private void createTroops() {
		
		for (Settlement settlement : tribe.getSettlements()) {
			Army currentArmy = new Army (settlement,0,new HashMap<Equipment, Integer>());
			if (settlement.getArmies().size() == 0) {
				settlement.getArmies().add(currentArmy);
			
			}
			else
				for (Army army : settlement.getArmies()) {
					currentArmy = army;
					break;
				}
					
			for (Map.Entry<Equipment, Integer> equipment : settlement.getEquipment().entrySet()) {
					currentArmy.addEquipment(equipment.getKey(), equipment.getValue());
				
			}
		}
	}
	/**
	 * Method to order weapons. We don't order more than we have resources or food for
	 */
	private void orderWeapons() {
		if (tribe.getTreasury()>0) {
			int fundsAvailable = tribe.getTreasury();
			for (Settlement settlement : tribe.getSettlements())
				for (Equipment equipment : tribe.getEquipmentAvailableToOrder()) {
					if (equipment.getMaterials() == null) {
						settlement.orderEquipment(equipment, fundsAvailable);
						fundsAvailable = 0;
						break;
					}
					//if(settlement.getResources().containsKey(equipment.getMaterials().keySet())) {
						//Order either how much resources or how much funds we have, - whichever is smaller
						int amountToOrder = fundsAvailable;
						for (Map.Entry<IProducible, Integer> material : equipment.getMaterials().entrySet()) {
							if (amountToOrder>settlement.commodityPerTurn(material.getKey(), true)//amount we can produce
									+settlement.getResources().getOrDefault(material.getKey(), 0)) //amount already in warehouse
								amountToOrder = settlement.commodityPerTurn(material.getKey(), true)//amount we can produce
										+settlement.getResources().getOrDefault(material.getKey(), 0); //amount already in warehouse
								
						}
						//>settlement.getResources().getOrDefault(equipment.getResource(), 0)?settlement.getResources().getOrDefault(equipment.getResource(), 0):fundsAvailable;
						settlement.orderEquipment(equipment, amountToOrder);
					//}
					if (tribe.getTreasury()<=0)
						break;
				}
		}
	}
	
	/**
	 * Method to add working hexes to settlements where possible
	 */
	private void sendExpeditions() {
		for (Settlement settlement : tribe.getSettlements()) {
            if (!settlement.hasTroopsToPatrol())
            	continue;

            //Lets send AI resource expeditions
            //Resource resourceToCollect = Resource.wildGame;
            for (Hex hex: settlement.getLocation().getNeighbouringHexes() ) {
                //Don't send resource expedition to neighboring village
                if (hex.getSettlement() != null)
                    continue;

                //Lets check if we already collecting the resource in demand on a given hex
                WorkingGroup wgOnResource = null;
                
                if (!settlement.getWorkingHexes().contains(hex))
                	settlement.sendExpedition(hex, 0, 0);
                /*
                for (WorkingGroup wg: hex.getWorkingGroupsCamps())
                    if (wg.getResource() == resourceToCollect && wg.getHomeBase().getOwner().equals(this))
                        wgOnResource = wg;

                if (wgOnResource == null) {
                    //If there is no work group yet, send one
                    int peopleAvailable = settlement.getIdlePeople()+settlement.getBufferGroup(resourceToCollect.getCommodity().getNeed()).getPeople() - hexgame.SETTLEMENTLOCALWILDGAME;
                    int resourceAvailable = hex.getResources().getOrDefault(resourceToCollect, 0);
                    int peopleToSend = 0;
                    if (peopleAvailable > resourceAvailable)
                        peopleToSend = resourceAvailable;
                    else
                        peopleToSend = peopleAvailable;


                    if (peopleToSend > 0)
                        settlement.sendExpedition(hex, peopleToSend, resourceAvailable);
                    else
                        break;
                } else {
                    //otherwise lets increase its headcount if possible
                    int peopleAvailable = settlement.getIdlePeople() - hexgame.SETTLEMENTLOCALWILDGAME + wgOnResource.getPeople();
                    int resourceAvailable = hex.getResources().getOrDefault(resourceToCollect, 0);
                    int peopleToSend = 0;
                    if (peopleAvailable > resourceAvailable)
                        peopleToSend = resourceAvailable;
                    else
                        peopleToSend = peopleAvailable;
                    if (wgOnResource.getPeopleMax()<peopleToSend)
                    wgOnResource.setPeopleMax(peopleToSend);

                }
                */
            }
		}
			
	}
	public AI(Tribe tribe) {
		this.tribe = tribe;
	}
}
