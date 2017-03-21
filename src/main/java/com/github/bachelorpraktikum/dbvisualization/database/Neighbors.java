package com.github.bachelorpraktikum.dbvisualization.database;

import java.util.Optional;

public class Neighbors {

    private final int id;
    private DBEdge edge;
    private final int edgeID;
    private Betriebsstelle betriebsstelle;
    private final int betriebsstelleID;

    public Neighbors(int id, DBEdge edge, Betriebsstelle betriebsstelle) {
        this.id = id;
        this.edge = edge;
        edgeID = edge.getId();
        this.betriebsstelle = betriebsstelle;
        betriebsstelleID = betriebsstelle.getId();
    }

    public int getId() {
        return id;
    }

    public Optional<DBEdge> getEdge() {
        return Optional.ofNullable(edge);
    }

    public Optional<Betriebsstelle> getBetriebsstelle() {
        return Optional.ofNullable(betriebsstelle);
    }

    public int getEdgeID() {
        return edgeID;
    }

    public int getBetriebsstelleID() {
        return betriebsstelleID;
    }
}
