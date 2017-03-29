package com.github.bachelorpraktikum.dbvisualization.database;

import com.github.bachelorpraktikum.dbvisualization.database.model.ABSExportable;
import com.github.bachelorpraktikum.dbvisualization.database.model.Element;
import com.github.bachelorpraktikum.dbvisualization.database.model.Tables;
import com.github.bachelorpraktikum.dbvisualization.database.model.Vertex;
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

    public void setVertexFrom(Vertex from) {
        this.from = from;
    }

    public void setVertexTo(Vertex to) {
        this.to = to;
    }

    public boolean setVertex(Vertex vertex) {
        int vertexID = vertex.getId();
        boolean success = false;
        if (vertexID == fromID) {
            from = vertex;
            success = true;
        } else if (vertexID == toID) {
            to = vertex;
            success = true;
        }

        return success;
    }

    public int getWayNumber() {
        return wayNumber;
    }

    @Override
    public String export() {
        // ABSName, NodeFromName, NodeToName, Length(in m)
        String formattableString = "Edge %s = new local EdgeImpl(%s,%s,%d);";
        return String
            .format(formattableString, getAbsName(), from.getId(), to.getId(), kmToM(getLength()));
    }

    private int kmToM(double length) {
        return new Double(length * 1000).intValue();
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

    public boolean isFree() {
        return wayNumber == -1;
    }
}
