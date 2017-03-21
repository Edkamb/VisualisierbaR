package com.github.bachelorpraktikum.dbvisualization.database;

import java.util.Optional;
public class ObjectObjectAttribute {

    private final int id;
    private Vertex object1;
    private Vertex object2;
    private Attribute attribute;

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

    public Optional<Vertex> getObject1() {
        return Optional.ofNullable(object1);
    }

    public Optional<Vertex> getObject2() {
        return Optional.ofNullable(object2);
    }

    public Optional<Attribute> getAttribute() {
        return Optional.ofNullable(attribute);
    }

    }

    }
}
