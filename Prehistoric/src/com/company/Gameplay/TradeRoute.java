package com.company.Gameplay;

import com.company.Enumerations.Transport;

import java.io.Serializable;

import com.company.HexMap;
import com.company.Interfaces.IProducible;

public class TradeRoute implements Serializable {
    private Settlement homeBase;
    private Settlement destination;
    private IProducible goods;
    private double purchasePrice;
    private double sellPrice;
    private int volume;

    public Settlement getDestination() {
        return destination;
    }

    public Settlement getHomeBase() {
        return homeBase;
    }

    public IProducible getGoods() {
        return goods;
    }

    //method to forecast profits on a given trade route
    public double profit(Transport transport) {
        int pathLength = HexMap.getPathLength(homeBase.getLocation(), destination.getLocation());
        int pathExpenses = /*HexMap.getPath(homeBase.getLocation(), destination.getLocation()).size()**/pathLength*transport.getCrew();
        double revenue = transport.getCapacity()/goods.getWeight() * (purchasePrice - sellPrice);
        double result = revenue - pathExpenses;
        return result;
    }

    public TradeRoute (Settlement homeBase, Settlement destination, IProducible goods, double purchasePrice, double sellPrice, int volume) {
        this.homeBase = homeBase;
        this.destination = destination;
        this.goods = goods;
        this.purchasePrice = purchasePrice;
        this.sellPrice = sellPrice;
        this.volume = volume;
    }
}
