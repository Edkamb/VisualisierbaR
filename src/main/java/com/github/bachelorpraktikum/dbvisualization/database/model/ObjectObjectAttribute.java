package com.github.bachelorpraktikum.dbvisualization.database.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Optional;

public class ObjectObjectAttribute implements Element {

    private final int id;
    private Vertex object1;
    private final int object1ID;
    private Vertex object2;
    private final int object2ID;
    private Attribute attribute;
    private final int attributeID;

    public ObjectObjectAttribute(int id,
        Vertex object1, Vertex object2,
        Attribute attribute) {
        this.id = id;
        this.object1 = object1;
        object1ID = object1.getId();
        this.object2 = object2;
        object2ID = object2.getId();
        this.attribute = attribute;
        attributeID = attribute.getId();
    }

    public ObjectObjectAttribute(ResultSet rs) throws SQLException {
        Iterator<String> columnNames = Tables.OBJECT_OBJECT_ATTRIBUTES.getColumnNames().iterator();
        id = rs.getInt(columnNames.next());
        object1ID = rs.getInt(columnNames.next());
        object2ID = rs.getInt(columnNames.next());
        attributeID = rs.getInt(columnNames.next());
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

    public int getObject1ID() {
        return object1ID;
    }

    public int getObject2ID() {
        return object2ID;
    }

    public int getAttributeID() {
        return attributeID;
    }
}
