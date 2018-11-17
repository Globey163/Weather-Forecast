package com.example.yassin.weatherforecast.DBObj;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "forecasts")
public class DBForecast {

    @PrimaryKey
    public int id;

    private double latitude;
    private double longitude;

    private String approvedTime;


    public DBForecast(double latitude, double longitude, String approvedTime){

        this.latitude = latitude;
        this.longitude = longitude;
        this.approvedTime = approvedTime;
        id = hashCode();
    }


    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    public int getId() { return id; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getApprovedTime() { return approvedTime; }
}
