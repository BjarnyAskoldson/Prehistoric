package com.company.Interfaces;

import com.company.Gameplay.Settlement;
import com.company.Gameplay.Tribe;

public interface IAbleToFight {
    int getPeople();
    int getFirepower();
    Tribe getOwner();
    boolean isAggressive();
    String getName();
    Settlement getHomeBase();
    void takeCaptives(int captives);
    void disband();
    boolean atTarget();
    boolean isMissionCompleted();
    void addPeople(int people);
    void setActedThisTurn(boolean acted);

}
