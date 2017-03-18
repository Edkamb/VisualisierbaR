package com.github.bachelorpraktikum.dbvisualization.database;

public class ObjectAttribute {

    private final int id;
    private final String type;
    private final Vertex object;
    private final Attribute attribute;
    private final String value;

    public ObjectAttribute(int id, String type, Vertex object, Attribute attribute,
        String value) {
        this.id = id;
        this.type = type;
        this.object = object;
        this.attribute = attribute;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Vertex getVertex() {
        return object;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public String getValue() {
        return value;
    }
}
