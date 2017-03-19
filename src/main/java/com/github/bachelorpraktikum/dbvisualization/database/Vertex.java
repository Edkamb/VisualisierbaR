package com.github.bachelorpraktikum.dbvisualization.database;

import com.github.bachelorpraktikum.dbvisualization.model.Coordinates;

public class Vertex implements ABSExportable {

    private final int id;
    private Betriebsstelle betriebsstelle;
    private final int betriebsstelle_ID;
    private final int kennziffer;
    private final String name;
    private final double length;
    private final Direction direction;
    private DBEdge edge;
    private final int edge_ID;
    private final Coordinates local;
    private final Coordinates global;

    Vertex(int id, int betriebsstelle_id, int kennziffer, String name, double length,
        Direction direction, int edge_id, Coordinates local, Coordinates global) {
        this.id = id;
        betriebsstelle_ID = betriebsstelle_id;
        this.kennziffer = kennziffer;
        this.name = name;
        this.length = length;
        this.direction = direction;
        edge_ID = edge_id;
        this.local = local;
        this.global = global;
    }

    public int getId() {
        return id;
    }

    // TODO
    public Betriebsstelle getBetriebsstelle() {
        return betriebsstelle;
    }

    public void setBetriebsstelle(Betriebsstelle bst) {
        betriebsstelle = bst;
    }

    public int getBetriebsstelle_ID() {
        return betriebsstelle_ID;
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

    // TODO
    public DBEdge getEdge() {
        return edge;
    }

    public void setEdge(DBEdge edge) {
        this.edge = edge;
    }

    public Coordinates getLocal() {
        return local;
    }

    public Coordinates getGlobal() {
        return global;
    }

    public int getEdge_ID() {
        return edge_ID;
    }

    @Override
    public String export() {
        String absName = String.format("node_%d", id);
        String formatableString = "Node %s = new local NodeImpl(%d,%d);";
        return String.format(formatableString, absName, local.getX(), local.getY());
    }
}
