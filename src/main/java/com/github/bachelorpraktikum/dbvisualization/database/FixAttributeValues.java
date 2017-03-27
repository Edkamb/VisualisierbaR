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

    static Optional<FixAttributeValues> get(int id) {
        for (FixAttributeValues attributeValue : FixAttributeValues.values()) {
            if (id == attributeValue.getId()) {
                return Optional.of(attributeValue);
            }
        }

        return Optional.empty();
    }
}
