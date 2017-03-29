package com.github.bachelorpraktikum.dbvisualization.database;

import java.util.List;
import java.util.StringJoiner;

public class ABSExporter {

    private final List<ABSExportable> elements;

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

    }

    public List<ABSExportable> getElements() {
        return elements;
    }
}
