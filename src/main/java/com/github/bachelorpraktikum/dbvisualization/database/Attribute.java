package com.github.bachelorpraktikum.dbvisualization.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

public class Attribute extends DBTable implements ABSExportable, Element {

    private final int id;
    private final String title;
    private final String description;
    private final String acronym;
    private Vertex vertex;
    private DBEdge edge;

    public Attribute(int id, String title, String description, String acronym) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.acronym = acronym;
    }

    Attribute(ResultSet rs) throws SQLException {
        Iterator<String> columnNames = Tables.ATTRIBUTES.getColumnNames().iterator();
        id = rs.getInt(columnNames.next());
        title = rs.getString(columnNames.next());
        description = rs.getString(columnNames.next());
        acronym = rs.getString(columnNames.next());
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAcronym() {
        return acronym;
    }

    @Override
    public String export() {
        if (vertex == null) {
            throw new IllegalArgumentException(
                "Attribute needs to be tied to a vertex to be exported.");
        }

        String name = getAbsName();
        String exportString = "";

        if (getId() == FixAttributeValues.HAUPT_UND_VORSIGNAL.getId()
            || getId() == FixAttributeValues.HAUPT_UND_VORSIGNAL_MIT_SPERRSIGNAL.getId()) {
            String hauptsignalFormatString = "HauptSignal %s = new local HauptSignalImpl(%s, %s);";
            String vorsignalFormatString = "VorSignal %s = new local VorSignalImpl(%s);";

            exportString = String.format(hauptsignalFormatString, name, getVertex().getAbsName(),
                getEdge().getAbsName());
            exportString = new StringJoiner(System.lineSeparator()).add(exportString)
                .add(vorsignalFormatString).toString();
        } else if () {

        } else {
            Optional<FixAttributeValues> fixAttributeOpt = FixAttributeValues.get(getId());
            if (fixAttributeOpt.isPresent()) {
                FixAttributeValues fixAttribute = fixAttributeOpt.get();
                String formattableString = "%s %s = new local %s(%s);";
                exportString = String
                    .format(formattableString, fixAttribute.classNameLhs(), name,
                        fixAttribute.classNameRhs(), getVertex().getAbsName());
                String comment = "// Experimental. This was constructed by using default values. See Attribute.java and FixAttributeValues.java (VisualisierbaR project) for more information.";
                exportString = new StringJoiner(System.lineSeparator()).add(comment)
                    .add(exportString).toString();
            } else {
                // TODO
            }
        }

        return exportString;
    }

    @Override
    public String getAbsName() {
        return null;
    }

    @Override
    public List<String> exportChildren() {
        return Collections.emptyList();
    }

    void setVertex(Vertex vertex) {
        this.vertex = vertex;
    }

    Vertex getVertex() {
        return vertex;
    }

    void setEdge(DBEdge edge) {
        this.edge = edge;
    }

    DBEdge getEdge() {
        return edge;
    }
}
