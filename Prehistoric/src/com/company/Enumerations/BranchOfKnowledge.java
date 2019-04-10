package com.company.Enumerations;

public enum BranchOfKnowledge {
    Craft("Craft"),
    Nature("Nature"),
    Warcraft("Warcraft"),
    Economy("Economy");
    private String name;
    BranchOfKnowledge(String name) {
        this.name = name;
    }
}
