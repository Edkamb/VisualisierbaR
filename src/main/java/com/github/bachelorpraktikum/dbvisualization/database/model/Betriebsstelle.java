package com.github.bachelorpraktikum.dbvisualization.database.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Betriebsstelle implements ABSExportable, Element {

    private final String ZUGFOLGE_EXPORT = "[HTTPName: \"%s\"]ActiveZugFolge %<s = new ZugFolgeImpl(\"%<s\");";
    private final String BAHNHOF_EXPORT = "[HTTPName: \"%s\"]ZugMelde %<s = new BahnhofImpl(\"%<s\");";

    private final int id;
    private final String title;
    private final String shortName;
    private final String rl100;
    private final int weatherID;
    private final int kennziffer;
    private Attribute attribute;
    private Set<Vertex> vertices;

    public Betriebsstelle(int id, String title, String shortName, String rl100, int weatherID,
        int kennziffer) {
        this.id = id;
        this.title = title;
        this.shortName = shortName;
        this.rl100 = rl100;
        this.weatherID = weatherID;
        this.kennziffer = kennziffer;
    }

    public Betriebsstelle(ResultSet rs) throws SQLException {
        Iterator<String> columnNames = Tables.BETRIEBSSTELLEN.getColumnNames().iterator();
        id = rs.getInt(columnNames.next());
        title = rs.getString(columnNames.next());
        shortName = rs.getString(columnNames.next());
        rl100 = rs.getString(columnNames.next());
        weatherID = rs.getInt(columnNames.next());
        kennziffer = rs.getInt(columnNames.next());
    }

    /**
     * Checks whether the Betriebsstelle has an <tt>ID</tt> larger 0
     *
     * @return Whether the Betriebsstelle is valid
     */
    public boolean isValid() {
        return id != 0;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getShortName() {
        return shortName;
    }

    public String getRl100() {
        return rl100;
    }

    public int getWeatherID() {
        return weatherID;
    }

    public int getKennziffer() {
        return kennziffer;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    /**
     * @param attribute Attribute for the Betriebsstelle
     */
    public void addAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    @Override
    public String export() {
        // TODO
        if (isZugfolge()) {
            return String.format(ZUGFOLGE_EXPORT, getAbsName());
        }

        return String.format(BAHNHOF_EXPORT, getAbsName());
    }

    @Override
    public String getAbsName() {
        String formattableString = "bahnhof_%d";
        if (isZugfolge()) {
            formattableString = "zugfolge_%d";
        }

        return String.format(formattableString, getId());
    }

    @Override
    public List<String> exportChildren() {
        return null;
    }

    /**
     * Checks whether the Betriebsstelle is a Zugfolge. This is hardcoded since it's the same way in
     * the database. The ID for a Zugfolge is defined in `FixAttributeValues`.
     *
     * @return Whether the element is a `Zugfolge`
     */
    boolean isZugfolge() {
        for (Vertex vertex : getVertices()) {
            for (Attribute attribute : vertex.getAttributes()) {
                if (attribute.getId() == FixAttributeValues.ZUGFOLGE.getId()) {
                    return true;
                }
            }
        }

        return false;
    }

    public Set<Vertex> getVertices() {
        return vertices;
    }

    public void addVertex(Vertex vertex) {
        vertices.add(vertex);
    }

    public void addAllVertices(Collection<Vertex> vertices) {
        this.vertices.addAll(vertices);
    }
}
