package com.github.bachelorpraktikum.dbvisualization.database;

public class DBEdge {

    private final int id;
    private final Vertex from;
    private final Vertex to;
    private final int wayNumber;

    public DBEdge(int id, Vertex from,
        Vertex to, int wayNumber) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.wayNumber = wayNumber;
    }

    public int getId() {
        return id;
    }

    public Vertex getFrom() {
        return from;
    }

    public Vertex getTo() {
        return to;
    }

    public int getWayNumber() {
        return wayNumber;
    }
}
