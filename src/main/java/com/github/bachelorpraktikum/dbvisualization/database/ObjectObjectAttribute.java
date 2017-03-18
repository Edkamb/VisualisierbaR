package com.github.bachelorpraktikum.dbvisualization.database;

public class ObjectObjectAttribute {

    private final int id;
    private final Vertex object1;
    private final Vertex object2;
    private final Attribute attribute;

    public ObjectObjectAttribute(int id,
        Vertex object1, Vertex object2,
        Attribute attribute) {
        this.id = id;
        this.object1 = object1;
        this.object2 = object2;
        this.attribute = attribute;
    }

    public int getId() {
        return id;
    }

    public Vertex getObject1() {
        return object1;
    }

    public Vertex getObject2() {
        return object2;
    }

    public Attribute getAttribute() {
        return attribute;
    }
}
