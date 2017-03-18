package com.github.bachelorpraktikum.dbvisualization.database;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public enum Tables {
    BETRIEBSSTELLEN("betriebsstellen", new String[]{
        "id", "titel", "Kurzname", "rl100", "wetter_ID", "Kennziffer_BST"
    }),

    VERTICES("vertices", new String[]{
        "ID", "betriebsstellen_ID", "Kennziffer", "name", "km", "direction", "edge_ID", "XLocal",
        "YLocal", "XGlobal", "YGlobal"
    }),

    OBJECT_OBJECT_ATTRIBUTES("object_object_attributes", new String[]{
        "ID", "object1_ID", "object2_ID", "attribute_ID"
    }),

    NEIGHBORS("neighbors", new String[]{
        "Kennziffer_BST", "edgeName", "Kennziffer_BST_Nachbar"
    }),

    EDGES("edges", new String[]{
        "ID", "vertex_ID_from", "vertex_ID_to", "wayNumber"
    }),

    OBJECTS_ATTRIBUTES("objects_attributes", new String[]{
        "ID", "type", "object_ID", "attribute_ID", "value"
    }),

    ATTRIBUTES("attributes", new String[]{
        "ID", "titel", "description", "acronym"
    });

    private String name;
    private List<String> columnNames;

    Tables(String name, String[] columnNames) {
        this.columnNames = new LinkedList<>();
        Collections.addAll(this.columnNames, columnNames);
        this.name = name;
    }

    String getName() {
        return name;
    }

    List<String> getColumnNames() {
        return columnNames;
    }
}
