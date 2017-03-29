package com.github.bachelorpraktikum.dbvisualization.database.model;

import com.github.bachelorpraktikum.dbvisualization.model.Coordinates;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Vertex implements ABSExportable, Element {

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
    private Set<Attribute> attributes;

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

        attributes = new HashSet<>();
    }

    public Vertex(ResultSet rs) throws SQLException {
        Iterator<String> columnNames = Tables.VERTICES.getColumnNames().iterator();
        id = rs.getInt(columnNames.next());
        betriebsstelle_ID = rs.getInt(columnNames.next());
        kennziffer = rs.getInt(columnNames.next());
        name = rs.getString(columnNames.next());
        length = rs.getDouble(columnNames.next());
        int direction_index = rs.getInt(columnNames.next());
        direction = Direction.get(direction_index);
        edge_ID = rs.getInt(columnNames.next());
        int localX = rs.getInt(columnNames.next());
        int localY = rs.getInt(columnNames.next());
        // TODO
        local = new Coordinates(Math.max(localX, 0), Math.max(localY, 0));
        int globalX = rs.getInt(columnNames.next());
        int globalY = rs.getInt(columnNames.next());
        // TODO
        global = new Coordinates(Math.max(globalX, 0), Math.max(globalY, 0));

        attributes = new HashSet<>();
    }

    /**
     * Checks whether the Vertex is valid (The vertex is in a Betriebsstelle)
     *
     * @return Whether the Vertex is valid
     */
    public boolean isValid() {
        return betriebsstelle_ID != 0;
    }

    public int getId() {
        return id;
    }

    // TODO
    public Optional<Betriebsstelle> getBetriebsstelle() {
        return Optional.ofNullable(betriebsstelle);
    }

    public void setBetriebsstelle(Betriebsstelle bst) {
        betriebsstelle = bst;
    }

    public int getBetriebsstelleID() {
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

    public Optional<DBEdge> getEdge() {
        return Optional.ofNullable(edge);
    }

    public void setEdge(DBEdge edge) {
        this.edge = edge;
    }

    /**
     * Returns the local coordinates in the corresponding <tt>Betriebsstelle</tt>
     *
     * @return Local coordinates (local to the Betriebsstelle Coordinate)
     */
    @Deprecated
    public Coordinates getLocalCoordinates() {
        return local;
    }

    public Coordinates getGlobalCoordinates() {
        return global;
    }

    public int getEdgeID() {
        return edge_ID;
    }

    /**
     * Adds an attribute to the attributes set of the vertex
     *
     * @param attribute Attribute to add
     * @return <tt>true</tt> if this set did not already contain the specified element
     */
    public boolean addAttribute(Attribute attribute) {
        return attributes.add(attribute);
    }

    Set<Attribute> getAttributes() {
        return attributes;
    }

    @Override
    public String export() {
        String formattableString = "Node %s = new local NodeImpl(%d,%d);";
        return String.format(formattableString, getAbsName(), global.getX(), global.getY());
    }

    @Override
    public String getAbsName() {
        return String.format("node_%d", id);
    }

    @Override
    public List<String> exportChildren() {
        return attributes.stream().map(Attribute::export).collect(Collectors.toList());
    }
}
