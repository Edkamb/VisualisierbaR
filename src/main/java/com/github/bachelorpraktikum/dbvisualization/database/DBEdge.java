package com.github.bachelorpraktikum.dbvisualization.database;

public class DBEdge implements ABSExportable {

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
