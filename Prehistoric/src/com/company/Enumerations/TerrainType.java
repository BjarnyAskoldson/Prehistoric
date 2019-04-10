package com.company.Enumerations;

import java.util.HashMap;
import javafx.scene.paint.Color;

//import java

public enum TerrainType {
//    Plain (Color.getColor("#FFB911")),
 //   Forest(Color.getColor("#1C720B")),
//    Desert(Color.getColor("F5FF56"));
    Plain (Color.ORANGE, getResourcesList("Plain")),
    Forest(Color.GREEN, getResourcesList("Forest")),
    Desert(Color.YELLOW, getResourcesList("Desert"));
    private final Color color;
    private HashMap  <Resource, Integer> resourcesMax;
    private static HashMap  <Resource, Integer> getResourcesList(String type) {
        HashMap  <Resource, Integer> result = new HashMap  <Resource, Integer>();
        switch (type) {
            case "Plain":
                result.put(Resource.wildGame,150);
                result.put(Resource.flintStone, 50);
                result.put(Resource.wood, 50);
                result.put(Resource.arableLand, 1000);
                break;
            case "Forest":
                result.put(Resource.wildGame,200);
                result.put(Resource.wood, 1000);
                result.put(Resource.arableLand, 300);
                break;
            case "Desert":
                result.put(Resource.wildGame,50);
                result.put(Resource.wood, 50);
                result.put(Resource.flintStone, 200);
                break;
        }
        return result;
    }
    TerrainType (Color color, HashMap  <Resource, Integer>resources) {
        this.resourcesMax = resources;
        this.color = color;
    }
    public Color getColor() {
        return this.color;
    }
    public  HashMap  <Resource, Integer> getResources() {
        return resourcesMax;
    }
}

