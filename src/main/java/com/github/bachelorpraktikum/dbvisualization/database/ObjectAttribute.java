package com.github.bachelorpraktikum.dbvisualization.database;

import java.util.Optional;

public class ObjectAttribute {

    private final int id;
    private final String type;
    private Vertex object;
    private Attribute attribute;
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

    public Optional<Vertex> getVertex() {
        return Optional.ofNullable(object);
    }

    public Optional<Attribute> getAttribute() {
        return Optional.ofNullable(attribute);
    }

    }

    public String getValue() {
        return value;
    }
}
