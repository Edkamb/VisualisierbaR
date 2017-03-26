package com.github.bachelorpraktikum.dbvisualization.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

public class Betriebsstelle implements ABSExportable {

    private final int id;
    private final String title;
    private final String shortName;
    private final String rl100;
    private final int weatherID;
    private final int kennziffer;

    public Betriebsstelle(int id, String title, String shortName, String rl100, int weatherID,
        int kennziffer) {
        this.id = id;
        this.title = title;
        this.shortName = shortName;
        this.rl100 = rl100;
        this.weatherID = weatherID;
        this.kennziffer = kennziffer;
    }

    Betriebsstelle(ResultSet rs) throws SQLException {
        Iterator<String> columnNames = Tables.BETRIEBSSTELLEN.getColumnNames().iterator();
        id = rs.getInt(columnNames.next());
        title = rs.getString(columnNames.next());
        shortName = rs.getString(columnNames.next());
        rl100 = rs.getString(columnNames.next());
        weatherID = rs.getInt(columnNames.next());
        kennziffer = rs.getInt(columnNames.next());
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

    @Override
    public String export() {
        // TODO
        if (isZugfolge(null)) {
            return String.format(ZUGFOLGE_EXPORT, getAbsName());
        }

        return String.format(BAHNHOF_EXPORT, getAbsName());
    }

    @Override
    public String getAbsName() {
        String formattableString = "bahnhof_%d";
        if (isZugfolge(attribute)) {
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
     * @param attribute Attribute to check ID against
     */
    public boolean isZugfolge(Attribute attribute) {
        if (this.attribute != null) {
            attribute = this.attribute;
        }
        if (attribute == null) {
            throw new IllegalArgumentException(
                "An attribute has to be provided to check whether the Betriebsstelle is a Bahnhof or a Zugfolge.");
        }

        return attribute.getId() == FixAttributeValues.ZUGFOLGE.getId();
    }
}
