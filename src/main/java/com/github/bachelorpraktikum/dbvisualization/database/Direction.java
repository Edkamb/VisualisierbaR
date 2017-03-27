package com.github.bachelorpraktikum.dbvisualization.database;

public enum Direction implements Element {
    None("None", 0),
    AscendingChainage("Ascending", 1),
    DescendingChainage("Descending", 2),
    Both("Both", 3);

    private String name;
    private int numericalValue;

    Direction(String name, int numericalValue) {
        this.name = name;
        this.numericalValue = numericalValue;
    }

    String getName() {
        return name;
    }

    int numericalValue() {
        return numericalValue;
    }

    public static Direction get(int index) {
        if (index > Direction.values().length) {
            throw new IllegalArgumentException(
                "Index can't be larger than the length of the Direction enum.");
        }

        for (Direction dir : Direction.values()) {
            if (dir.numericalValue() == index) {
                return dir;
            }
        }

        return null;
    }
}
