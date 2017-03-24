package com.github.bachelorpraktikum.dbvisualization.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

public class Neighbors {

    private final int id;
    private final int vertex1ID;
    private final int vertex2ID;
    private Vertex vertex1;
    private Vertex vertex2;

    public Neighbors(int id, Vertex vertex1, Vertex vertex2) {
        this.id = id;
        this.vertex1 = vertex1;
        vertex1ID = vertex1.getId();
        this.vertex2 = vertex2;
        vertex2ID = vertex2.getId();
    }

    Neighbors(ResultSet rs) throws SQLException {
        Iterator<String> columnNames = Tables.NEIGHBORS.getColumnNames().iterator();
        id = rs.getInt(columnNames.next());
        vertex1ID = rs.getInt(columnNames.next());
        vertex2ID = rs.getInt(columnNames.next());
    }

    public int getId() {
        return id;
    }


    public Vertex getVertex1() {
        return vertex1;
    }

    public void setVertex1(Vertex vertex1) {
        this.vertex1 = vertex1;
    }

    public Vertex getVertex2() {
        return vertex2;
    }

    public void setVertex2(Vertex vertex2) {
        this.vertex2 = vertex2;
    }

    public int getVertex1ID() {
        return vertex1ID;
    }

    public int getVertex2ID() {
        return vertex2ID;
    }
}
