package com.github.bachelorpraktikum.dbvisualization.database;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;

public class ABSExporter {

    private final List<ABSExportable> elements;
    private boolean exportString;

    ABSExporter() {
        this(new LinkedList<>());
    }

    ABSExporter(List<ABSExportable> elements) {
        this.elements = elements;
    }

    private String getEnclosingFunctionFormattable() {
        String lineSep = System.lineSeparator();
        return String.format("Unit run() {%s%s%s%s}", lineSep, "%s", "%s", lineSep);
    }

    private String getLineSeperatedElements() {
        StringJoiner lineSeperatorJoiner = new StringJoiner(System.lineSeparator());
        elements.forEach(absExportable -> lineSeperatorJoiner.add(absExportable.export()));

        return lineSeperatorJoiner.toString();
    }

    private String getExtra() {
        return "";
    }

    /**
     * Writes the abs content (enclosed in a `run` function) to the given stream.
     *
     * @param outputStream Stream to write abs export to
     * @throws IOException Thrown if writting to the <code>outputStream</code> was unsuccessful
     */
    public void export(OutputStream outputStream) throws IOException {
        outputStream.write(getExportString().getBytes());
    }

    public List<ABSExportable> getElements() {
        return elements;
    }

    private String getExportString() {
        return String
            .format(getEnclosingFunctionFormattable(), getLineSeperatedElements(), getExtra());
    }
}
