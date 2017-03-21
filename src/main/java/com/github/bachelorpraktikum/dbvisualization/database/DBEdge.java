package com.github.bachelorpraktikum.dbvisualization.database;

import java.util.Optional;
public class DBEdge implements ABSExportable {

    private final int id;
    private Vertex from;
    private Vertex to;
    private final int wayNumber;
    private int length;

    public DBEdge(int id, Vertex from,
        Vertex to, int wayNumber, int length) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.wayNumber = wayNumber;
        this.length = length;
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
        return String.format("Edge %s = new local EdgeImpl(%s,%s,%d);");
    }
}
