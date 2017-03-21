package com.github.bachelorpraktikum.dbvisualization.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Optional;

public class Neighbors {

    private final int id;
    private DBEdge edge;
    private final int edgeID;
    private Betriebsstelle betriebsstelle;
    private final int betriebsstelleID;

    Neighbors(int id, DBEdge edge, Betriebsstelle betriebsstelle) {
        this.id = id;
        this.edge = edge;
        edgeID = edge.getId();
        this.betriebsstelle = betriebsstelle;
        betriebsstelleID = betriebsstelle.getId();
    }

    Neighbors(ResultSet rs) throws SQLException {
        Iterator<String> columnNames = Tables.NEIGHBORS.getColumnNames().iterator();
        id = rs.getInt(columnNames.next());
        edgeID = rs.getInt(columnNames.next());
        betriebsstelleID = rs.getInt(columnNames.next());
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
