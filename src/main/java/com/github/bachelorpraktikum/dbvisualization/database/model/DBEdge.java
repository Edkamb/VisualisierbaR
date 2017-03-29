package com.github.bachelorpraktikum.dbvisualization.database.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

public class DBEdge implements ABSExportable, Element {

    private final int id;
    private Vertex from;
    private final int fromID;
    private Vertex to;
    private final int toID;
    private final int wayNumber;
    private double length;
    private Set<Vertex> vertices;

    public DBEdge(int id, Vertex from,
        Vertex to, int wayNumber) {
        this.id = id;
        this.from = from;
        fromID = from.getId();
        this.to = to;
        toID = to.getId();
        this.wayNumber = wayNumber;
        setLength();
        vertices = new HashSet<>();
    }

    public DBEdge(ResultSet rs) throws SQLException {
        Iterator<String> columnNames = Tables.EDGES.getColumnNames().iterator();
        id = rs.getInt(columnNames.next());
        fromID = rs.getInt(columnNames.next());
        toID = rs.getInt(columnNames.next());
        wayNumber = rs.getInt(columnNames.next());
        vertices = new HashSet<>();
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

    /**
     * Checks whether the given vertex is equivalent to the <code>FROM</code> or the <code>TO</code>
     * vertex and sets it accordingly. The success of this operation is returned
     * <code>from.ID</code> or <code>to.ID</code>
     *
     * @param vertex Vertex to check for
     * @return Whether the vertex is on this edge
     */
    public boolean setVertex(Vertex vertex) {
        int vertexID = vertex.getId();
        boolean success = false;
        if (vertexID == fromID) {
            from = vertex;
            success = true;
        } else if (vertexID == toID) {
            to = vertex;
            success = true;
        } else if (vertex.getEdgeID() == getId()) {
            addVertex(vertex);
            success = true;
        }

        return success;
    }

    public int getWayNumber() {
        return wayNumber;
    }

    /**
     * <p>{@inheritDoc}</p>
     *
     * <p>Attention: The {@link Vertex#getId()} is used to get the ID from the
     * <code>from</code> and <code>to</code> vertices. An info message will be shown when neither of
     * them is found. This is, to prevent using an Edge with no corresponding <code>from</code>
     * and <code>to</code> vertices. This shouldn't occur if the 'track' in the database is
     * correct.</p>
     */
    @Override
    public String export() {
        // ABSName, NodeFromName, NodeToName, Length(in m)
        String formattableString = "Edge %s = new local EdgeImpl(%s,%s,%d);";
        if (from == null || to == null) {
            String message = String
                .format("Edge with ID %d is has a null vertex | From: %d(%s) | To: %d(%s)", getId(),
                    getFromID(), from, getToID(), to);
            Logger.getLogger(getClass().getName()).info(message);
            return "";
        }
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
        return Collections.emptyList();
    }

    public int getFromID() {
        return fromID;
    }

    public int getToID() {
        return toID;
    }

    public Set<Vertex> getVertices() {
        return vertices;
    }

    public boolean addVertex(Vertex vertex) {
        return vertices.add(vertex);
    }

    public boolean isFree() {
        return wayNumber == -1;
    }
}
