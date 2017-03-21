package com.github.bachelorpraktikum.dbvisualization.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Optional;

public class DBEdge implements ABSExportable {

    private final int id;
    private Vertex from;
    private final int fromID;
    private Vertex to;
    private final int toID;
    private final int wayNumber;
    private int length;

    DBEdge(int id, Vertex from,
        Vertex to, int wayNumber, int length) {
        this.id = id;
        this.from = from;
        fromID = from.getId();
        this.to = to;
        toID = to.getId();
        this.wayNumber = wayNumber;
        this.length = length;
    }

    DBEdge(ResultSet rs) throws SQLException {
        Iterator<String> columnNames = Tables.EDGES.getColumnNames().iterator();
        id = rs.getInt(columnNames.next());
        fromID = rs.getInt(columnNames.next());
        toID = rs.getInt(columnNames.next());
        wayNumber = rs.getInt(columnNames.next());
    }

    public int getId() {
        return id;
    }

    public Optional<Vertex> getFrom() {
        return Optional.ofNullable(from);
    }

    public Optional<Vertex> getTo() {
        return Optional.ofNullable(to);
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getWayNumber() {
        return wayNumber;
    }

    @Override
    public String export() {
        // ABSName, NodeToName, NodeFromName, Length
        String formattableString = "Edge %s = new local EdgeImpl(%s,%s,%d);";
        String absName = String.format("edge_%d", id);
        String exportString = String
            .format(formattableString, absName, from.getId(), to.getId(), getLength());
        return exportString;
    }

    public int getFromID() {
        return fromID;
    }

    public int getToID() {
        return toID;
    }
}
