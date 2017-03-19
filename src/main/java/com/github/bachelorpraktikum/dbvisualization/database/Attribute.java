package com.github.bachelorpraktikum.dbvisualization.database;

public class Attribute extends DBTable implements ABSExportable {

    private final int id;
    private final String title;
    private final String description;
    private final String acronym;

    public Attribute(int id, String title, String description, String acronym) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.acronym = acronym;
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
        return null;
    }
}
