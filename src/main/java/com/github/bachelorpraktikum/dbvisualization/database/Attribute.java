package com.github.bachelorpraktikum.dbvisualization.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

public class Attribute extends DBTable implements ABSExportable, Element {

    private final int id;
    private final String title;
    private final String description;
    private final String acronym;

    public Attribute(int id, String title, String description, String acronym) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.acronym = acronym;
    }

    Attribute(ResultSet rs) throws SQLException {
        Iterator<String> columnNames = Tables.ATTRIBUTES.getColumnNames().iterator();
        id = rs.getInt(columnNames.next());
        title = rs.getString(columnNames.next());
        description = rs.getString(columnNames.next());
        acronym = rs.getString(columnNames.next());
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAcronym() {
        return acronym;
    }

    @Override
    public String export() {
        return null;
    }

    @Override
    public String getAbsName() {
        return null;
    }

    @Override
    public List<String> exportChildren() {
        return Collections.emptyList();
    }

    void setVertex(Vertex vertex) {
        this.vertex = vertex;
    }

    Vertex getVertex() {
        return vertex;
    }

    void setEdge(DBEdge edge) {
        this.edge = edge;
    }

    DBEdge getEdge() {
        return edge;
    }
}
