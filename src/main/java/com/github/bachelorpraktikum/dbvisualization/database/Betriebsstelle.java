package com.github.bachelorpraktikum.dbvisualization.database;

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
        return null;
    }
}
