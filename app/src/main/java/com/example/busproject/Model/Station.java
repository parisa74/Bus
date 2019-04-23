package com.example.busproject.Model;


import com.google.gson.annotations.SerializedName;


public class Station {

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("stateId")

    private String stateId;
    @SerializedName("pub")

    private String pub;
    @SerializedName("lat")

    private float lat;
    @SerializedName("lng")

    private float lng;

    private String stationId;

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

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getPub() {
        return pub;
    }

    public void setPub(String pub) {
        this.pub = pub;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

}