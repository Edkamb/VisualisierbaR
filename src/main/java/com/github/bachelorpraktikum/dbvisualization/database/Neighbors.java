package com.github.bachelorpraktikum.dbvisualization.database;

public class Neighbors {

    private final int id;
    private final DBEdge edge;
    private final Betriebsstelle betriebsstelle;

    public Neighbors(int id, DBEdge edge, Betriebsstelle betriebsstelle) {
        this.id = id;
        this.edge = edge;
        this.betriebsstelle = betriebsstelle;
    }

    public int getId() {
        return id;
    }

    public DBEdge getEdgeID() {
        return edge;
    }

    public Betriebsstelle getKennzifferBST() {
        return betriebsstelle;
    }
}
