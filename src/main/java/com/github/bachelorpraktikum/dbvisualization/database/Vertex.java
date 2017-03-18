package com.github.bachelorpraktikum.dbvisualization.database;

import com.github.bachelorpraktikum.dbvisualization.model.Coordinates;

public class Vertex {

    private final int id;
    private final Betriebsstelle betriebsstelle;
    private final int kennziffer;
    private final String name;
    private final double length;
    private final Direction direction;
    private final DBEdge edge;
    private final Coordinates local;
    private final Coordinates global;

    Vertex(int id, Betriebsstelle betriebsstelle, int kennziffer, String name, double length,
        Direction direction, DBEdge edge, Coordinates local, Coordinates global) {
        this.id = id;
        this.betriebsstelle = betriebsstelle;
        this.kennziffer = kennziffer;
        this.name = name;
        this.length = length;
        this.direction = direction;
        this.edge = edge;
        this.local = local;
        this.global = global;
    }

    public int getId() {
        return id;
    }

    public Betriebsstelle getBetriebsstelle() {
        return betriebsstelle;
    }

    public int getKennziffer() {
        return kennziffer;
    }

    public String getName() {
        return name;
    }

    public double getLength() {
        return length;
    }

    public Direction getDirection() {
        return direction;
    }

    public DBEdge getEdge() {
        return edge;
    }

    public Coordinates getLocal() {
        return local;
    }

    public Coordinates getGlobal() {
        return global;
    }
}
