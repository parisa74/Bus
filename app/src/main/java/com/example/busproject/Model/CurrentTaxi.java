package com.example.busproject.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurrentTaxi {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("info")
    @Expose
    private TaxiInfo info;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public TaxiInfo getInfo() {
        return info;
    }

    public void setInfo(TaxiInfo info) {
        this.info = info;
    }

}