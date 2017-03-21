package com.github.bachelorpraktikum.dbvisualization.database;

import java.util.Optional;

public class Neighbors {

    private final int id;
    private DBEdge edge;
    private Betriebsstelle betriebsstelle;

    public Neighbors(int id, DBEdge edge, Betriebsstelle betriebsstelle) {
        this.id = id;
        this.edge = edge;
        this.betriebsstelle = betriebsstelle;
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
    }

    }
}
