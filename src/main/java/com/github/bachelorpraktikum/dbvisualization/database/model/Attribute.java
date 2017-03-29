package com.github.bachelorpraktikum.dbvisualization.database.model;

import com.github.bachelorpraktikum.dbvisualization.database.DBEdge;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.logging.Logger;

public class Attribute implements ABSExportable, Cloneable, Element {

    private final int id;
    private final String title;
    private final String description;
    private final String acronym;
    private Vertex vertex;

    public Attribute(int id, String title, String description, String acronym) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.acronym = acronym;
    }

    public Attribute(ResultSet rs) throws SQLException {
        Iterator<String> columnNames = Tables.ATTRIBUTES.getColumnNames().iterator();
        id = rs.getInt(columnNames.next());
        title = rs.getString(columnNames.next());
        description = rs.getString(columnNames.next());
        acronym = rs.getString(columnNames.next());
    }

    @Override
    protected Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException ignored) {

        }

        return null;
    }

    public int getId() {
        return id;
    }

    public Optional<String> getTitle() {
        return Optional.ofNullable(title);
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

        String hauptsignalFormatString = "HauptSignal %s = new local HauptSignalImpl(%s, %s);";
        String vorsignalFormatString = "VorSignal %s = new local VorSignalImpl(%s);";
        if (getId() == FixAttributeValues.HAUPT_UND_VORSIGNAL.getId()
            || getId() == FixAttributeValues.HAUPT_UND_VORSIGNAL_MIT_SPERRSIGNAL.getId()) {
            if (getEdge() == null) {
                throw new IllegalArgumentException(
                    "Attribute(HauptUndVorsignal) needs to be tied to a edge to be exported.");
            }

            exportString = String.format(hauptsignalFormatString, name, getVertex().getAbsName(),
                getEdge().getAbsName());
            exportString = new StringJoiner(System.lineSeparator()).add(exportString)
                .add(String.format(vorsignalFormatString, getAbsName(), getVertex().getAbsName()))
                .toString();
        } else if (getId() == FixAttributeValues.VORSIGNAL.getId()) {
            exportString = String
                .format(vorsignalFormatString, name, getVertex().getAbsName());
        } else if (getId() == FixAttributeValues.SPERRSIGNAL.getId()) {
            Logger.getLogger(getClass().getName())
                .info("Not creating abs class SPERRSIGNAL, implementation is unknown.");
        } else {
            Optional<FixAttributeValues> fixAttributeOpt = FixAttributeValues.get(getId());
            if (fixAttributeOpt.isPresent()) {
                FixAttributeValues fixAttribute = fixAttributeOpt.get();
                if (fixAttribute.classNameLhs() == null) {
                    return exportString;
                }
                String formattableString = "%s %s = new local %s(%s);";
                exportString = String
                    .format(formattableString, fixAttribute.classNameLhs(), name,
                        fixAttribute.classNameRhs(), getVertex().getAbsName());
                String comment = "// Experimental. This was constructed by using default values. See Attribute.java and FixAttributeValues.java (VisualisierbaR project) for more information.";
                exportString = new StringJoiner(System.lineSeparator()).add(comment)
                    .add(exportString).toString();
            }
        }

        return exportString;
    }

    @Override
    public String getAbsName() {
        String name_prefix = getTitle().orElse(String.valueOf(getId())).replace(" ", "_")
            .toLowerCase();
        return String.format("%s_%d", name_prefix, getId());
    }

    @Override
    public List<String> exportChildren() {
        return Collections.emptyList();
    }

    public void setVertex(Vertex vertex) {
        this.vertex = vertex;
    }

    Vertex getVertex() {
        return vertex;
    }

    Optional<DBEdge> getEdge() {
        return vertex.getEdge();
    }

    public Attribute getClone() {
        return (Attribute) this.clone();
    }
}
