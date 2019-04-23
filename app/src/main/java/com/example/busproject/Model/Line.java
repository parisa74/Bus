
package com.example.busproject.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Line {

  @SerializedName("id")
    private int id;
    @SerializedName("name")

    private String name;

    @SerializedName("color")

    private String color;

    @SerializedName("Stations")

    private List<Station> stations = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<Station> getStations() {
        return stations;
    }

    public void setStations(List<Station> stations) {
        this.stations = stations;
    }

}