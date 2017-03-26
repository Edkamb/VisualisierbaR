package com.github.bachelorpraktikum.dbvisualization.database;

enum FixAttributeValues {
    ZUGFOLGE(85);

    private final int id;

    FixAttributeValues(int id) {
        this.id = id;
    }

    int getId() {
        return id;
    }
}
