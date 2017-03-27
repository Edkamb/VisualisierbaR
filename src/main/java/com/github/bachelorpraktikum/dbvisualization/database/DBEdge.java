package com.github.bachelorpraktikum.dbvisualization.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class DBEdge implements ABSExportable, Element {

    private final int id;
    private Vertex from;
    private final int fromID;
    private Vertex to;
    private final int toID;
    private final int wayNumber;
    private double length;

    public DBEdge(int id, Vertex from,
        Vertex to, int wayNumber) {
        this.id = id;
        this.from = from;
        fromID = from.getId();
        this.to = to;
        toID = to.getId();
        this.wayNumber = wayNumber;
        setLength();
    }

    public DBEdge(ResultSet rs) throws SQLException {
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

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public void setLength() {
        length = (Math.max(from.getLength(), to.getLength()) -
            Math.min(from.getLength(), to.getLength()));
    }
    public int getWayNumber() {
        return wayNumber;
    }

    @Override
    public String export() {
        // ABSName, NodeToName, NodeFromName, Length
        String formattableString = "Edge %s = new local EdgeImpl(%s,%s,%d);";
        return String
            .format(formattableString, getAbsName(), from.getId(), to.getId(), getLength());
    }

    @Override
    public String getAbsName() {
        return String.format("edge_%d", getId());
    }

    @Override
    public List<String> exportChildren() {
        return null;
    }

    public int getFromID() {
        return fromID;
    }

    public int getToID() {
        return toID;
    }
}
