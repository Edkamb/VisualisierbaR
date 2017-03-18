package com.github.bachelorpraktikum.dbvisualization.database;

public enum Direction {
    None("None"),
    AscendingChainage("Ascending"),
    DescendingChainage("Descending"),
    Both("Both");

    private String name;

    Direction(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }
}
